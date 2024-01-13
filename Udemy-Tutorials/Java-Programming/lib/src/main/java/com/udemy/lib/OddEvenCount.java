package com.udemy.lib;

public class OddEvenCount {

    public static void main(String[] args) {
        int[] array = {2,41,54,4654, 5, 44656, 85,46};

        int odd = 0, even = 0;

        for (int j : array) {
            if ((j % 2) == 0) {
                even++;
            } else {
                odd++;
            }
        }

        System.out.println("Even Count: " + even);
        System.out.println("Odd Count: " + odd);
    }
}
