public class Fish extends Animal {

    public Fish(String name, double weight) {
        super(name, weight, null); // fish don't have fur but we need to pass something
    }

    @Override
    public String makeSound() {
        return ""; // fish don't make sounds
    }
}
