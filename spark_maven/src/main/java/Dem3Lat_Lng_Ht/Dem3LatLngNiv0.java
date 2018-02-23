package Dem3Lat_Lng_Ht;

import java.awt.Point;
import java.io.IOException;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.SortedMap;
import java.util.TreeMap;

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
import org.apache.spark.api.java.AbstractJavaRDDLike;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.*;
import org.apache.spark.util.StatCounter;

import GridSingleton.Grid;
import GridSingleton.GridTreeMap1;
import scala.Function2;
import scala.Tuple2;

public class Dem3LatLngNiv0 {
	private final static String tableName = "heightPointDataDG0";
	private final static String columnFamily = "DG0";

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

		SparkConf conff = new SparkConf().setAppName("Niveau0Dem3LatLng");
		JavaSparkContext context = new JavaSparkContext(conff);
		String inputPath = args[0];
		GridTreeMap1 gridsInstance = GridTreeMap1.getInstance();
		JavaRDD<String> records = context.textFile(inputPath, 20);
		JavaRDD<String[]> recordsLine = records.map(line -> line.split(","));
		SortedMap<Integer, Grid> g = gridsInstance.grids;
		JavaRDD<Tuple2<Integer, Double>> latrdd = recordsLine.map(line -> {
			Double lt = Double.parseDouble(line[0]);
			Double lng = Double.parseDouble(line[1]);
			Double ht = Double.parseDouble(line[2]);
			for (Grid grid : g.values()) {
				if ((lt > grid.getLatMin() && lt < grid.getLatMax())
						&& (lng > grid.getLongMin() && lng < grid.getLongMax())) {
					return new Tuple2<>(grid.getNumPx(), lt);
				}
			}
			return new Tuple2<>(0, 0.0);
		});
		JavaPairRDD<Integer, Double> pairrddLat = JavaPairRDD.fromJavaRDD(latrdd);
		JavaPairRDD<Integer, StatCounter> rddRlat = pairrddLat.aggregateByKey(new StatCounter(),
				(acc, x) -> acc.merge(x), (acc1, acc2) -> acc1.merge(acc2));
		JavaRDD<Tuple2<Integer, Double>> rddLatMoy = rddRlat.map(x -> new Tuple2<>(x._1, x._2.mean()));
		JavaPairRDD<Integer, Double> pairrddLatMoy = JavaPairRDD.fromJavaRDD(rddLatMoy);

		JavaRDD<Tuple2<Integer, Double>> lngrdd = recordsLine.map(line -> {
			Double lt = Double.parseDouble(line[0]);
			Double lng = Double.parseDouble(line[1]);
			for (Grid grid : g.values()) {
				if ((lt > grid.getLatMin() && lt < grid.getLatMax())
						&& (lng > grid.getLongMin() && lng < grid.getLongMax())) {
					return new Tuple2<>(grid.getNumPx(), lng);
				}
			}
			return new Tuple2<>(0, 0.0);
		});
		JavaPairRDD<Integer, Double> pairrddLng = JavaPairRDD.fromJavaRDD(lngrdd);
		JavaPairRDD<Integer, StatCounter> rddRLng = pairrddLng.aggregateByKey(new StatCounter(),
				(acc, x) -> acc.merge(x), (acc1, acc2) -> acc1.merge(acc2));
		JavaRDD<Tuple2<Integer, Double>> rddLngMoy = rddRLng.map(x -> new Tuple2<>(x._1, x._2.mean()));
		JavaPairRDD<Integer, Double> pairrddLngMoy = JavaPairRDD.fromJavaRDD(rddLngMoy);

		JavaRDD<Tuple2<Integer, Double>> htrdd = recordsLine.map(line -> {
			Double lt = Double.parseDouble(line[0]);
			Double lng = Double.parseDouble(line[1]);
			Double ht = Double.parseDouble(line[2]);
			for (Grid grid : g.values()) {
				if ((lt > grid.getLatMin() && lt < grid.getLatMax())
						&& (lng > grid.getLongMin() && lng < grid.getLongMax())) {
					return new Tuple2<>(grid.getNumPx(), ht);
				}
			}
			return new Tuple2<>(0, 0.0);
		});
		JavaPairRDD<Integer, Double> pairrddHt = JavaPairRDD.fromJavaRDD(htrdd);
		JavaPairRDD<Integer, StatCounter> rddRHt = pairrddHt.aggregateByKey(new StatCounter(), (acc, x) -> acc.merge(x),
				(acc1, acc2) -> acc1.merge(acc2));
		JavaRDD<Tuple2<Integer, Double>> rddhtMoy = rddRHt.map(x -> new Tuple2<>(x._1, x._2.mean()));
		JavaPairRDD<Integer, Double> pairrddhttMoy = JavaPairRDD.fromJavaRDD(rddhtMoy);
		JavaPairRDD<Integer, Tuple2<Double, Double>> joinLtLng = pairrddLatMoy.join(pairrddLngMoy);
		JavaPairRDD<Integer, Tuple2<Tuple2<Double, Double>, Double>> joinTot = joinLtLng.join(pairrddhttMoy);

		JavaRDD<String> rddfinal = joinTot.map(x -> x._1().toString() + "," + (x._2._1()._1()).toString() + ","
				+ (x._2._1()._2()).toString() + "," + (x._2._2()).toString());
		JavaPairRDD<ImmutableBytesWritable, Put> hbasePuts = rddfinal
				.mapToPair(new PairFunction<String, ImmutableBytesWritable, Put>() {
					public Tuple2<ImmutableBytesWritable, Put> call(String tuple) throws Exception {
						String tokens[] = tuple.split(",");
						Put put = new Put(Bytes.toBytes(tokens[0]));
						put.add(Bytes.toBytes(columnFamily), Bytes.toBytes("latitude"), Bytes.toBytes(tokens[1]));
						put.add(Bytes.toBytes(columnFamily), Bytes.toBytes("longtitude"), Bytes.toBytes(tokens[2]));
						put.add(Bytes.toBytes(columnFamily), Bytes.toBytes("hauteur"), Bytes.toBytes(tokens[3]));

						return new Tuple2<ImmutableBytesWritable, Put>(new ImmutableBytesWritable(), put);
					}
				});
		hbasePuts.saveAsNewAPIHadoopDataset(newAPIJobConfiguration.getConfiguration());

	}
}
