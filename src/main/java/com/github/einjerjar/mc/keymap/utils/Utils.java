package com.github.einjerjar.mc.keymap.utils;

import java.text.Normalizer;

public class Utils {
    public static final  String SEPARATOR       = "--------------------";
    private static final int    MAX_SLUG_LENGTH = 256;

    private Utils() {
    }

    public static int clamp(int x, int min, int max) {
        return Math.max(Math.min(x, max), min);
    }

    public static double clamp(double x, double min, double max) {
        return Math.max(Math.min(x, max), min);
    }

    public static String slugify(final String s) {
        //algorithm used in https://github.com/slugify/slugify/blob/master/core/src/main/java/com/github/slugify/Slugify.java
        final String intermediateResult = Normalizer
                .normalize(s, Normalizer.Form.NFD)
                .replaceAll("[^\\p{ASCII}]", "")
                .replaceAll("[^-_a-zA-Z\\d]", "-").replaceAll("\\s+", "-")
                .replaceAll("-+", "-").replaceAll("^-", "")
                .replaceAll("-$", "").toLowerCase();
        return intermediateResult.substring(0,
                Math.min(MAX_SLUG_LENGTH, intermediateResult.length()));
    }
}
