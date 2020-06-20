package budget;

public enum PurchaseCategory {

    FOOD("Food", 1),
    CLOTHES("Clothes", 2),
    ENTERTAINMENT("Entertainment", 3),
    OTHER("Other", 4),
    ALL("All", 5);

    private String name;

    private int category;

    PurchaseCategory(String name, int category) {
        this.name = name;
        this.category = category;
    }

    public static PurchaseCategory valueOfCategory(int value) {
        for (PurchaseCategory category : values()) {
            if (category.category == value) {
                return category;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return name;
    }
}
