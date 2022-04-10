package com.github.einjerjar.mc.keymap.keys;

import net.minecraft.text.Text;

import java.util.List;

public interface CategoryHolder {

    Text categoryName();

    String categoryKey();

    void addKeybind(KeybindHolder kb);

    List<KeybindHolder> keybinds();

    String getModName();

    int size();
}
