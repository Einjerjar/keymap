package com.github.einjerjar.mc.keymap.keys.wrappers;

import net.minecraft.network.chat.Component;

import java.util.List;

public interface KeyHolder {
    List<Integer> getCode();

    String getTranslatableName();

    Component getTranslatedName();

    String getTranslatableKey();

    Component getTranslatedKey();

    boolean setKey(List<Integer> keys, boolean mouse);

    boolean resetKey();
}
