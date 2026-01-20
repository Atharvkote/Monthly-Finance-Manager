package com.finance_manager.service;

import com.finance_manager.dao.impl.ExpenseDAO;
import com.finance_manager.exceptions.DatabaseOperationException;
import com.finance_manager.exceptions.InvalidAmountException;
import com.finance_manager.model.Expense;

import java.time.LocalDate;
import java.util.List;

public class ExpenseService {
    private final com.finance_manager.interfaces.ExpenseDAO expenseDAO;

    public ExpenseService() {
        this.expenseDAO = new ExpenseDAO();
    }

    public void addExpense(Expense expense) throws InvalidAmountException, DatabaseOperationException {
        validateExpense(expense);
        expenseDAO.insertExpense(expense);
    }

    public List<Expense> getExpenseByMonth(int year, int month) throws DatabaseOperationException {
        return expenseDAO.fetchByMonth(year, month);
    }

    public List<Expense> getExpenseByDateRange(LocalDate from, LocalDate to) throws DatabaseOperationException {
        return expenseDAO.fetchByDateRange(from, to);
    }

    public void updateExpense(Expense expense) throws InvalidAmountException, DatabaseOperationException {
        validateExpense(expense);
        expenseDAO.updateExpense(expense);
    }

    public void deleteExpense(int id) throws DatabaseOperationException {
        expenseDAO.deleteExpense(id);
    }

    private void validateExpense(Expense expense) throws InvalidAmountException {
        if (expense == null || expense.getAmount() <= 0) {
            throw new InvalidAmountException("Expense amount must be greater than zero.");
        }
        if (expense.getDate() == null) {
            expense.setDate(LocalDate.now());
        }
    }
}

