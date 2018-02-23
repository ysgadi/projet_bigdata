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

public class WorldCitiesPopStatVilles {
	private final static String tableName = "worldCitiesPopPaysDG3";
	private final static String columnFamily = "DG3";

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
		FileOutputFormat.setOutputPath(newAPIJobConfiguration, new Path(args[1]));

		SparkConf conff = new SparkConf().setAppName("worldCitiesPopPaysDG3");
		JavaSparkContext context = new JavaSparkContext(conff);
		String inputPath = args[0];
		JavaRDD<String> records = context.textFile(inputPath, 20);
		JavaRDD<String[]> recordsLine = records.map(line -> line.split(","));
		JavaRDD<Tuple2<String, String>> rddpop = recordsLine.map(data -> {
			boolean isdbl = false;
			if (isDouble(data[4])) {
				isdbl = true;
			}
			if (isdbl)
				return new Tuple2<>(data[5] + "*" + data[6], data[1] + "," + data[4] + "," + data[5] + "," + data[6]);
			else
				return new Tuple2<>("", "");
		});
		JavaPairRDD<String, String> rddp = JavaPairRDD.fromJavaRDD(rddpop);
		JavaPairRDD<String, String> rddpf = rddp.filter(x -> !((x._1()).isEmpty()));
		JavaRDD<String> rddfinale = rddpf.map(x -> x._1() + "," + x._2());
		JavaPairRDD<ImmutableBytesWritable, Put> hbasePuts = rddfinale
				.mapToPair(new PairFunction<String, ImmutableBytesWritable, Put>() {
					public Tuple2<ImmutableBytesWritable, Put> call(String tuple) throws Exception {
						String tokens[] = tuple.split(",");

						Put put = new Put(Bytes.toBytes(tokens[0]));
						put.add(Bytes.toBytes(columnFamily), Bytes.toBytes("ville"), Bytes.toBytes(tokens[1]));
						put.add(Bytes.toBytes(columnFamily), Bytes.toBytes("population"), Bytes.toBytes(tokens[2]));
						put.add(Bytes.toBytes(columnFamily), Bytes.toBytes("latitude"), Bytes.toBytes(tokens[3]));
						put.add(Bytes.toBytes(columnFamily), Bytes.toBytes("longtitude"), Bytes.toBytes(tokens[4]));
						return new Tuple2<ImmutableBytesWritable, Put>(new ImmutableBytesWritable(), put);
					}
				});
		hbasePuts.saveAsNewAPIHadoopDataset(newAPIJobConfiguration.getConfiguration());

	}
}
