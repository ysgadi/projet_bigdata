package Dem3Lat_Lng_Ht;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaDoubleRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.util.StatCounter;

public class Dem3LatLngHtStat {
	public static void main(String[] args) throws Exception {

		SparkConf conff = new SparkConf().setAppName("Dem3LatLngStat");
		JavaSparkContext context = new JavaSparkContext(conff);
		String inputPath = args[0];
		JavaRDD<String> records = context.textFile(inputPath, 20);
		JavaRDD<String[]> recordsLine = records.map(line -> line.split(","));

		JavaRDD<Double> latrdd = recordsLine.map(line -> {

			Double lt = Double.parseDouble(line[0]);

			return lt;
		});
		JavaRDD<Double> longrdd = recordsLine.map(line -> {

			Double lng = Double.parseDouble(line[1]);

			return lng;
		});
		JavaRDD<Double> htrdd = recordsLine.map(line -> {

			Double ht = Double.parseDouble(line[2]);

			return ht;
		});
		JavaDoubleRDD rddslat = latrdd.mapToDouble(x -> x);
		JavaDoubleRDD rddslng = longrdd.mapToDouble(x -> x);
		JavaDoubleRDD rddsht = htrdd.mapToDouble(x -> x);
		StatCounter statlat = rddslat.stats();
		StatCounter statlng = rddslng.stats();
		StatCounter statht = rddsht.stats();
		System.out.println("MinLat: " + statlat.min());
		System.out.println("MaxLat: " + statlat.max());
		System.out.println("MinLong: " + statlng.min());
		System.out.println("MaxLong: " + statlng.max());
		System.out.println("MinHt: " + statht.min());
		System.out.println("MaxHt: " + statht.max());

	}
}
