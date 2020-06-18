package budget;

public enum PurchaseCategory {
    FOOD("Food"),
    CLOTHES("Clothes"),
    ENTERTAINMENT("Entertainment"),
    OTHER("Other"),
    ALL("All");

    private String name;

    PurchaseCategory(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
