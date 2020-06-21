package budget;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.*;

public class BudgetRepository {

    private List<BudgetItem> budgetItems = new ArrayList<>();

    public void clearBudgetItems() {
        budgetItems.clear();
    }

    public void addIncome(BigDecimal amount) {
        budgetItems.add(new BudgetItem("Income", amount, BudgetItemType.INCOME, null));
    }

    public void addPurchase(String name, BigDecimal amount, PurchaseCategory category) {
        budgetItems.add(new BudgetItem(name, amount, BudgetItemType.PURCHASE, category));
    }

    public void addBudgetItem(BudgetItem budgetItem) {
        budgetItems.add(budgetItem);
    }

    public List<BudgetItem> getBudgetItems() {
        return budgetItems;
    }

    public List<BudgetItem> getPurchases() {
        return budgetItems.stream()
                .filter(item -> item.getType() == BudgetItemType.PURCHASE)
                .collect(toList());
    }

    public List<BudgetItem> getPurchasesSorted() {
        return budgetItems.stream()
                .filter(item -> item.getType() == BudgetItemType.PURCHASE)
                .sorted(Comparator.comparing(BudgetItem::getAmount).reversed())
                .collect(toList());
    }

    public List<BudgetItem> getPurchasesByCategory(PurchaseCategory category) {
        if (category == PurchaseCategory.ALL) {
            return getPurchases();
        }

        return budgetItems.stream()
                .filter(item -> item.getType() == BudgetItemType.PURCHASE)
                .filter(item -> item.getCategory() == category)
                .collect(toList());
    }

    public List<BudgetItem> getPurchasesByCategorySorted(PurchaseCategory category) {
        if (category == PurchaseCategory.ALL) {
            return getPurchasesSorted();
        }

        return budgetItems.stream()
                .filter(item -> item.getType() == BudgetItemType.PURCHASE)
                .filter(item -> item.getCategory() == category)
                .sorted(Comparator.comparing(BudgetItem::getAmount).reversed())
                .collect(toList());
    }

    public long getBudgetItemsCount() {
        return budgetItems.size();
    }

    public long getPurchasesCount() {
        return budgetItems.stream()
                .filter(item -> item.getType() == BudgetItemType.PURCHASE)
                .count();
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

    public BigDecimal getTotalPurchasesByCategory(PurchaseCategory category) {
        if (category == PurchaseCategory.ALL) {
            return getTotalPurchases();
        }

        BigDecimal total = new BigDecimal(0);
        for (BudgetItem item : budgetItems) {
            if (item.getType() == BudgetItemType.PURCHASE && item.getCategory() == category) {
                total = total.add(item.getAmount());
            }
        }
        return total;
    }

    public Map<PurchaseCategory, BigDecimal> getTotalPurchasesWithCategories() {
        return budgetItems.stream()
                .collect(
                        groupingBy(
                                BudgetItem::getCategory,
                                () -> new EnumMap<>(PurchaseCategory.class),
                                reducing(new BigDecimal(0), BudgetItem::getAmount, BigDecimal::add)
                        )
                );
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
