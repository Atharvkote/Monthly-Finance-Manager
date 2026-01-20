package com.finance_manager.interfaces;

import com.finance_manager.exceptions.DatabaseOperationException;
import com.finance_manager.model.Expense;

import java.time.LocalDate;
import java.util.List;

public interface ExpenseDAO {
    void insertExpense(Expense expense) throws DatabaseOperationException;
    List<Expense> fetchByMonth(int year, int month) throws DatabaseOperationException;
    List<Expense> fetchByDateRange(LocalDate from, LocalDate to) throws DatabaseOperationException;
    void updateExpense(Expense expense) throws DatabaseOperationException;
    void deleteExpense(int id) throws DatabaseOperationException;
}

