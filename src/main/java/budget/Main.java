package budget;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Main {

    private static final Scanner scanner = new Scanner(System.in);

    private static final MenuRenderer menu = new MenuRenderer();

    public static void main(String[] args) {
        String filename = args.length > 0 ? args[0] : "purchases.txt";

        Scanner scanner = new Scanner(System.in);
        BudgetRepository budgetRepository = new BudgetRepository();

        while (true) {
            menu.showMainMenu();
            int option = Integer.parseInt(scanner.nextLine());
            System.out.println();

            switch (option) {
                case 0:
                    System.out.println("Bye!");
                    return;
                case 1:
                    processAddIncome(budgetRepository);
                    break;
                case 2:
                    processAddPurchase(budgetRepository);
                    break;
                case 3:
                    processGetPurchases(budgetRepository);
                    break;
                case 4:
                    processGetBalance(budgetRepository);
                    break;
                case 5:
                    processSave(budgetRepository, filename);
                    break;
                case 6:
                    processLoad(budgetRepository, filename);
                    break;
                case 7:
                    processAnalyze(budgetRepository);
                    break;
                default:
                    System.out.println("Incorrect option! Try again." + System.lineSeparator());
                    break;
            }
        }
    }

    private static void processAddIncome(BudgetRepository budgetRepository) {
        String income = getUserInput("Enter income: ");
        budgetRepository.addIncome(new BigDecimal(income).setScale(2, RoundingMode.DOWN));
        System.out.println("Income was added!");
        System.out.println();
    }

    private static void processAddPurchase(BudgetRepository budgetRepository) {
        while (true) {
            menu.showAddPurchaseCategories();
            int option = Integer.parseInt(scanner.nextLine());
            System.out.println();

            if (option == 5) {
                return;
            }

            PurchaseCategory category = PurchaseCategory.valueOfCategory(option);
            if (category == null) {
                System.out.println("Incorrect option! Try again." + System.lineSeparator());
                continue;
            }

            String name = getUserInput("Enter purchase name: ");
            String price = getUserInput("Enter its price: ");
            budgetRepository.addPurchase(name, new BigDecimal(price).setScale(2, RoundingMode.DOWN), category);
            System.out.println("Purchase was added!");
            System.out.println();
        }
    }

    private static void processGetPurchases(BudgetRepository budgetRepository) {
        if (budgetRepository.getPurchasesCount() == 0) {
            System.out.println("Purchase list is empty!");
            System.out.println();
            return;
        }

        while (true) {
            menu.showSearchPurchaseCategories();
            int option = Integer.parseInt(scanner.nextLine());
            System.out.println();

            if (option == 6) {
                return;
            }

            PurchaseCategory category = PurchaseCategory.valueOfCategory(option);
            if (category == null) {
                System.out.println("Incorrect option! Try again." + System.lineSeparator());
                continue;
            }

            List<BudgetItem> purchases = budgetRepository.getPurchasesByCategory(category);
            BigDecimal total = budgetRepository.getTotalPurchasesByCategory(category);
            printPurchaseList(category, purchases, total);
        }
    }

    private static void processGetBalance(BudgetRepository budgetRepository) {
        System.out.printf("Balance: $%.2f%n%n", budgetRepository.getTotalBalance());
    }

    private static void processSave(BudgetRepository budgetRepository, String filename) {
        saveBudgetItems(budgetRepository, filename);
        System.out.println("Purchases were saved!" + System.lineSeparator());
    }

    private static void processLoad(BudgetRepository budgetRepository, String filename) {
        loadBudgetItems(budgetRepository, filename);
        System.out.println("Purchases were loaded!" + System.lineSeparator());
    }

    private static void processAnalyze(BudgetRepository budgetRepository) {
        while (true) {
            menu.showSortOptions();
            int option = Integer.parseInt(scanner.nextLine());
            System.out.println();

            if (option == 4) {
                return;
            }

            switch (option) {
                case 1:
                    processSortPurchases(budgetRepository);
                    break;
                case 2:
                    processSummarizePurchases(budgetRepository);
                    break;
                case 3:
                    processSortPurchasesByCategory(budgetRepository);
                    break;
                default:
                    System.out.println("Incorrect option! Try again." + System.lineSeparator());
                    break;
            }
        }
    }

    private static void saveBudgetItems(BudgetRepository budgetRepository, String filename) {
        if (filename != null) {
            try (ObjectOutputStream out = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(filename)))) {
                try {
                    out.writeLong(budgetRepository.getBudgetItemsCount());

                    for (BudgetItem budgetItem : budgetRepository.getBudgetItems()) {
                        out.writeObject(budgetItem);
                    }
                } catch (IOException e) {
                    System.out.println("Error occurred: " + e.getMessage());
                }
            } catch (IOException e) {
                System.out.println("Error occurred when trying to write file: " + filename);
            }
        }
    }

    private static void loadBudgetItems(BudgetRepository budgetRepository, String filename) {
        if (filename != null) {
            File file = new File(filename);

            if (file.exists() && file.isFile()) {
                try (ObjectInputStream in = new ObjectInputStream(new BufferedInputStream(new FileInputStream(file)))) {
                    try {
                        budgetRepository.clearBudgetItems();

                        long n = in.readLong();
                        for (int i = 0; i < n; i++) {
                            BudgetItem budgetItem = (BudgetItem) in.readObject();
                            budgetRepository.addBudgetItem(budgetItem);
                        }
                    } catch (IOException | ClassNotFoundException e) {
                        System.out.println("Error occurred: " + e.getMessage());
                    }
                } catch (IOException e) {
                    System.out.println("Error occurred when trying to read file: " + filename);
                }
            }
        }
    }

    private static void processSortPurchases(BudgetRepository budgetRepository) {
        List<BudgetItem> purchases = budgetRepository.getPurchasesSorted();
        BigDecimal total = budgetRepository.getTotalPurchases();
        printPurchaseList(PurchaseCategory.ALL, purchases, total);
    }

    private static void processSortPurchasesByCategory(BudgetRepository budgetRepository) {
        menu.showSortPurchaseCategories();
        PurchaseCategory category = PurchaseCategory.valueOfCategory(Integer.parseInt(scanner.nextLine()));
        System.out.println();

        if (category == null) {
            return;
        }

        List<BudgetItem> purchases = budgetRepository.getPurchasesByCategorySorted(category);
        BigDecimal total = budgetRepository.getTotalPurchasesByCategory(category);
        printPurchaseList(category, purchases, total);
    }

    private static void processSummarizePurchases(BudgetRepository budgetRepository) {
        Map<PurchaseCategory, BigDecimal> purchasesTotals = budgetRepository.getTotalPurchasesWithCategories();
        purchasesTotals.forEach((category, amount) -> System.out.printf("%s - $%.2f%n", category, amount));
        System.out.printf("Total sum: $%.2f%n", budgetRepository.getTotalPurchases());
        System.out.println();
    }

    private static String getUserInput(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine();
    }

    private static void printPurchaseList(PurchaseCategory category, List<BudgetItem> purchases, BigDecimal total) {
        System.out.println(category + ":");
        if (purchases.isEmpty()) {
            System.out.println("Purchase list is empty!");
        } else {
            purchases.forEach(System.out::println);
            System.out.printf("Total sum: $%.2f%n", total);
        }
        System.out.println();
    }
}
