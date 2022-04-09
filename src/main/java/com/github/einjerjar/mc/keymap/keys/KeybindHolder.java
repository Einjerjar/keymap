package com.github.einjerjar.mc.keymap.keys;

import net.minecraft.text.Text;

import java.util.List;

public abstract class KeybindHolder {
    public CategoryHolder category;

    public abstract List<Integer> code();

    public abstract Text keyTranslation();

    public abstract Text translation();

    public abstract String translationKey();

    public abstract void assignHotKey(Integer[] hotkey, boolean mouse);

    public abstract void resetHotkey();

    public abstract void updateState();
}
