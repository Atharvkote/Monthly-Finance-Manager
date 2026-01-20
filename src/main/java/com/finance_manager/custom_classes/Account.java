package com.finance_manager.custom_classes;

import com.finance_manager.custom_exceptions.InsufficientFundsException;

public class Account {
    private String name;
    private double balance;
    private double previous_instance_balance;

    public Account(){
        this.name = "";
        this.balance = 0.0;
        this.previous_instance_balance = 0.0;
    }

    public Account(String name, double balance) {
        this.name = name;
        this.balance = balance;
        this.previous_instance_balance = balance;
    }

    public String get_name() {
        return name;
    }

    public void set_name(String name) {
        this.name = name;
    }

    public double get_balance() {
        return balance;
    }

    public void set_balance(double balance) {
        this.balance = balance;
    }

    public double get_previous_instance_balance() {
        return previous_instance_balance;
    }

    public void set_previous_instance_balance(double previous_instance_balance) {
        this.previous_instance_balance = previous_instance_balance;
    }

    protected void reset_previous_instance_balance(){
        this.previous_instance_balance = this.balance;
    }

    protected void update_balance(double amount ,boolean isWithdrawal){
        if(isWithdrawal){
            if(this.balance - amount > 0){
            this.balance -= amount;
            }
        } else {
            this.balance += amount;
        }
    }




}
