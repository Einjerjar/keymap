package com.github.einjerjar.mc.keymap.keys;

import net.minecraft.text.Text;

import java.util.List;

public abstract class KeybindHolder {
    public CategoryHolder category;

    public abstract List<Integer> getCode();

    public abstract Text getKeyTranslation();

    public abstract Text getTranslation();

    public abstract String getTranslationKey();

    public abstract void assignHotKey(Integer[] hotkey, boolean mouse);

    public abstract void resetHotkey();
}
