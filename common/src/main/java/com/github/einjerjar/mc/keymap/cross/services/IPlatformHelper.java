package com.github.einjerjar.mc.keymap.cross.services;

import net.minecraft.client.gui.screens.Screen;

import java.io.File;

public interface IPlatformHelper {
    String loader();

    boolean modLoaded(String modid);

    boolean dev();

    File config(String file);

    Screen configScreen(Screen parent);

    IKeybindHelper keybindHelper();

    ITickHelper tickHelper();
}
