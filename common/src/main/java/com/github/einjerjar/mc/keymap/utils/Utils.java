package com.github.einjerjar.mc.keymap.utils;


import net.minecraft.locale.Language;

import java.text.Normalizer;

/**
 * General purpose utility
 */
public class Utils {
    /**
     * Redundant string used in the mod
     */
    public static final    String   SEPARATOR        = "--------------------";
    protected static final int      MAX_SLUG_LENGTH  = 256;
    protected static       Language languageInstance = null;

    private Utils() {
    }


    /**
     * Gets the language instace for quick translations without going through components
     *
     * @return The Language instance
     */
    public static synchronized Language languageInstance() {
        if (languageInstance == null) languageInstance = Language.getInstance();
        return languageInstance;
    }

    /**
     * Translates a string via the Language instance without going through componets
     *
     * @param key The translation key
     *
     * @return The translated value, or the key if not found
     */
    public static String translate(String key) {
        return languageInstance().getOrDefault(key);
    }

    /**
     * Basic integer clamp
     *
     * @param x   The value to clamp
     * @param min The minimum value
     * @param max The maximum value
     *
     * @return The clamped value
     */
    public static int clamp(int x, int min, int max) {
        return Math.max(Math.min(x, max), min);
    }

    /**
     * Basic Double clamp
     *
     * @param x   The value to clamp
     * @param min The minimum value
     * @param max The maximum value
     *
     * @return The clamped value
     */
    public static double clamp(double x, double min, double max) {
        return Math.max(Math.min(x, max), min);
    }

    /**
     * Sliggifies string for use with search queries;
     * Directly taken from <a href="https://github.com/slugify/slugify/blob/master/core/src/main/java/com/github/slugify/Slugify.java">https://github.com/slugify/slugify</a>
     *
     * @param s The string to process
     *
     * @return The sluggified string
     */
    public static String slugify(final String s) {
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
