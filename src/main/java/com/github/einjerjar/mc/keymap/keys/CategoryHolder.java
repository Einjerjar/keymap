package com.github.einjerjar.mc.keymap.keys;

import net.minecraft.text.Text;

import java.util.List;

public interface CategoryHolder {
    Text getCategoryName();

    String getCategoryKey();

    void addKeybind(KeybindHolder kb);

    List<KeybindHolder> getKeybinds();

    KeybindHolder getKeyByTranslationKey(String key);

    String getModName();

    void sortKeybinds();

    int count();
}
