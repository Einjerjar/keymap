package com.github.einjerjar.mc.widgets.utils;

public class WidgetUtils {
    private WidgetUtils() {
    }

    public static double clamp(double x, double min, double max) {
        return Math.max(Math.min(x, max), min);
    }

    public static int clamp(int x, int min, int max) {
        return Math.max(Math.min(x, max), min);
    }
}
