public class Animal {

    private String name;
    private double weight;
    private SoundBehavior soundBehavior;

    public Animal(String name, double weight, SoundBehavior soundBehavior) {
        this.name = name;
        this.weight = weight;
        this.soundBehavior = soundBehavior;
    }

    public String makeSound() {
        return soundBehavior.makeSound();
    }

    public String getName() {
        return name;
    }

    public double getWeight() {
        return weight;
    }
}
