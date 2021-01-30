package exercise_1;

import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import scala.Tuple2;
import scala.Tuple3;
import java.util.*;
import java.util.stream.Collectors;


public class Exercise_1 {

	public static String kNN_prediction(JavaSparkContext ctx) {
	    String out = "";

	    JavaRDD<String> file = ctx.textFile("src/main/resources/breast.csv")
				.filter(t -> !t.contains("id"))
				// We cache it (alias of 'persist()' Transformation), since it will be use twice below:
				.cache();

	    // We get a Java Base RDD with all lines that are 'test' (ended in '0'), and all lines that are
		// 'train' (ended in '1'):
	    JavaRDD<String> test = file.filter(t -> t.split(",")[32].equals("0"));
		JavaRDD<String> train = file.filter(t -> t.split(",")[32].equals("1"));

		// We perform the 'cartesian()' Transformation that will yield the test rows as Keys, and the
		// train rows as Values, and we implement the 'eucl_dist()' function to get Tuple2 objects:
		out = test.cartesian(train).mapToPair(t -> eucl_dist(t))

				// Then, we compute the minimum between every 2 Tuple2 objects by just comparing the 3rd
				// value (Euclidean Distance) of the Tuple3 object that has been defined as the Value
				// of the Java Pair RDD:
				.reduceByKey((t1, t2) -> {
					if (t1._3() < t2._3()) {
						return new Tuple3<>(t1._1(), t1._2(), t1._3());
					}
					else {
						return new Tuple3<>(t2._1(), t2._2(), t2._3());
					}
				})

				// We get rid of the Key, since we already found the minimum Euclidean Distance for
				// all possible train rows, and we retrieve just the diagnosis from train and test rows:
				.values().map(t -> t._1()+"_"+t._2())

				// Finally, we straightforwardly use 'countByValue()' action, which gives us the train_test
				// predicted cancer and the 4 possible combinations that let us build the Confusion Matrix:
				.countByValue().entrySet().stream()

				// We implement the following in order to get exactly the very same input as stated in the
				// exercise statement:
				.sorted(Comparator.comparing(Map.Entry::getKey))
				.map(t -> t.getKey()+"\t"+t.getValue()+"\n").collect(Collectors.joining());

	    return out;
	}

	private static Tuple2<String, Tuple3<String, String, Double>> eucl_dist(Tuple2<String, String> t1) {
		ArrayList<String> predictors = Utils_1NN.getPredictors();
		double eucl_dist = 0;
		for (int i = 0; i < predictors.size(); i++) {
			Double test_val = Double.valueOf(Utils_1NN.getAttribute(t1._1.split(","), predictors.get(i)));
			Double train_val = Double.valueOf(Utils_1NN.getAttribute(t1._2.split(","), predictors.get(i)));
			eucl_dist += Math.pow(test_val - train_val, 2);
		}
		return new Tuple2<>(t1._1.split(",")[0],
				new Tuple3<>(t1._2.split(",")[1], t1._1.split(",")[1], Math.sqrt(eucl_dist)));
	}

}

