package com.finance_manager.dao.impl;

import com.finance_manager.dao.DBConnection;
import com.finance_manager.interfaces.IncomeDAO;
import com.finance_manager.exceptions.DatabaseOperationException;
import com.finance_manager.model.Income;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class IncomeDAOImpl implements IncomeDAO {

    private static final String INSERT_SQL = "INSERT INTO income (amount, source, description, date) VALUES (?, ?, ?, ?)";
    private static final String FETCH_BY_MONTH_SQL = "SELECT id, amount, source, description, date FROM income WHERE YEAR(date) = ? AND MONTH(date) = ?";
    private static final String FETCH_BY_RANGE_SQL = "SELECT id, amount, source, description, date FROM income WHERE date BETWEEN ? AND ?";
    private static final String UPDATE_SQL = "UPDATE income SET amount = ?, source = ?, description = ?, date = ? WHERE id = ?";
    private static final String DELETE_SQL = "DELETE FROM income WHERE id = ?";

    @Override
    public void insertIncome(Income income) throws DatabaseOperationException {
        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(INSERT_SQL, Statement.RETURN_GENERATED_KEYS)) {
            ps.setDouble(1, income.getAmount());
            ps.setString(2, income.getSource());
            ps.setString(3, income.getDescription());
            ps.setDate(4, Date.valueOf(income.getDate()));

            int affected = ps.executeUpdate();
            if (affected == 0) {
                throw new DatabaseOperationException("Inserting income failed, no rows affected.");
            }
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    income.setId(rs.getInt(1));
                }
            }
        } catch (SQLException e) {
            throw new DatabaseOperationException("Error inserting income: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Income> fetchByMonth(int year, int month) throws DatabaseOperationException {
        List<Income> list = new ArrayList<>();
        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(FETCH_BY_MONTH_SQL)) {
            ps.setInt(1, year);
            ps.setInt(2, month);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Income income = new Income(rs.getInt("id"), rs.getDouble("amount"), rs.getString("source"), rs.getString("description"), rs.getDate("date").toLocalDate());
                    list.add(income);
                }
            }
        } catch (SQLException e) {
            throw new DatabaseOperationException("Error fetching income by month: " + e.getMessage(), e);
        }
        return list;
    }

    @Override
    public List<Income> fetchByDateRange(LocalDate from, LocalDate to) throws DatabaseOperationException {
        List<Income> list = new ArrayList<>();
        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(FETCH_BY_RANGE_SQL)) {
            ps.setDate(1, Date.valueOf(from));
            ps.setDate(2, Date.valueOf(to));
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Income income = new Income(rs.getInt("id"), rs.getDouble("amount"), rs.getString("source"), rs.getString("description"), rs.getDate("date").toLocalDate());
                    list.add(income);
                }
            }
        } catch (SQLException e) {
            throw new DatabaseOperationException("Error fetching income by date range: " + e.getMessage(), e);
        }
        return list;
    }

    @Override
    public void updateIncome(Income income) throws DatabaseOperationException {
        if (income == null || income.getId() <= 0) {
            throw new DatabaseOperationException("Income id must be provided for update.");
        }

        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(UPDATE_SQL)) {
            ps.setDouble(1, income.getAmount());
            ps.setString(2, income.getSource());
            ps.setString(3, income.getDescription());
            ps.setDate(4, Date.valueOf(income.getDate()));
            ps.setInt(5, income.getId());

            int affected = ps.executeUpdate();
            if (affected == 0) {
                throw new DatabaseOperationException("Updating income failed, no rows affected.");
            }
        } catch (SQLException e) {
            throw new DatabaseOperationException("Error updating income: " + e.getMessage(), e);
        }
    }

    @Override
    public void deleteIncome(int id) throws DatabaseOperationException {
        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(DELETE_SQL)) {
            ps.setInt(1, id);
            int affected = ps.executeUpdate();
            if (affected == 0) {
                throw new DatabaseOperationException("Deleting income failed, no rows affected (id may not exist).");
            }
        } catch (SQLException e) {
            throw new DatabaseOperationException("Error deleting income: " + e.getMessage(), e);
        }
    }
}
