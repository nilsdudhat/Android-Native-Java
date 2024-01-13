package com.udemy.lib;

import java.util.Scanner;

public class ReverseString {

    public static void main(String[] args) {

        System.out.println("Please enter String: ");

        Scanner scanner = new Scanner(System.in);

        String input = scanner.nextLine();

        StringBuilder reverse = new StringBuilder();

        for (int i = (input.length() - 1); i >= 0; i--) {
            reverse.append(input.charAt(i));
        }

        System.out.println("Reversed String: " + reverse);
    }
}
