package com.finance_manager.dao;

import com.finance_manager.exceptions.DatabaseOperationException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class SchemaInitializer {
    // Keep defaults aligned with DBConnection
    private static final String DEFAULT_DB_URL = "jdbc:mysql://localhost:3306/finance_db?useSSL=false&serverTimezone=UTC";
    private static final String DEFAULT_DB_USER = "root";
    private static final String DEFAULT_DB_PASSWORD = "";
    private static final String TARGET_DB = "finance_db";

    public static void ensureSchema() throws DatabaseOperationException {
        String url = System.getenv("FINANCE_DB_URL");
        String user = System.getenv("FINANCE_DB_USER");
        String pass = System.getenv("FINANCE_DB_PASSWORD");

        if (url == null || url.isBlank()) url = DEFAULT_DB_URL;
        if (user == null) user = DEFAULT_DB_USER;
        if (pass == null) pass = DEFAULT_DB_PASSWORD;

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new DatabaseOperationException("MySQL JDBC Driver not found", e);
        }

        // Build a server-level URL (no database) so we can create the target database if missing
        String serverUrl = buildServerUrl(url);

        try (Connection conn = DriverManager.getConnection(serverUrl, user, pass);
             Statement stmt = conn.createStatement()) {
            stmt.executeUpdate("CREATE DATABASE IF NOT EXISTS " + TARGET_DB);
        } catch (SQLException e) {
            // If we couldn't connect to server or create DB, bubble up with detail
            throw new DatabaseOperationException("Failed to create or access database: " + e.getMessage(), e);
        }

        // Now create tables if not exist
        String dbUrl = ensureDbInUrl(url);
        try (Connection conn = DriverManager.getConnection(dbUrl, user, pass);
             Statement stmt = conn.createStatement()) {
            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS income (\n" +
                    "  id INT PRIMARY KEY AUTO_INCREMENT,\n" +
                    "  amount DOUBLE NOT NULL,\n" +
                    "  source VARCHAR(50),\n" +
                    "  description VARCHAR(100),\n" +
                    "  date DATE NOT NULL\n" +
                    ");");

            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS expense (\n" +
                    "  id INT PRIMARY KEY AUTO_INCREMENT,\n" +
                    "  amount DOUBLE NOT NULL,\n" +
                    "  category VARCHAR(50),\n" +
                    "  description VARCHAR(100),\n" +
                    "  date DATE NOT NULL\n" +
                    ");");
        } catch (SQLException e) {
            throw new DatabaseOperationException("Failed to create tables: " + e.getMessage(), e);
        }
    }

    private static String buildServerUrl(String url) {
        // url expected like: jdbc:mysql://host:port[/database][?params]
        String prefix = "jdbc:mysql://";
        if (!url.startsWith(prefix)) return url;
        int start = prefix.length();
        int slashIndex = url.indexOf('/', start);
        if (slashIndex == -1) {
            // no path, append params
            return url;
        }
        String hostPort = url.substring(start, slashIndex);
        return "jdbc:mysql://" + hostPort + "/?useSSL=false&serverTimezone=UTC";
    }

    private static String ensureDbInUrl(String url) {
        // ensure the URL points to the TARGET_DB
        if (url.contains("/" + TARGET_DB)) {
            return url;
        }
        // If URL has jdbc:mysql://host:port/ and possibly params, replace path with TARGET_DB
        String prefix = "jdbc:mysql://";
        int start = prefix.length();
        int slashIndex = url.indexOf('/', start);
        if (slashIndex == -1) {
            // no path, append db
            return url + "/" + TARGET_DB + "?useSSL=false&serverTimezone=UTC";
        }
        // find beginning of params
        int paramsIndex = url.indexOf('?', slashIndex);
        String hostPort = url.substring(start, slashIndex);
        String params = paramsIndex == -1 ? "" : url.substring(paramsIndex);
        return "jdbc:mysql://" + hostPort + "/" + TARGET_DB + params;
    }
}

