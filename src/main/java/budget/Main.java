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
import java.util.Scanner;

public class Main {

    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        String filename = args.length > 0 ? args[0] : "purchases.txt";

        Scanner scanner = new Scanner(System.in);
        BudgetRepository budgetRepository = new BudgetRepository();

        showMenu();
        int option;
        while ((option = Integer.parseInt(scanner.nextLine())) != 0) {
            System.out.println();
            switch (option) {
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
                default:
                    System.out.println("Incorrect option! Try again." + System.lineSeparator());
                    break;
            }

            showMenu();
        }

        System.out.println(System.lineSeparator() + "Bye!");
    }

    private static void showMenu() {
        System.out.println("Choose your action:");
        System.out.println("1) Add income");
        System.out.println("2) Add purchase");
        System.out.println("3) Show list of purchases");
        System.out.println("4) Balance");
        System.out.println("5) Save");
        System.out.println("6) Load");
        System.out.println("0) Exit");
    }

    private static void showAddPurchaseCategories() {
        System.out.println("Choose the type of purchase:");
        System.out.println("1) Food");
        System.out.println("2) Clothes");
        System.out.println("3) Entertainment");
        System.out.println("4) Other");
        System.out.println("5) Back");
    }

    private static void showSearchPurchaseCategories() {
        System.out.println("Choose the type of purchase:");
        System.out.println("1) Food");
        System.out.println("2) Clothes");
        System.out.println("3) Entertainment");
        System.out.println("4) Other");
        System.out.println("5) All");
        System.out.println("6) Back");
    }

    private static void processAddIncome(BudgetRepository budgetRepository) {
        String income = getUserInput("Enter income: ");
        budgetRepository.addIncome(new BigDecimal(income).setScale(2, RoundingMode.DOWN));
        System.out.println("Income was added!");
        System.out.println();
    }

    private static void processAddPurchase(BudgetRepository budgetRepository) {
        showAddPurchaseCategories();

        int option;
        while ((option = Integer.parseInt(scanner.nextLine())) != 5) {
            System.out.println();
            PurchaseCategory category;
            switch (option) {
                case 1:
                    category = PurchaseCategory.FOOD;
                    break;
                case 2:
                    category = PurchaseCategory.CLOTHES;
                    break;
                case 3:
                    category = PurchaseCategory.ENTERTAINMENT;
                    break;
                case 4:
                    category = PurchaseCategory.OTHER;
                    break;
                default:
                    System.out.println("Incorrect option! Try again." + System.lineSeparator());
                    showAddPurchaseCategories();
                    continue;
            }

            String name = getUserInput("Enter purchase name: ");
            String price = getUserInput("Enter its price: ");
            budgetRepository.addPurchase(name, new BigDecimal(price).setScale(2, RoundingMode.DOWN), category);
            System.out.println("Purchase was added!");
            System.out.println();

            showAddPurchaseCategories();
        }

        System.out.println();
    }

    private static void processGetPurchases(BudgetRepository budgetRepository) {
        if (budgetRepository.getPurchasesCount() == 0) {
            System.out.println("Purchase list is empty!");
            System.out.println();
            return;
        }

        showSearchPurchaseCategories();

        int option;
        while ((option = Integer.parseInt(scanner.nextLine())) != 6) {
            System.out.println();
            PurchaseCategory category;
            switch (option) {
                case 1:
                    category = PurchaseCategory.FOOD;
                    break;
                case 2:
                    category = PurchaseCategory.CLOTHES;
                    break;
                case 3:
                    category = PurchaseCategory.ENTERTAINMENT;
                    break;
                case 4:
                    category = PurchaseCategory.OTHER;
                    break;
                case 5:
                    category = PurchaseCategory.ALL;
                    break;
                default:
                    System.out.println("Incorrect option! Try again." + System.lineSeparator());
                    showSearchPurchaseCategories();
                    continue;
            }

            List<BudgetItem> purchases = budgetRepository.getPurchasesByCategory(category);

            System.out.println(category + ":");
            if (purchases.isEmpty()) {
                System.out.println("Purchase list is empty!");
            } else {
                BigDecimal total = budgetRepository.getTotalPurchasesByCategory(category);
                printPurchaseList(purchases, total);
            }

            System.out.println();
            showSearchPurchaseCategories();
        }

        System.out.println();
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

    private static String getUserInput(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine();
    }

    private static void printPurchaseList(List<BudgetItem> purchases, BigDecimal total) {
        purchases.forEach(System.out::println);
        System.out.printf("Total sum: $%.2f%n", total);
    }
}
