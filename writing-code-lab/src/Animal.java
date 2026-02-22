public class Animal {

    private String name;
    private double weight;
    private String furColor; // all animals have fur right?

    public Animal(String name, double weight, String furColor) {
        this.name = name;
        this.weight = weight;
        this.furColor = furColor;
    }

    public String makeSound() {
        return "...";
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public String getFurColor() {
        return furColor;
    }

    public void setFurColor(String furColor) {
        this.furColor = furColor;
    }
}
