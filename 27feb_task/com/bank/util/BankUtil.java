package com.bank.util;

import java.util.Random;

public class BankUtil {

    public static String generateAccountNumber() {
        Random r = new Random();
        return "ACC" + (10000 + r.nextInt(90000));
    }

    public static void validateMinimumBalance(double balance) throws Exception {

        if(balance < 1000){
            throw new Exception("Minimum balance should be ₹1000");
        }

    }

}