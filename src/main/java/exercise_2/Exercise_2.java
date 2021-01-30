package exercise_2;

import org.apache.spark.api.java.JavaSparkContext;
import scala.Tuple2;
import java.util.*;

public class Exercise_2 {

    public static String happinessRanking(JavaSparkContext spark) {
        String out = "";

        // We begin the exercise by tackling each file in an independent way, so we enable the possible
        // parallelization of the same tasks that apply to these first Transformations and the defined
        // final Action, which are the same for both 3 files.

        Tuple2<Double, String> report_2015 = spark.textFile("src/main/resources/2015_long.csv")
                // We perform 2 predicates within the same filter in order to save 1 additional Transformation,
                // so we achieve higher efficiency because of the smaller amount of data that will result from
                // this filter (we are pushing down the Selections). That is, there is less potential data
                // transfers when Shuffling at the end of each Stage (main bottleneck in distributed systems):
                .filter(t -> !t.contains("Country")&&t.contains("Europe"))
                // Here, we perform a strict projection just with the columns that will be necessary to
                // yield the expected output afterwards (we are pushing down Projections here). They are
                // the Happiness Score (Key) and the Country (Value), yielding again less amount of data
                // to transfer over the network among Workers when Shuffling in a potential Big Data
                // environment:
                .mapToPair(t -> new Tuple2<Double,String>(Double.parseDouble(t.split(",")[3]),
                        t.split(",")[0]))
                // Finally, we decide to perform a reverse Sort Transformation with the smaller data set
                // resulting from the 2 previous Transformations, so that it becomes more lightweight to
                // perform. In addition to this, we decide to then perform an Action to collect the very
                // first element, which will be the Tuple2<HappinessScore,Country> with the highest
                // Happiness Score for the year file we are reading from:
                .sortByKey(false).first();

        Tuple2<Double, String> report_2016 = spark.textFile("src/main/resources/2016_long.csv")
                // Same rationale as the above same file operations:
                .filter(t -> !t.contains("Country")&&t.contains("Europe"))
                // Same rationale as the above same file operations:
                .mapToPair(t -> new Tuple2<Double,String>(Double.parseDouble(t.split(",")[3]),
                        t.split(",")[0]))
                // Same rationale as the above same file operations:
                .sortByKey(false).first();

        Tuple2<Double, String> report_2017 = spark.textFile("src/main/resources/2017_long.csv")
                // Same rationale as the above same file operations:
                .filter(t -> !t.contains("Country")&&t.contains("Europe"))
                // Same rationale as the above same file operations:
                .mapToPair(t -> new Tuple2<Double,String>(Double.parseDouble(t.split(",")[3]),
                        t.split(",")[0]))
                // Same rationale as the above same file operations:
                .sortByKey(false).first();

        // Then, we build a very simple list with the 3 resulting elements, which are the countries
        // with the highest Happiness Score in 2015, 2016 and 2017, respectively, and we use the efficient
        // built-in 'Collections.sort()' from Java to sort such a list.

        // Then, we have defined below a simple Class that implements the 'Comparator' interface and overrides
        // the method 'compare()', so we are now able to compare Tuple2 objects and, for our case of interest,
        // let Java know how it must compare Tuple2 objects when sorted. Therefore, we just pick up the last
        // one (which holds the highest Happiness Score value) and we print it in the output:
        List<Tuple2<Double, String>> high_scores = Arrays.asList(report_2015, report_2016, report_2017);
        Collections.sort(high_scores, new Tuple2Comparator());
        Tuple2<Double, String> top = high_scores.get(2);

        out = top._2+" is the happiest country in Europe for 2015, 2016 and 2017 with an score of "+top._1;

        return out;
    }
}

// Here, we implement a special Comparator to provide in the 'Collections.sort()' function in order to
// let Java know how it must sort our Java List of Tuple2 objects, which is by means of the first element
// of the tuple (in our case, the Happiness Score), treating it as primitive 'double':
class Tuple2Comparator implements Comparator<Tuple2> {
    @Override
    public int compare(Tuple2 o1, Tuple2 o2) {
        double t1_1 = (double) o1._1;
        double t2_1 = (double)o2._1;
        return Double.compare(t1_1, t2_1);
    }
}
