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
    public Text getCategoryName() {
        return categoryName;
    }

    @Override
    public String getCategoryKey() {
        return category;
    }

    @Override
    public void sortKeybinds() {
        bindings.sort((o1, o2) ->
            o1.getTranslation().getString().compareToIgnoreCase(
                o2.getTranslation().getString()));
    }

    @Override
    public void addKeybind(KeybindHolder kb) {
        if (bindings.contains(kb)) return;
        bindings.add(kb);
        bindingMap.put(kb.getTranslationKey(), kb);
    }

    @Override
    public List<KeybindHolder> getKeybinds() {
        return bindings;
    }

    @Override
    public KeybindHolder getKeyByTranslationKey(String key) {
        if (!bindingMap.containsKey(key)) return null;

        return bindingMap.get(key);
    }

    @Override
    public String getModName() {
        return "minecraft";
    }

    @Override
    public int count() {
        return bindings.size();
    }
}
