package com.udemy.lib;

import java.util.Arrays;

public class MultiplicationOfArrayIndexes {

    public static void main(String[] args) {
        int[] array1 = {1, 3, -5, 4};
        int[] array2 = {1, 4, -5, -2};

        int[] finalArray = new int[array1.length];

        for (int i = 0; i < array1.length; i++) {
            finalArray[i] = array1[i] * array2[i];
        }

        System.out.println(Arrays.toString(finalArray));
    }
}
