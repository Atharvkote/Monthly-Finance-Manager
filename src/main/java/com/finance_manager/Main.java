package com.finance_manager;

import com.finance_manager.exceptions.DatabaseOperationException;
import com.finance_manager.exceptions.InvalidAmountException;
import com.finance_manager.model.Expense;
import com.finance_manager.model.Income;
import com.finance_manager.model.MonthlyReport;
import com.finance_manager.service.ExpenseService;
import com.finance_manager.service.IncomeService;
import com.finance_manager.service.ReportService;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        IncomeService incomeService = new IncomeService();
        ExpenseService expenseService = new ExpenseService();
        ReportService reportService = new ReportService(incomeService, expenseService);

        Scanner scanner = new Scanner(System.in);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        boolean running = true;
        while (running) {
            printMenu();
            System.out.print("Select an option: ");
            String input = scanner.nextLine().trim();
            switch (input) {
                case "1":
                    handleAddIncome(scanner, incomeService, formatter);
                    break;
                case "2":
                    handleAddExpense(scanner, expenseService, formatter);
                    break;
                case "3":
                    handleViewMonthly(incomeService, expenseService, reportService, scanner);
                    break;
                case "4":
                    handleMonthlyReport(reportService, scanner);
                    break;
                case "5":
                    handleCustomReport(reportService, scanner, formatter);
                    break;
                case "6":
                handleUpdateIncome(scanner, incomeService, formatter);
                break;
            case "7":
                handleDeleteIncome(scanner, incomeService);
                break;
            case "8":
                handleUpdateExpense(scanner, expenseService, formatter);
                break;
            case "9":
                handleDeleteExpense(scanner, expenseService);
                break;
            case "0":
                    running = false;
                    System.out.println("Exiting. Goodbye!");
                    break;
                default:
                    System.out.println("Invalid option. Try again.");
            }
            System.out.println();
        }

        scanner.close();
    }

    private static void printMenu() {
        System.out.println("=== Monthly Finance Manager ===");
        System.out.println("1) Add Income");
        System.out.println("2) Add Expense");
        System.out.println("3) View Monthly Income vs Expense (quick)");
        System.out.println("4) Generate Monthly Report (detailed history)");
        System.out.println("5) Generate Custom Date Range Report (detailed history)");
        System.out.println("6) Update Income");
        System.out.println("7) Delete Income");
        System.out.println("8) Update Expense");
        System.out.println("9) Delete Expense");
        System.out.println("0) Exit");
    }

    private static void handleAddIncome(Scanner scanner, IncomeService incomeService, DateTimeFormatter formatter) {
        try {
            System.out.print("Amount: ");
            double amount = Double.parseDouble(scanner.nextLine().trim());
            System.out.print("Source: ");
            String source = scanner.nextLine().trim();
            System.out.print("Description: ");
            String desc = scanner.nextLine().trim();
            System.out.print("Date (yyyy-MM-dd) [leave empty for today]: ");
            String dateStr = scanner.nextLine().trim();
            LocalDate date = dateStr.isEmpty() ? LocalDate.now() : LocalDate.parse(dateStr, formatter);

            Income income = new Income(amount, source, desc, date);
            incomeService.addIncome(income);
            System.out.println("Income recorded successfully.");
        } catch (NumberFormatException e) {
            System.out.println("Invalid amount. Please enter a numeric value.");
        } catch (DateTimeParseException e) {
            System.out.println("Invalid date format. Use yyyy-MM-dd.");
        } catch (InvalidAmountException e) {
            System.out.println("Error: " + e.getMessage());
        } catch (DatabaseOperationException e) {
            System.out.println("Error: " + e.getMessage());
            if (e.getCause() != null) {
                System.out.println("Cause: " + e.getCause().getMessage());
            }
        }
    }

    private static void handleAddExpense(Scanner scanner, ExpenseService expenseService, DateTimeFormatter formatter) {
        try {
            System.out.print("Amount: ");
            double amount = Double.parseDouble(scanner.nextLine().trim());
            System.out.print("Category: ");
            String category = scanner.nextLine().trim();
            System.out.print("Description: ");
            String desc = scanner.nextLine().trim();
            System.out.print("Date (yyyy-MM-dd) [leave empty for today]: ");
            String dateStr = scanner.nextLine().trim();
            LocalDate date = dateStr.isEmpty() ? LocalDate.now() : LocalDate.parse(dateStr, formatter);

            Expense expense = new Expense(amount, category, desc, date);
            expenseService.addExpense(expense);
            System.out.println("Expense recorded successfully.");
        } catch (NumberFormatException e) {
            System.out.println("Invalid amount. Please enter a numeric value.");
        } catch (DateTimeParseException e) {
            System.out.println("Invalid date format. Use yyyy-MM-dd.");
        } catch (InvalidAmountException e) {
            System.out.println("Error: " + e.getMessage());
        } catch (DatabaseOperationException e) {
            System.out.println("Error: " + e.getMessage());
            if (e.getCause() != null) {
                System.out.println("Cause: " + e.getCause().getMessage());
            }
        }
    }

    private static void handleViewMonthly(IncomeService incomeService, ExpenseService expenseService, ReportService reportService, Scanner scanner) {
        try {
            System.out.print("Enter year (e.g., 2026): ");
            int year = Integer.parseInt(scanner.nextLine().trim());
            System.out.print("Enter month (1-12): ");
            int month = Integer.parseInt(scanner.nextLine().trim());

            MonthlyReport report = reportService.generateMonthlyReport(year, month);
            System.out.println("--- Quick Monthly View ---");
            System.out.printf("Total Income: %.2f\n", report.getTotalIncome());
            System.out.printf("Total Expense: %.2f\n", report.getTotalExpense());
            System.out.printf("Savings: %.2f\n", report.getSavings());
        } catch (NumberFormatException e) {
            System.out.println("Invalid number input.");
        } catch (DatabaseOperationException e) {
            System.out.println("DB Error: " + e.getMessage());
            if (e.getCause() != null) {
                System.out.println("Cause: " + e.getCause().getMessage());
            }
        }
    }

    private static void handleMonthlyReport(ReportService reportService, Scanner scanner) {
        try {
            System.out.print("Enter year (e.g., 2026): ");
            int year = Integer.parseInt(scanner.nextLine().trim());
            System.out.print("Enter month (1-12): ");
            int month = Integer.parseInt(scanner.nextLine().trim());

            MonthlyReport report = reportService.generateMonthlyReport(year, month);
            System.out.println("--- Monthly report for " + year + "-" + String.format("%02d", month) + " ---");
            printDetailedHistory(reportService, year, month);
            printReport(report, "Summary");
        } catch (NumberFormatException e) {
            System.out.println("Invalid number input.");
        } catch (DatabaseOperationException e) {
            System.out.println("DB Error: " + e.getMessage());
            if (e.getCause() != null) {
                System.out.println("Cause: " + e.getCause().getMessage());
            }
        }
    }

    private static void handleCustomReport(ReportService reportService, Scanner scanner, DateTimeFormatter formatter) {
        try {
            System.out.print("From date (yyyy-MM-dd): ");
            LocalDate from = LocalDate.parse(scanner.nextLine().trim(), formatter);
            System.out.print("To date (yyyy-MM-dd): ");
            LocalDate to = LocalDate.parse(scanner.nextLine().trim(), formatter);

            MonthlyReport report = reportService.generateCustomDateReport(from, to);
            System.out.println("--- Custom date report " + from + " to " + to + " ---");
            printDetailedHistory(reportService, from, to);
            printReport(report, "Summary");
        } catch (DateTimeParseException e) {
            System.out.println("Invalid date format. Use yyyy-MM-dd.");
        } catch (DatabaseOperationException e) {
            System.out.println("DB Error: " + e.getMessage());
            if (e.getCause() != null) {
                System.out.println("Cause: " + e.getCause().getMessage());
            }
        }
    }

    private static void printReport(MonthlyReport report, String title) {
        System.out.println("--- " + title + " ---");
        System.out.printf("Total Income : %.2f\n", report.getTotalIncome());
        System.out.printf("Total Expense: %.2f\n", report.getTotalExpense());
        System.out.printf("Savings      : %.2f\n", report.getSavings());
    }

    private static void printDetailedHistory(ReportService reportService, int year, int month) throws DatabaseOperationException {
        List<Income> incomes = reportService.getIncomeService().getIncomeByMonth(year, month);
        List<Expense> expenses = reportService.getExpenseService().getExpenseByMonth(year, month);
        printTransactionHistory(incomes, expenses);
    }

    private static void printDetailedHistory(ReportService reportService, LocalDate from, LocalDate to) throws DatabaseOperationException {
        List<Income> incomes = reportService.getIncomeService().getIncomeByDateRange(from, to);
        List<Expense> expenses = reportService.getExpenseService().getExpenseByDateRange(from, to);
        printTransactionHistory(incomes, expenses);
    }

    private static void printTransactionHistory(List<Income> incomes, List<Expense> expenses) {
        System.out.println("--- Detailed income / expense history ---");
        record Movement(int Id ,LocalDate date, String type, String description, double change, double balance) {}

        // Build a single list of movements
        List<Movement> movements = new java.util.ArrayList<>();
        for (Income i : incomes) {
            movements.add(new Movement(i.getId(), i.getDate(), "INCOME", i.getSource() + " - " + i.getDescription(), i.getAmount(), 0));
        }
        for (Expense e : expenses) {
            movements.add(new Movement(e.getId() , e.getDate(), "EXPENSE", e.getCategory() + " - " + e.getDescription(), -e.getAmount(), 0));
        }

        // Sort by date
        movements.sort(Comparator.comparing(Movement::date));

        if (movements.isEmpty()) {
            System.out.println("No transactions in this period.");
            return;
        }

        System.out.printf(
                "%-6s %-12s %-12s %-30s %12s %12s%n",
                "ID", "Date", "Type", "Description", "Change", "Balance"
        );

        double balance = 0.0;

        for (Movement m : movements) {
            balance += m.change();
            System.out.printf(
                    "%-6d %-12s %-12s %-30s %12.2f %12.2f%n",
                    m.Id(),
                    m.date(),
                    m.type(),
                    m.description(),
                    m.change(),
                    balance
            );
        }
    }

    private static void handleUpdateIncome(Scanner scanner, IncomeService incomeService, DateTimeFormatter formatter) {
        try {
            System.out.print("Enter income ID to update: ");
            int id = Integer.parseInt(scanner.nextLine().trim());

            System.out.print("New amount: ");
            double amount = Double.parseDouble(scanner.nextLine().trim());
            System.out.print("New source: ");
            String source = scanner.nextLine().trim();
            System.out.print("New description: ");
            String desc = scanner.nextLine().trim();
            System.out.print("New date (yyyy-MM-dd): ");
            LocalDate date = LocalDate.parse(scanner.nextLine().trim(), formatter);

            Income income = new Income(id, amount, source, desc, date);
            incomeService.updateIncome(income);
            System.out.println("Income updated successfully.");
        } catch (NumberFormatException e) {
            System.out.println("Invalid number input.");
        } catch (DateTimeParseException e) {
            System.out.println("Invalid date format. Use yyyy-MM-dd.");
        } catch (InvalidAmountException | DatabaseOperationException e) {
            System.out.println("Error: " + e.getMessage());
            if (e.getCause() != null) {
                System.out.println("Cause: " + e.getCause().getMessage());
            }
        }
    }

    private static void handleDeleteIncome(Scanner scanner, IncomeService incomeService) {
        try {
            System.out.print("Enter income ID to delete: ");
            int id = Integer.parseInt(scanner.nextLine().trim());
            incomeService.deleteIncome(id);
            System.out.println("Income deleted successfully.");
        } catch (NumberFormatException e) {
            System.out.println("Invalid number input.");
        } catch (DatabaseOperationException e) {
            System.out.println("Error: " + e.getMessage());
            if (e.getCause() != null) {
                System.out.println("Cause: " + e.getCause().getMessage());
            }
        }
    }

    private static void handleUpdateExpense(Scanner scanner, ExpenseService expenseService, DateTimeFormatter formatter) {
        try {
            System.out.print("Enter expense ID to update: ");
            int id = Integer.parseInt(scanner.nextLine().trim());

            System.out.print("New amount: ");
            double amount = Double.parseDouble(scanner.nextLine().trim());
            System.out.print("New category: ");
            String category = scanner.nextLine().trim();
            System.out.print("New description: ");
            String desc = scanner.nextLine().trim();
            System.out.print("New date (yyyy-MM-dd): ");
            LocalDate date = LocalDate.parse(scanner.nextLine().trim(), formatter);

            Expense expense = new Expense(id, amount, category, desc, date);
            expenseService.updateExpense(expense);
            System.out.println("Expense updated successfully.");
        } catch (NumberFormatException e) {
            System.out.println("Invalid number input.");
        } catch (DateTimeParseException e) {
            System.out.println("Invalid date format. Use yyyy-MM-dd.");
        } catch (InvalidAmountException | DatabaseOperationException e) {
            System.out.println("Error: " + e.getMessage());
            if (e.getCause() != null) {
                System.out.println("Cause: " + e.getCause().getMessage());
            }
        }
    }

    private static void handleDeleteExpense(Scanner scanner, ExpenseService expenseService) {
        try {
            System.out.print("Enter expense ID to delete: ");
            int id = Integer.parseInt(scanner.nextLine().trim());
            expenseService.deleteExpense(id);
            System.out.println("Expense deleted successfully.");
        } catch (NumberFormatException e) {
            System.out.println("Invalid number input.");
        } catch (DatabaseOperationException e) {
            System.out.println("Error: " + e.getMessage());
            if (e.getCause() != null) {
                System.out.println("Cause: " + e.getCause().getMessage());
            }
        }
    }
}
