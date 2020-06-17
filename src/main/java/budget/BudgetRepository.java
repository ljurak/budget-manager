package budget;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class BudgetRepository {

    private List<BudgetItem> budgetItems = new ArrayList<>();

    public void addIncome(BigDecimal amount) {
        budgetItems.add(new BudgetItem("Income", amount, BudgetItemType.INCOME));
    }

    public void addPurchase(String name, BigDecimal amount) {
        budgetItems.add(new BudgetItem(name, amount, BudgetItemType.PURCHASE));
    }

    public List<BudgetItem> getPurchases() {
        return  budgetItems.stream()
                .filter(item -> item.getType() == BudgetItemType.PURCHASE)
                .collect(Collectors.toList());
    }

    public BigDecimal getTotalPurchases() {
        BigDecimal total = new BigDecimal(0);
        for (BudgetItem item : budgetItems) {
            if (item.getType() == BudgetItemType.PURCHASE) {
                total = total.add(item.getAmount());
            }
        }
        return total;
    }

    public BigDecimal getTotalBalance() {
        BigDecimal balance = new BigDecimal(0);
        for (BudgetItem item : budgetItems) {
            if (item.getType() == BudgetItemType.INCOME) {
                balance = balance.add(item.getAmount());
            } else {
                balance = balance.subtract(item.getAmount());
            }
        }
        return balance;
    }
}
