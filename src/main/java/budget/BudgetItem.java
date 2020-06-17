package budget;

import java.math.BigDecimal;

public class BudgetItem {

    private final String name;

    private final BigDecimal amount;

    private final BudgetItemType type;

    public BudgetItem(String name, BigDecimal amount, BudgetItemType type) {
        this.name = name;
        this.amount = amount;
        this.type = type;
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

    @Override
    public String toString() {
        return String.format("%s $%.2f", name, amount);
    }
}
