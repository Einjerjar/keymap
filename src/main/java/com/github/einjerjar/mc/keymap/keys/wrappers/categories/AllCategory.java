package com.github.einjerjar.mc.keymap.keys.wrappers.categories;

import net.minecraft.locale.Language;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;

public class AllCategory implements CategoryHolder {
    protected final String    key            = "keymap.listCatAll";
    protected final Component translatedName = new TranslatableComponent(key);

    @Override public String getTranslatableName() {
        return key;
    }

    @Override public Component getTranslatedName() {
        return translatedName;
    }

    @Override public String getModName() {
        return Language.getInstance().getOrDefault(key);
    }

    @Override public String getFilterSlug() {
        return "";
    }
}
