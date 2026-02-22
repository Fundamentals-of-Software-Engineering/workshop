public class Dog extends Animal {

    public Dog(String name, double weight, String furColor) {
        super(name, weight, furColor);
    }

    @Override
    public String makeSound() {
        return "Woof";
    }
}
