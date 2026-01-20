package com.finance_manager.interfaces;

import com.finance_manager.exceptions.DatabaseOperationException;
import com.finance_manager.model.Income;

import java.time.LocalDate;
import java.util.List;

public interface IncomeDAO {
    void insertIncome(Income income) throws DatabaseOperationException;
    List<Income> fetchByMonth(int year, int month) throws DatabaseOperationException;
    List<Income> fetchByDateRange(LocalDate from, LocalDate to) throws DatabaseOperationException;
    void updateIncome(Income income) throws DatabaseOperationException;
    void deleteIncome(int id) throws DatabaseOperationException;
}

