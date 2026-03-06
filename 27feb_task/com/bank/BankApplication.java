package com.bank;

import com.bank.accounts.SavingsAccount;
import com.bank.customers.Customer;
import com.bank.exceptions.InsufficientBalanceException;
import com.bank.loans.Loan;

import static com.bank.util.BankUtil.generateAccountNumber;

public class BankApplication {

    public static void main(String[] args) {

        try {

            // Create Customer
            Customer customer = new Customer(101, "Rahul");

            // Generate account number
            String accNo = generateAccountNumber();

            // Create Savings Account
            SavingsAccount account = new SavingsAccount(accNo, 5000, 5);

            // Link account with customer
            customer.linkAccount(account);

            // Deposit
            account.deposit(2000);

            // Withdrawal
            account.withdraw(1000);

            // Calculate interest
            double interest = account.calculateInterest();
            System.out.println("Interest Earned: " + interest);

            // Create loan
            Loan loan = new Loan(100000, 10, 2);
            double emi = loan.calculateEMI();

            System.out.println("Loan EMI: " + emi);

            // Display details
            customer.displayCustomerDetails();

        }
        catch (InsufficientBalanceException e){
            System.out.println("Error: " + e.getMessage());
        }
        catch (Exception e){
            System.out.println("Exception: " + e.getMessage());
        }

    }

}