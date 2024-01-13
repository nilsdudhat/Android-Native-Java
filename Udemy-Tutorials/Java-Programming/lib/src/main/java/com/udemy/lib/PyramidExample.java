package com.udemy.lib;

import java.util.Scanner;

public class PyramidExample {

    public static void main(String[] args) {
        System.out.println("Please enter a Number: ");

        Scanner scanner = new Scanner(System.in);
        int input = scanner.nextInt();

        for (int i = 1; i <= input; i++) {
            int spaces = input - i;
            for (int j = 1; j <= spaces; j++) {
                System.out.print(" ");
            }
            for (int j = 1; j <= i; j++) {
                System.out.print(i + " ");
            }
            System.out.println(" ");
        }
    }
}
