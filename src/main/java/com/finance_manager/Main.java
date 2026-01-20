package com.finance_manager;

import com.finance_manager.dao.SchemaInitializer;
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
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        // Attempt to initialize schema (creates DB and tables if missing) — requires privileges
        try {
            SchemaInitializer.ensureSchema();
            System.out.println("Database schema checked/initialized.");
        } catch (DatabaseOperationException e) {
            System.out.println("Warning: could not initialize database schema: " + e.getMessage());
            if (e.getCause() != null) System.out.println("Cause: " + e.getCause().getMessage());
            System.out.println("Proceeding; if DB/tables do not exist, insert operations may fail.");
        }

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
        System.out.println("4) Generate Monthly Report");
        System.out.println("5) Generate Custom Date Range Report");
        System.out.println("6) Exit");
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
            printReport(report, String.format("Monthly report for %d-%02d", year, month));
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
            printReport(report, String.format("Custom date report %s to %s", from, to));
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
}
