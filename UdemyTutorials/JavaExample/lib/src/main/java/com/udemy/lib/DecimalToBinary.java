package com.udemy.lib;

import java.util.Scanner;

public class DecimalToBinary {

    public static void main(String[] args) {
        convertToBinary();
    }

    private static void convertToBinary() {
        System.out.println("Please enter value to convert into binary: ");

        Scanner scanner = new Scanner(System.in);
        int input = scanner.nextInt();

        int conversion = input;

        int[] binaryArray = new int[32];

        int index = 0;

        while (conversion != 0) {
            binaryArray[index] = conversion % 2;
            conversion = conversion / 2;
            index++;
        }

        System.out.print("Converted Binary value of " + input + " is: ");
        for (int j = index - 1; j >= 0; j--) {
            System.out.print(binaryArray[j]);
        }
        System.out.println(" ");
        System.out.println(" ");
        System.out.println(" ");

        convertToBinary();
    }
}
