package com.udemy.lib;

import java.util.Scanner;

public class CountExample {

    public static void main(String[] args) {
        System.out.println("Please enter String to check: ");
        
        Scanner scanner = new Scanner(System.in);
        
        String input = scanner.nextLine();
        
        int alphabetCount = 0;
        int specialCharCount = 0;
        int numbersCount = 0;

        for (int i = 0; i < input.length(); i++) {
            if (Character.isAlphabetic(input.charAt(i))) {
                alphabetCount++;
            } else if (Character.isDigit(input.charAt(i))) {
                numbersCount++;
            } else {
                specialCharCount++;
            }
        }

        System.out.println("Total Alphabets: " + alphabetCount);
        System.out.println("Total Numbers: " + numbersCount);
        System.out.println("Total Special Characters: " + specialCharCount);
    }
}
