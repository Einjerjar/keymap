package com.github.einjerjar.mc.keymap.keys.category;

import com.github.einjerjar.mc.keymap.keys.CategoryHolder;
import com.github.einjerjar.mc.keymap.keys.KeybindHolder;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class VanillaCategory implements CategoryHolder {
    String category;
    Text categoryName;

    List<KeybindHolder> bindings = new ArrayList<>();
    Map<String, KeybindHolder> bindingMap = new HashMap<>();

    public VanillaCategory(String category) {
        this.category = category;
        this.categoryName = new TranslatableText(category);
    }

    @Override
    public Text categoryName() {
        return categoryName;
    }

    @Override
    public String categoryKey() {
        return category;
    }

    @Override
    public void addKeybind(KeybindHolder kb) {
        if (bindings.contains(kb)) return;
        bindings.add(kb);
        bindingMap.put(kb.translationKey(), kb);
        kb.category = this;
    }

    @Override
    public List<KeybindHolder> keybinds() {
        return bindings;
    }

    @Override
    public String getModName() {
        return "minecraft";
    }

    @Override
    public int size() {
        return bindings.size();
    }
}
