package com.github.einjerjar.mc.keymap.keys;

import net.minecraft.text.Text;

import java.util.List;

public interface KeybindHolder {
    List<Integer> getCode();

    Text getKeyTranslation();

    Text getTranslation();

    String getTranslationKey();

    void assignHotKey(int[] hotkey, boolean mouse);

    void resetHotkey();
}
