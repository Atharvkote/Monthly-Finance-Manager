-- Schema for finance_db
CREATE DATABASE IF NOT EXISTS finance_db;
USE finance_db;

CREATE TABLE IF NOT EXISTS income (
  id INT PRIMARY KEY AUTO_INCREMENT,
  amount DOUBLE NOT NULL,
  source VARCHAR(50),
  description VARCHAR(100),
  date DATE NOT NULL
);

CREATE TABLE IF NOT EXISTS expense (
  id INT PRIMARY KEY AUTO_INCREMENT,
  amount DOUBLE NOT NULL,
  category VARCHAR(50),
  description VARCHAR(100),
  date DATE NOT NULL
);

