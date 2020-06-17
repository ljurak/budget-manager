package budget;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        List<PurchaseItem> purchases = new ArrayList<>();

        while (scanner.hasNext()) {
            String[] input = scanner.nextLine().split(" \\$");
            if (input.length == 2) {
                PurchaseItem purchase = new PurchaseItem(input[0], new BigDecimal(input[1]));
                purchases.add(purchase);
            }
        }

        for (PurchaseItem purchase : purchases) {
            System.out.println(purchase);
        }
        System.out.println();

        BigDecimal total = countTotal(purchases);
        System.out.printf("Total: $%s", total);
    }

    private static BigDecimal countTotal(List<PurchaseItem> purchases) {
        BigDecimal total = new BigDecimal(0);
        for (PurchaseItem purchase : purchases) {
            total = total.add(purchase.getPrice());
        }
        return total;
    }
}
