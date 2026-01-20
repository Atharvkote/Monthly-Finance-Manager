package com.finance_manager.model;

import java.time.LocalDate;

public class Income {
    private int id;
    private double amount;
    private String source;
    private String description;
    private LocalDate date;

    public Income() {
    }

    public Income(double amount, String source, String description, LocalDate date) {
        this.amount = amount;
        this.source = source;
        this.description = description;
        this.date = date;
    }

    public Income(int id, double amount, String source, String description, LocalDate date) {
        this.id = id;
        this.amount = amount;
        this.source = source;
        this.description = description;
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "Income{" +
                "id=" + id +
                ", amount=" + amount +
                ", source='" + source + '\'' +
                ", description='" + description + '\'' +
                ", date=" + date +
                '}';
    }
}

