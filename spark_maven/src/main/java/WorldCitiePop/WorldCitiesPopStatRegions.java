package WorldCitiePop;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.HBaseAdmin;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.io.ImmutableBytesWritable;
import org.apache.hadoop.hbase.mapreduce.TableInputFormat;
import org.apache.hadoop.hbase.mapreduce.TableOutputFormat;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.PairFunction;
import org.apache.spark.util.StatCounter;

import scala.Tuple2;

public class WorldCitiesPopStatRegions {
	private final static String tableName = "worldCitiesPopPaysDG2";
	private final static String columnFamily = "DG2";

	public static boolean isDouble(String str) {
		try {
			Double.parseDouble(str);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public static void main(String[] args) throws Exception {

		Configuration conf = HBaseConfiguration.create();
		HBaseAdmin hBaseAdmin = null;
		try {
			hBaseAdmin = new HBaseAdmin(conf);
			if (hBaseAdmin.isTableAvailable(tableName)) {
				System.out.println("Table " + tableName + " is available.");

			} else {
				System.out.println("Table " + tableName + " is not available.");
			}
		} catch (IOException e) {

			e.printStackTrace();
		} finally {
			hBaseAdmin.close();

		}
		conf.set(TableInputFormat.INPUT_TABLE, tableName);
		Job newAPIJobConfiguration = Job.getInstance(conf);
		newAPIJobConfiguration.getConfiguration().set(TableOutputFormat.OUTPUT_TABLE, tableName);
		newAPIJobConfiguration.setOutputFormatClass(org.apache.hadoop.hbase.mapreduce.TableOutputFormat.class);
		FileOutputFormat.setOutputPath(newAPIJobConfiguration, new Path(args[2]));

		SparkConf conff = new SparkConf().setAppName("worldCitiesPopPaysDG2");
		JavaSparkContext context = new JavaSparkContext(conff);
		String inputPath = args[0];
		String region_codes = args[1];
		JavaRDD<String> recordsRegion = context.textFile(region_codes, 20);
		JavaRDD<String[]> recordsLineRegion = recordsRegion.map(line -> line.split(","));
		JavaRDD<Tuple2<String, String>> rddregion = recordsLineRegion.map(data -> {
			return new Tuple2<>((data[0] + "*" + data[1]).toLowerCase(), data[2]);
		});
		JavaPairRDD<String, String> pairrddRegion = JavaPairRDD.fromJavaRDD(rddregion);

		JavaRDD<String> records = context.textFile(inputPath, 20);
		JavaRDD<String[]> recordsLine = records.map(line -> line.split(","));
		JavaRDD<Tuple2<String, Double>> rddpop = recordsLine.map(data -> {

			Double population = -1.;
			if (isDouble(data[4])) {
				population = Double.parseDouble(data[4]);
				return new Tuple2<>((data[0] + "*" + data[3]).toLowerCase(), population);
			}

			return new Tuple2<>("", 0.0);
		});
		JavaPairRDD<String, Double> rddp = JavaPairRDD.fromJavaRDD(rddpop);
		JavaPairRDD<String, StatCounter> rddpopStat = rddp.aggregateByKey(new StatCounter(), (acc, x) -> acc.merge(x),
				(acc1, acc2) -> acc1.merge(acc2));
		JavaRDD<Tuple2<String, Double>> rddpr = rddpopStat.map(x -> new Tuple2<>(x._1, x._2.sum()));
		JavaPairRDD<String, Double> pairrddPopSum = JavaPairRDD.fromJavaRDD(rddpr);

		JavaRDD<Tuple2<String, Double>> rddlat = recordsLine.map(data -> {

			Double population = -1.;
			if (isDouble(data[4])) {
				population = Double.parseDouble(data[4]);
				return new Tuple2<>((data[0] + "*" + data[3]).toLowerCase(), Double.parseDouble(data[5]));
			}

			return new Tuple2<>("", 0.0);
		});
		JavaPairRDD<String, Double> rddl = JavaPairRDD.fromJavaRDD(rddlat);
		JavaPairRDD<String, StatCounter> rddlatStat = rddl.aggregateByKey(new StatCounter(), (acc, x) -> acc.merge(x),
				(acc1, acc2) -> acc1.merge(acc2));
		JavaRDD<Tuple2<String, Double>> rddLatMoy = rddlatStat.map(x -> new Tuple2<>(x._1, x._2.mean()));
		JavaPairRDD<String, Double> pairrddLatMOy = JavaPairRDD.fromJavaRDD(rddLatMoy);

		JavaRDD<Tuple2<String, Double>> rddlng = recordsLine.map(data -> {

			Double population = -1.;
			if (isDouble(data[4])) {
				population = Double.parseDouble(data[4]);
				return new Tuple2<>((data[0] + "*" + data[3]).toLowerCase(), Double.parseDouble(data[6]));
			}

			return new Tuple2<>("", 0.0);
		});
		JavaPairRDD<String, Double> rddlngg = JavaPairRDD.fromJavaRDD(rddlng);
		JavaPairRDD<String, StatCounter> rddlngStat = rddlngg.aggregateByKey(new StatCounter(),
				(acc, x) -> acc.merge(x), (acc1, acc2) -> acc1.merge(acc2));
		JavaRDD<Tuple2<String, Double>> rddLngMoy = rddlngStat.map(x -> new Tuple2<>(x._1, x._2.mean()));
		JavaPairRDD<String, Double> pairrddLNgMOy = JavaPairRDD.fromJavaRDD(rddLngMoy);

		JavaPairRDD<String, Tuple2<Double, Double>> joinPOpLat = pairrddPopSum.join(pairrddLatMOy);
		JavaPairRDD<String, Tuple2<Tuple2<Double, Double>, Double>> joinTot = joinPOpLat.join(pairrddLNgMOy);
		JavaPairRDD<String, Tuple2<Tuple2<Tuple2<Double, Double>, Double>, String>> joinTotRegion = joinTot
				.join(pairrddRegion);
		JavaRDD<String> rddfinale = joinTotRegion.map(x -> x._1().toString() + "," + (x._2()._1()._1()._1).toString()
				+ "," + (x._2()._1()._1()._2).toString() + "," + (x._2()._1()._2()).toString() + "," + x._2()._2());
		JavaPairRDD<ImmutableBytesWritable, Put> hbasePuts = rddfinale
				.mapToPair(new PairFunction<String, ImmutableBytesWritable, Put>() {
					public Tuple2<ImmutableBytesWritable, Put> call(String tuple) throws Exception {
						String tokens[] = tuple.split(",");
						Put put = new Put(Bytes.toBytes(tokens[0]));
						put.add(Bytes.toBytes(columnFamily), Bytes.toBytes("population"), Bytes.toBytes(tokens[1]));
						put.add(Bytes.toBytes(columnFamily), Bytes.toBytes("latitude"), Bytes.toBytes(tokens[2]));
						put.add(Bytes.toBytes(columnFamily), Bytes.toBytes("longtitude"), Bytes.toBytes(tokens[3]));
						put.add(Bytes.toBytes(columnFamily), Bytes.toBytes("region"), Bytes.toBytes(tokens[4]));
						return new Tuple2<ImmutableBytesWritable, Put>(new ImmutableBytesWritable(), put);
					}
				});
		hbasePuts.saveAsNewAPIHadoopDataset(newAPIJobConfiguration.getConfiguration());

	}
}
