package com.udemy.lib;

public class AverageValueOfArrayElements {

    public static void main(String[] args) {
        int[] array = {16,14542,4543,64644,545,65,217,48,9};

        int sum = 0;

        for (int j : array) {
            sum = sum + j;
        }

        float average = (float) sum / array.length;
        System.out.println("Average: " + average);
    }
}
