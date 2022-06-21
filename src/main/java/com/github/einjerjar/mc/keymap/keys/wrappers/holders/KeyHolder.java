package com.github.einjerjar.mc.keymap.keys.wrappers.holders;

import com.github.einjerjar.mc.keymap.keys.extrakeybind.KeyComboData;
import net.minecraft.network.chat.Component;

import java.util.List;

public interface KeyHolder {
    List<Integer> getCode();

    Integer getKeyHash();

    boolean isComplex();

    KeyComboData getComplexCode();

    String getTranslatableName();

    String getCategory();

    Component getTranslatedName();

    String getTranslatableKey();

    Component getTranslatedKey();

    String getSearchString();

    boolean setKey(List<Integer> keys, boolean mouse);

    boolean resetKey();
}
