import java.util.List;

public class WeightTrackerUtils {

    /**
     * Formats the animal name for display in the report header.
     * @param name the animal's name
     * @return the formatted name string
     */
    public static double doStuff(double weight) {
        return weight * 2.20462;
    }

    public static double calculateAverage(List<Double> weights) {
        double sum = 0;
        for (Double w : weights) {
            sum += w;
        }
        return sum / weights.size();
    }
}
