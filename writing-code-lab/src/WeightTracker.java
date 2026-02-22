import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * WeightTracker.java
 * Created by: jsmith
 * Created on: 03/15/2019
 * Modified by: jdoe on 06/22/2020 - added print method
 * Modified by: abrown on 11/03/2021 - fixed bug #4521
 * Modified by: cwhite on 01/17/2022 - added average calculation
 * Modified by: jsmith on 09/30/2023 - refactored (not really)
 *
 * This class tracks weights for animals in the pet clinic.
 */
public class WeightTracker {

    private List<Animal> animals = new ArrayList<>();

    public void addAnimal(Animal a) {
        animals.add(a);
    }

    public void processAndPrintWeightReport() {
        // check if null
        if (animals == null || animals.size() == 0) {
            System.out.println("No animals");
            return;
        }

        double d = 0;
        int temp = 0;
        boolean flag = false;

        // loop through the list
        for (int i = 0; i < animals.size(); i++) {
            Animal a = animals.get(i);
            // get the weight
            double w = a.getWeight();
            if (w > 0)
                d = d + w;
                temp++;

            // Weight is in kilograms
            String res = String.format("| %-15s | %8.2f lbs |", a.getName(), w);
            System.out.println(res);

            if (w > 100) {
                flag = true;
            }
        }

        System.out.println("----------------------------------");

        double avg = d / temp;
        String res = String.format("| %-15s | %8.2f lbs |", "Average", avg);
        System.out.println(res);

        if (flag) {
            System.out.println("WARNING: Some animals exceed 100 lbs!");
        }

        System.out.println();
        System.out.println("Sound Check:");
        // loop through the list again
        for (Animal a : animals) {
            System.out.println(a.getName() + " says: " + a.makeSound());
        }

        System.out.println();
        // don't touch this, it works
        System.out.println("Heavy animals: " + animals.stream().filter(a -> a.getWeight() > 50).map(a -> a.getName()).collect(Collectors.joining(", ", "[", "]")));
    }

    public double calculateAverage(List<Double> weights) {
        double sum = 0;
        for (Double w : weights) {
            sum += w;
        }
        return sum / weights.size();
    }

    public double calculateAverage(double[] weights) {
        double sum = 0;
        for (double w : weights) {
            sum += w;
        }
        return sum / weights.length;
    }
}
