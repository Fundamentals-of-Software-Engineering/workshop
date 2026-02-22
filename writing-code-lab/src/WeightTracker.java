import java.util.ArrayList;
import java.util.List;

public class WeightTracker {

    private static final double HEAVY_THRESHOLD = 100.0;
    private List<Animal> animals = new ArrayList<>();

    public void addAnimal(Animal animal) {
        animals.add(animal);
    }

    public void printWeightReport() {
        if (animals.isEmpty()) {
            System.out.println("No animals");
            return;
        }

        List<Double> weights = new ArrayList<>();
        boolean hasHeavyAnimal = false;

        for (Animal animal : animals) {
            double weight = animal.getWeight();
            weights.add(weight);
            System.out.println(WeightReportFormatter.formatRow(animal.getName(), weight));

            if (weight > HEAVY_THRESHOLD) {
                hasHeavyAnimal = true;
            }
        }

        System.out.println("----------------------------------");

        double average = WeightCalculator.average(weights);
        System.out.println(WeightReportFormatter.formatRow("Average", average));

        if (hasHeavyAnimal) {
            System.out.println("WARNING: Some animals exceed " + HEAVY_THRESHOLD + " lbs!");
        }
    }

    public void printSoundCheck() {
        for (Animal animal : animals) {
            System.out.println(animal.getName() + " says: " + animal.makeSound());
        }
    }

    public void printHeavyAnimals() {
        String heavyAnimals = WeightReportFormatter.formatHeavyAnimals(animals, 50);
        System.out.println("Heavy animals: " + heavyAnimals);
    }
}
