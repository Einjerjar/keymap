package com.github.einjerjar.mc.keymap.keys.category;

import com.github.einjerjar.mc.keymap.keys.CategoryHolder;
import com.github.einjerjar.mc.keymap.keys.KeybindHolder;
import fi.dy.masa.malilib.hotkeys.IHotkey;
import fi.dy.masa.malilib.hotkeys.KeybindCategory;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MalilibCategory implements CategoryHolder {
    KeybindCategory category;
    Text categoryName;

    List<KeybindHolder> bindings = new ArrayList<>();
    Map<String, KeybindHolder> bindingMap = new HashMap<>();

    public MalilibCategory(KeybindCategory category) {
        this.category = category;
        this.categoryName = new TranslatableText(category.getCategory());

        for (IHotkey hk : category.getHotkeys()) {
            // MalilibKeybind mk = new MalilibKeybind()
        }
    }

    @Override
    public Text getCategoryName() {
        return categoryName;
    }

    @Override
    public String getCategoryKey() {
        return category.getCategory();
    }

    @Override
    public void addKeybind(KeybindHolder kb) {
    }

    @Override
    public List<KeybindHolder> getKeybinds() {
        return null;
    }

    @Override
    public KeybindHolder getKeyByTranslationKey(String key) {
        return null;
    }

    @Override
    public String getModName() {
        return category.getModName();
    }

    @Override
    public void sortKeybinds() {

    }

    @Override
    public int size() {
        return 0;
    }
}
