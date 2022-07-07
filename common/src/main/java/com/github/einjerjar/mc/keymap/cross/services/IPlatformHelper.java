package com.github.einjerjar.mc.keymap.cross.services;

import net.minecraft.client.gui.screens.Screen;

import java.io.File;

/**
 * Helper service to be overridden loader specific ones
 */
public interface IPlatformHelper {
    /**
     * @return The current mod loader
     */
    String loader();

    /**
     * Check whether a mod is currently loaded
     *
     * @param modid The mod to look for
     *
     * @return Whether the check was a success
     */
    boolean modLoaded(String modid);

    /**
     * @return Whether the mod is running inside a development environment
     */
    boolean dev();

    /**
     * Fetches a reference to a file under the config folder
     *
     * @param file The filename
     *
     * @return The file reference
     */
    File config(String file);

    /**
     * Get the platform specific config screen
     *
     * @param parent The previous screen, can be null
     *
     * @return The screen
     */
    Screen configScreen(Screen parent);

    /**
     * @return A reference to the loader's implementation of the IKeybindHelper
     */
    IKeybindHelper keybindHelper();

    /**
     * @return A reference to the loader's implementation of the ITickHelper
     */
    ITickHelper tickHelper();
}
