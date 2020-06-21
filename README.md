# Budget manager

Console app for managing your home budget. It offers adding incomes and purchases, checking your balance,
showing list of purchases by different categories and analyzing them (sorting and summarizing by category).
You can also save/load your data to/from a file (by default purchases.txt file is used).

## How to start

```bash
git clone https://github.com/ljurak/budget-manager.git
cd budget-manager
mvn clean package
cd target
java -jar budget-manager.jar [file_with_data] (e.g. java -jar budget-manager.jar budget.txt)
```