package com.udemy.lib;

import java.util.Scanner;

public class DivisionExample {

    public static void main(String[] args) {

        Scanner scanner1 = new Scanner(System.in);

        System.out.println("Please enter upper value for division");
        int upper = scanner1.nextInt();

        Scanner scanner2 = new Scanner(System.in);

        System.out.println("Please enter lower value for division");
        int lower = scanner2.nextInt();

        Division division = new Division(upper, lower);

        System.out.println("Division Result: " + division.getDivision());
        System.out.println("Division Remainder: " + division.getRemainder());
    }
}