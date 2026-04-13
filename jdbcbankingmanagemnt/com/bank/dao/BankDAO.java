package com.bank.dao;

import com.bank.util.DBConnection;
import java.sql.*;

public class BankDAO {

    public boolean registerUser(String username, String password, String fullName) {
        String query = "INSERT INTO users (username, password, full_name) VALUES (?, ?, ?)";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement pst = con.prepareStatement(query)) {
            pst.setString(1, username);
            pst.setString(2, password);
            pst.setString(3, fullName);
            return pst.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public int loginUser(String username, String password) {
        String query = "SELECT id FROM users WHERE username = ? AND password = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement pst = con.prepareStatement(query)) {
            pst.setString(1, username);
            pst.setString(2, password);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                return rs.getInt("id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public boolean createAccount(int userId, String accountNumber, String accountType, double initialBalance) {
        String query = "INSERT INTO accounts (account_number, user_id, balance, account_type) VALUES (?, ?, ?, ?)";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement pst = con.prepareStatement(query)) {
            pst.setString(1, accountNumber);
            pst.setInt(2, userId);
            pst.setDouble(3, initialBalance);
            pst.setString(4, accountType);
            return pst.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public double getBalance(String accountNumber) {
        String query = "SELECT balance FROM accounts WHERE account_number = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement pst = con.prepareStatement(query)) {
            pst.setString(1, accountNumber);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                return rs.getDouble("balance");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public boolean updateBalance(String accountNumber, double amount, String type) {
        String updateQuery = "UPDATE accounts SET balance = balance + ? WHERE account_number = ?";
        String transQuery = "INSERT INTO transactions (account_number, amount, transaction_type) VALUES (?, ?, ?)";
        
        Connection con = null;
        try {
            con = DBConnection.getConnection();
            con.setAutoCommit(false);

            // Update balance
            PreparedStatement pstUpdate = con.prepareStatement(updateQuery);
            pstUpdate.setDouble(1, type.equalsIgnoreCase("DEPOSIT") ? amount : -amount);
            pstUpdate.setString(2, accountNumber);
            pstUpdate.executeUpdate();

            // Record transaction
            PreparedStatement pstTrans = con.prepareStatement(transQuery);
            pstTrans.setString(1, accountNumber);
            pstTrans.setDouble(2, amount);
            pstTrans.setString(3, type.toUpperCase());
            pstTrans.executeUpdate();

            con.commit();
            return true;
        } catch (SQLException e) {
            if (con != null) {
                try { con.rollback(); } catch (SQLException ex) { ex.printStackTrace(); }
            }
            e.printStackTrace();
            return false;
        } finally {
            if (con != null) {
                try { con.close(); } catch (SQLException e) { e.printStackTrace(); }
            }
        }
    }

    public void displayTransactionHistory(String accountNumber) {
        String query = "SELECT * FROM transactions WHERE account_number = ? ORDER BY transaction_date DESC";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement pst = con.prepareStatement(query)) {
            pst.setString(1, accountNumber);
            ResultSet rs = pst.executeQuery();
            System.out.println("\n--- Transaction History for " + accountNumber + " ---");
            System.out.printf("%-5s | %-10s | %-10s | %-20s\n", "ID", "Amount", "Type", "Date");
            while (rs.next()) {
                System.out.printf("%-5d | %-10.2f | %-10s | %-20s\n",
                        rs.getInt("id"),
                        rs.getDouble("amount"),
                        rs.getString("transaction_type"),
                        rs.getTimestamp("transaction_date"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public String getAccountNumberByUserId(int userId) {
        String query = "SELECT account_number FROM accounts WHERE user_id = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement pst = con.prepareStatement(query)) {
            pst.setInt(1, userId);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                return rs.getString("account_number");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
