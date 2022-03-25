package com.github.einjerjar.mc.keymap.keys;

import net.minecraft.text.Text;

import java.util.List;

public interface KeybindHolder {
    public List<Integer> getCode();
    public Text getKeyTranslation();
    public Text getTranslation();
    public String getTranslationKey();
    public void assignHotKey(int[] hotkey, boolean mouse);
}
