package com.udemy.lib;

public class Division {

    private final double division;
    private final int remainder;

    public Division(int upper, int lower) {
        division = (double) upper / lower;
        remainder = upper % lower;
    }

    public int getRemainder() {
        return remainder;
    }

    public double getDivision() {
        return division;
    }
}
