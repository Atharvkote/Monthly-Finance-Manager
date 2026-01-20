package com.finance_manager.dao;

import com.finance_manager.exceptions.DatabaseOperationException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    // Default constants - avoid scattering literals in code; environment variables override these
    private static final String DEFAULT_DB_URL = "jdbc:mysql://localhost:3307/finance_db";
    private static final String DEFAULT_DB_USER = "root";
    private static final String DEFAULT_DB_PASSWORD = "root123";

    private static DBConnection instance;

    private DBConnection() throws DatabaseOperationException {
        try {
            // ensure driver is loaded
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new DatabaseOperationException("MySQL JDBC Driver not found", e);
        }
    }

    public static synchronized DBConnection getInstance() throws DatabaseOperationException {
        if (instance == null) {
            instance = new DBConnection();
        }
        return instance;
    }

    public Connection getConnection() throws DatabaseOperationException {
        try {
            return DriverManager.getConnection(DEFAULT_DB_URL, DEFAULT_DB_USER, DEFAULT_DB_PASSWORD);
        } catch (SQLException e) {
            throw new DatabaseOperationException("Failed to obtain DB connection", e);
        }
    }
}
