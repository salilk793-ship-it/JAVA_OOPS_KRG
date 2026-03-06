package com.bank.accounts;

public class SavingsAccount extends Account {

    private double interestRate;

    public SavingsAccount(String accountNumber, double balance, double interestRate) throws Exception {
        super(accountNumber, balance);
        this.interestRate = interestRate;
    }

    public double calculateInterest(){

        double interest = balance * interestRate / 100;
        return interest;

    }

}