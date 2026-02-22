public class Dog extends Animal {

    private String furColor;

    public Dog(String name, double weight, String furColor) {
        super(name, weight, () -> "Woof");
        this.furColor = furColor;
    }

    public String getFurColor() {
        return furColor;
    }
}
