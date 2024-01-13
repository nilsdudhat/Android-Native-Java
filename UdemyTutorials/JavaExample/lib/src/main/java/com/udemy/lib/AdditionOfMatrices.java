package com.udemy.lib;

import java.util.Arrays;

public class AdditionOfMatrices {
    public static void main(String[] args) {
        int[][] firstMatrices = new int[3][3];

        firstMatrices[0] = new int[]{1, 2, 1};
        firstMatrices[1] = new int[]{2, 1, 3};
        firstMatrices[2] = new int[]{3, 2, 1};

        int[][] secondMatrices = new int[3][3];

        secondMatrices[0] = new int[]{2, 1, 2};
        secondMatrices[1] = new int[]{3, 2, 1};
        secondMatrices[2] = new int[]{1, 3, 2};

        int[][] finalMatrices = new int[3][3];

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                int firstMatricesValue = firstMatrices[i][j];
                int secondMatricesValue = secondMatrices[i][j];

                finalMatrices[i][j] = firstMatricesValue + secondMatricesValue;
            }
        }

        System.out.println(Arrays.deepToString(finalMatrices));
    }
}
