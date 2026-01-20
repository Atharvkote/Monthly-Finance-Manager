package com.finance_manager.model;

public class MonthlyReport {
    private double totalIncome;
    private double totalExpense;
    private double savings;

    public MonthlyReport() {}

    public MonthlyReport(double totalIncome, double totalExpense) {
        this.totalIncome = totalIncome;
        this.totalExpense = totalExpense;
        this.savings = totalIncome - totalExpense;
    }

    public double getTotalIncome() {
        return totalIncome;
    }

    public void setTotalIncome(double totalIncome) {
        this.totalIncome = totalIncome;
    }

    public double getTotalExpense() {
        return totalExpense;
    }

    public void setTotalExpense(double totalExpense) {
        this.totalExpense = totalExpense;
    }

    public double getSavings() {
        return savings;
    }

    public void setSavings(double savings) {
        this.savings = savings;
    }

    @Override
    public String toString() {
        return String.format("MonthlyReport{totalIncome=%.2f, totalExpense=%.2f, savings=%.2f}", totalIncome, totalExpense, savings);
    }
}

