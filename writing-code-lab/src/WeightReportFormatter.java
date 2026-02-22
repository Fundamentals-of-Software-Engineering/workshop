import java.util.List;
import java.util.stream.Collectors;

public class WeightReportFormatter {

    public static String formatRow(String name, double weight) {
        return String.format("| %-15s | %8.2f lbs |", name, weight);
    }

    public static String formatHeavyAnimals(List<Animal> animals, double threshold) {
        return animals.stream()
                .filter(animal -> animal.getWeight() > threshold)
                .map(Animal::getName)
                .collect(Collectors.joining(", ", "[", "]"));
    }
}
