package com.finance_manager.service;

import com.finance_manager.interfaces.ExpenseDAO;
import com.finance_manager.dao.impl.ExpenseDAOImpl;
import com.finance_manager.exceptions.DatabaseOperationException;
import com.finance_manager.exceptions.InvalidAmountException;
import com.finance_manager.model.Expense;

import java.time.LocalDate;
import java.util.List;

public class ExpenseService {
    private final ExpenseDAO expenseDAO;

    public ExpenseService() {
        this.expenseDAO = new ExpenseDAOImpl();
    }

    // for dependency injection/testing
    public ExpenseService(ExpenseDAO expenseDAO) {
        this.expenseDAO = expenseDAO;
    }

    public void addExpense(Expense expense) throws InvalidAmountException, DatabaseOperationException {
        if (expense == null || expense.getAmount() <= 0) {
            throw new InvalidAmountException("Expense amount must be greater than zero.");
        }
        if (expense.getDate() == null) {
            expense.setDate(LocalDate.now());
        }
        expenseDAO.insertExpense(expense);
    }

    public List<Expense> getExpenseByMonth(int year, int month) throws DatabaseOperationException {
        return expenseDAO.fetchByMonth(year, month);
    }

    public List<Expense> getExpenseByDateRange(LocalDate from, LocalDate to) throws DatabaseOperationException {
        return expenseDAO.fetchByDateRange(from, to);
    }
}

