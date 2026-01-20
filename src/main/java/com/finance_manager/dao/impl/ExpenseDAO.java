package com.finance_manager.dao.impl;

import com.finance_manager.dao.DBConnection;
import com.finance_manager.interfaces.ExpenseDAO;
import com.finance_manager.exceptions.DatabaseOperationException;
import com.finance_manager.model.Expense;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ExpenseDAOImpl implements ExpenseDAO {

    private static final String INSERT_SQL = "INSERT INTO expense (amount, category, description, date) VALUES (?, ?, ?, ?)";
    private static final String FETCH_BY_MONTH_SQL = "SELECT id, amount, category, description, date FROM expense WHERE YEAR(date) = ? AND MONTH(date) = ?";
    private static final String FETCH_BY_RANGE_SQL = "SELECT id, amount, category, description, date FROM expense WHERE date BETWEEN ? AND ?";
    private static final String UPDATE_SQL = "UPDATE expense SET amount = ?, category = ?, description = ?, date = ? WHERE id = ?";
    private static final String DELETE_SQL = "DELETE FROM expense WHERE id = ?";

    @Override
    public void insertExpense(Expense expense) throws DatabaseOperationException {
        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(INSERT_SQL, Statement.RETURN_GENERATED_KEYS)) {
            ps.setDouble(1, expense.getAmount());
            ps.setString(2, expense.getCategory());
            ps.setString(3, expense.getDescription());
            ps.setDate(4, Date.valueOf(expense.getDate()));

            int affected = ps.executeUpdate();
            if (affected == 0) {
                throw new DatabaseOperationException("Inserting expense failed, no rows affected.");
            }
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    expense.setId(rs.getInt(1));
                }
            }
        } catch (SQLException e) {
            throw new DatabaseOperationException("Error inserting expense: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Expense> fetchByMonth(int year, int month) throws DatabaseOperationException {
        List<Expense> list = new ArrayList<>();
        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(FETCH_BY_MONTH_SQL)) {
            ps.setInt(1, year);
            ps.setInt(2, month);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Expense expense = new Expense(rs.getInt("id"), rs.getDouble("amount"), rs.getString("category"), rs.getString("description"), rs.getDate("date").toLocalDate());
                    list.add(expense);
                }
            }
        } catch (SQLException e) {
            throw new DatabaseOperationException("Error fetching expense by month: " + e.getMessage(), e);
        }
        return list;
    }

    @Override
    public List<Expense> fetchByDateRange(LocalDate from, LocalDate to) throws DatabaseOperationException {
        List<Expense> list = new ArrayList<>();
        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(FETCH_BY_RANGE_SQL)) {
            ps.setDate(1, Date.valueOf(from));
            ps.setDate(2, Date.valueOf(to));
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Expense expense = new Expense(rs.getInt("id"), rs.getDouble("amount"), rs.getString("category"), rs.getString("description"), rs.getDate("date").toLocalDate());
                    list.add(expense);
                }
            }
        } catch (SQLException e) {
            throw new DatabaseOperationException("Error fetching expense by date range: " + e.getMessage(), e);
        }
        return list;
    }

    @Override
    public void updateExpense(Expense expense) throws DatabaseOperationException {
        if (expense == null || expense.getId() <= 0) {
            throw new DatabaseOperationException("Expense id must be provided for update.");
        }

        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(UPDATE_SQL)) {
            ps.setDouble(1, expense.getAmount());
            ps.setString(2, expense.getCategory());
            ps.setString(3, expense.getDescription());
            ps.setDate(4, Date.valueOf(expense.getDate()));
            ps.setInt(5, expense.getId());

            int affected = ps.executeUpdate();
            if (affected == 0) {
                throw new DatabaseOperationException("Updating expense failed, no rows affected.");
            }
        } catch (SQLException e) {
            throw new DatabaseOperationException("Error updating expense: " + e.getMessage(), e);
        }
    }

    @Override
    public void deleteExpense(int id) throws DatabaseOperationException {
        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(DELETE_SQL)) {
            ps.setInt(1, id);
            int affected = ps.executeUpdate();
            if (affected == 0) {
                throw new DatabaseOperationException("Deleting expense failed, no rows affected (id may not exist).");
            }
        } catch (SQLException e) {
            throw new DatabaseOperationException("Error deleting expense: " + e.getMessage(), e);
        }
    }
}
