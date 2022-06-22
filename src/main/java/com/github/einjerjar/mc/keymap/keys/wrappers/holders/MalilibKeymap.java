package com.github.einjerjar.mc.keymap.keys.wrappers.holders;

import com.github.einjerjar.mc.keymap.keys.extrakeybind.KeyComboData;
import net.minecraft.network.chat.Component;

import java.util.List;

public class MalilibKeymap implements KeyHolder {
    @Override public List<Integer> getCode() {
        return null;
    }

    @Override public Integer getKeyHash() {
        return null;
    }

    @Override public boolean isComplex() {
        return false;
    }

    @Override public KeyComboData getComplexCode() {
        return null;
    }

    @Override public String getTranslatableName() {
        return null;
    }

    @Override public String getCategory() {
        return null;
    }

    @Override public Component getTranslatedName() {
        return null;
    }

    @Override public String getTranslatableKey() {
        return null;
    }

    @Override public Component getTranslatedKey() {
        return null;
    }

    @Override public String getSearchString() {
        return null;
    }

    @Override public boolean setKey(List<Integer> keys, boolean mouse) {
        return false;
    }

    @Override public String getModName() {
        return null;
    }

    @Override public boolean resetKey() {
        return false;
    }
}
