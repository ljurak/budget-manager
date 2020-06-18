package budget;

import java.math.BigDecimal;

public class BudgetItem {

    private final String name;

    private final BigDecimal amount;

    private final BudgetItemType type;

    private final PurchaseCategory category;

    public BudgetItem(String name, BigDecimal amount, BudgetItemType type, PurchaseCategory category) {
        this.name = name;
        this.amount = amount;
        this.type = type;
        this.category = category;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public BudgetItemType getType() {
        return type;
    }

    public PurchaseCategory getCategory() {
        return category;
    }

    @Override
    public String toString() {
        return String.format("%s $%.2f", name, amount);
    }
}
