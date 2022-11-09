package com.spiritlight.itemabilities.utils;

import java.util.Random;

public class Probabilities {
    private static final Random random = new Random();

    public static boolean passPredicate(int max) {
        if(max >= 100) return true;
        if(max <= 0) return false;
        return random.nextInt(100) + 1 > max;
    }
}
