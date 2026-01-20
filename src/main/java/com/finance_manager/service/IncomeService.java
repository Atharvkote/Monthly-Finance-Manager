package com.finance_manager.service;

import com.finance_manager.interfaces.IncomeDAO;
import com.finance_manager.dao.impl.IncomeDAOImpl;
import com.finance_manager.exceptions.DatabaseOperationException;
import com.finance_manager.exceptions.InvalidAmountException;
import com.finance_manager.model.Income;

import java.time.LocalDate;
import java.util.List;

public class IncomeService {
    private final IncomeDAO incomeDAO;

    public IncomeService() {
        this.incomeDAO = new IncomeDAOImpl();
    }

    // for dependency injection/testing
    public IncomeService(IncomeDAO incomeDAO) {
        this.incomeDAO = incomeDAO;
    }

    public void addIncome(Income income) throws InvalidAmountException, DatabaseOperationException {
        if (income == null || income.getAmount() <= 0) {
            throw new InvalidAmountException("Income amount must be greater than zero.");
        }
        if (income.getDate() == null) {
            income.setDate(LocalDate.now());
        }
        incomeDAO.insertIncome(income);
    }

    public List<Income> getIncomeByMonth(int year, int month) throws DatabaseOperationException {
        return incomeDAO.fetchByMonth(year, month);
    }

    public List<Income> getIncomeByDateRange(LocalDate from, LocalDate to) throws DatabaseOperationException {
        return incomeDAO.fetchByDateRange(from, to);
    }
}

