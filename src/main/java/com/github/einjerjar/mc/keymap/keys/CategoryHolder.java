package com.github.einjerjar.mc.keymap.keys;

import net.minecraft.text.Text;

import java.util.List;

public interface CategoryHolder {
    public Text getCategoryName();
    public String getCategoryKey();

    public void addKeybind(KeybindHolder kb);
    public List<KeybindHolder> getKeybinds();
    public KeybindHolder getKeyByTranslationKey(String key);

    public String getModName();

    public void sortKeybinds();

    public int count();
}
