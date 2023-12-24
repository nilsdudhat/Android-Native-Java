package com.iphoto.iphonegallery.iosgallerylauncher.photoios.iosgallery.forandroid.util;

import java.util.Random;

public class MathUtils {

    public static int getForYouSize(int arraySize) {
        int forYouSize = 0;

        if (arraySize < 10) {

        } else if (arraySize < 15) {
            forYouSize = 5;
        } else if (arraySize < 25) {
            forYouSize = 10;
        } else if (arraySize < 50) {
            forYouSize = 15;
        } else {
            forYouSize = 20;
        }

        return forYouSize;
    }

    public static int getRandomNumber(int min, int max) {
        return (new Random()).nextInt((max - min) + 1) + min;
    }
}
