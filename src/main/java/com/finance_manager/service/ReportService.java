package com.finance_manager.service;

import com.finance_manager.exceptions.DatabaseOperationException;
import com.finance_manager.model.Income;
import com.finance_manager.model.Expense;
import com.finance_manager.model.MonthlyReport;

import java.time.LocalDate;
import java.util.List;

public class ReportService {
    private final IncomeService incomeService;
    private final ExpenseService expenseService;

    public ReportService(IncomeService incomeService, ExpenseService expenseService) {
        this.incomeService = incomeService;
        this.expenseService = expenseService;
    }

    public MonthlyReport generateMonthlyReport(int year, int month) throws DatabaseOperationException {
        List<Income> incomes = incomeService.getIncomeByMonth(year, month);
        List<Expense> expenses = expenseService.getExpenseByMonth(year, month);

        double totalIncome = incomes.stream().mapToDouble(Income::getAmount).sum();
        double totalExpense = expenses.stream().mapToDouble(Expense::getAmount).sum();

        return new MonthlyReport(totalIncome, totalExpense);
    }

    public MonthlyReport generateCustomDateReport(LocalDate from, LocalDate to) throws DatabaseOperationException {
        if (from.isAfter(to)) {
            // swap to ensure correct range
            LocalDate tmp = from;
            from = to;
            to = tmp;
        }
        List<Income> incomes = incomeService.getIncomeByDateRange(from, to);
        List<Expense> expenses = expenseService.getExpenseByDateRange(from, to);

        double totalIncome = incomes.stream().mapToDouble(Income::getAmount).sum();
        double totalExpense = expenses.stream().mapToDouble(Expense::getAmount).sum();

        return new MonthlyReport(totalIncome, totalExpense);
    }

    public IncomeService getIncomeService() {
        return incomeService;
    }

    public ExpenseService getExpenseService() {
        return expenseService;
    }
}

