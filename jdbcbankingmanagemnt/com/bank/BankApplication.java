package com.bank;

import com.bank.dao.BankDAO;
import com.bank.util.BankUtil;
import java.util.Scanner;

public class BankApplication {

    private static final String RESET = "\u001B[0m";
    private static final String CYAN = "\u001B[36m";
    private static final String GREEN = "\u001B[32m";
    private static final String YELLOW = "\u001B[33m";
    private static final String RED = "\u001B[31m";
    private static final String BOLD = "\u001B[1m";

    private static BankDAO bankDAO = new BankDAO();
    private static Scanner scanner = new Scanner(System.in);
    private static int currentUserId = -1;
    private static String currentAccountNumber = null;

    public static void main(String[] args) {
        System.out.println(CYAN + BOLD + "========================================" + RESET);
        System.out.println(CYAN + BOLD + "    WELCOME TO MODERN BANKING SYSTEM    " + RESET);
        System.out.println(CYAN + BOLD + "========================================" + RESET);

        while (true) {
            if (currentUserId == -1) {
                showAuthMenu();
            } else {
                showMainMenu();
            }
        }
    }

    private static void showAuthMenu() {
        System.out.println("\n1. " + GREEN + "Login" + RESET);
        System.out.println("2. " + GREEN + "Register" + RESET);
        System.out.println("3. " + RED + "Exit" + RESET);
        System.out.print("Choose an option: ");

        int choice = scanner.nextInt();
        scanner.nextLine(); // consume newline

        switch (choice) {
            case 1: login(); break;
            case 2: register(); break;
            case 3: System.exit(0);
            default: System.out.println(RED + "Invalid choice!" + RESET);
        }
    }

    private static void login() {
        System.out.print("Username: ");
        String username = scanner.nextLine();
        System.out.print("Password: ");
        String password = scanner.nextLine();

        int id = bankDAO.loginUser(username, password);
        if (id != -1) {
            currentUserId = id;
            currentAccountNumber = bankDAO.getAccountNumberByUserId(id);
            System.out.println(GREEN + "Login successful! Welcome back." + RESET);
        } else {
            System.out.println(RED + "Invalid credentials!" + RESET);
        }
    }

    private static void register() {
        System.out.print("Enter full name: ");
        String name = scanner.nextLine();
        System.out.print("Enter username: ");
        String username = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();

        if (bankDAO.registerUser(username, password, name)) {
            System.out.println(GREEN + "Registration successful!" + RESET);
            int id = bankDAO.loginUser(username, password);
            String accNo = BankUtil.generateAccountNumber();
            if (bankDAO.createAccount(id, accNo, "SAVINGS", 0.0)) {
                System.out.println(YELLOW + "Account created automatically. Acc No: " + accNo + RESET);
            }
        } else {
            System.out.println(RED + "Registration failed. Username might be taken." + RESET);
        }
    }

    private static void showMainMenu() {
        System.out.println("\n--- " + YELLOW + "Dashboard (Acc: " + currentAccountNumber + ")" + RESET + " ---");
        System.out.println("1. Check Balance");
        System.out.println("2. Deposit");
        System.out.println("3. Withdraw");
        System.out.println("4. Transaction History");
        System.out.println("5. Logout");
        System.out.print("Choose an option: ");

        int choice = scanner.nextInt();
        scanner.nextLine();

        switch (choice) {
            case 1: checkBalance(); break;
            case 2: deposit(); break;
            case 3: withdraw(); break;
            case 4: bankDAO.displayTransactionHistory(currentAccountNumber); break;
            case 5:
                currentUserId = -1;
                currentAccountNumber = null;
                System.out.println(YELLOW + "Logged out." + RESET);
                break;
            default: System.out.println(RED + "Invalid choice!" + RESET);
        }
    }

    private static void checkBalance() {
        double balance = bankDAO.getBalance(currentAccountNumber);
        System.out.println(GREEN + "Current Balance: $" + balance + RESET);
    }

    private static void deposit() {
        System.out.print("Enter amount to deposit: ");
        double amount = scanner.nextDouble();
        if (bankDAO.updateBalance(currentAccountNumber, amount, "DEPOSIT")) {
            System.out.println(GREEN + "Deposit successful!" + RESET);
        } else {
            System.out.println(RED + "Deposit failed." + RESET);
        }
    }

    private static void withdraw() {
        System.out.print("Enter amount to withdraw: ");
        double amount = scanner.nextDouble();
        double currentBalance = bankDAO.getBalance(currentAccountNumber);
        if (amount > currentBalance) {
            System.out.println(RED + "Insufficient balance!" + RESET);
            return;
        }
        if (bankDAO.updateBalance(currentAccountNumber, amount, "WITHDRAWAL")) {
            System.out.println(GREEN + "Withdrawal successful!" + RESET);
        } else {
            System.out.println(RED + "Withdrawal failed." + RESET);
        }
    }
}