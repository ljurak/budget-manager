package budget;

import java.math.BigDecimal;

public class PurchaseItem {

    private final String description;

    private final BigDecimal price;

    public PurchaseItem(String description, BigDecimal price) {
        this.description = description;
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    @Override
    public String toString() {
        return description + " $" + price;
    }
}
