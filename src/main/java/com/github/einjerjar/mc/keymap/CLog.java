package com.github.einjerjar.mc.keymap;

// Simple logger wrapper with mod_id
// no particular reason, just felt like making one
public class CLog {
    public static void info(String s) {
        // KeymapMain.LOGGER.info(String.format("[%s] %s", KeymapMain.MOD_ID, s));
        KeymapMain.LOGGER().info(s);
    }

    public static void error(String s) {
        // KeymapMain.LOGGER.error(String.format("[%s] %s", KeymapMain.MOD_ID, s));
        KeymapMain.LOGGER().error(s);
    }

    public static void debug(String s) {
        // KeymapMain.LOGGER.debug(String.format("[%s] %s", KeymapMain.MOD_ID, s));
        KeymapMain.LOGGER().debug(s);
    }

    public static void warn(String s) {
        // KeymapMain.LOGGER.warn(String.format("[%s] %s", KeymapMain.MOD_ID, s));
        KeymapMain.LOGGER().warn(s);
    }

    public static void trace(String s) {
        // KeymapMain.LOGGER.trace(String.format("[%s] %s", KeymapMain.MOD_ID, s));
        KeymapMain.LOGGER.trace(s);
    }
}
