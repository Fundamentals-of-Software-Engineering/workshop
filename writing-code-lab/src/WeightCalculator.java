import java.util.List;

public class WeightCalculator {

    public static double average(List<Double> weights) {
        return weights.stream()
                .mapToDouble(Double::doubleValue)
                .average()
                .orElse(0.0);
    }

    public static double kilogramsToPounds(double kilograms) {
        return kilograms * 2.20462;
    }
}
