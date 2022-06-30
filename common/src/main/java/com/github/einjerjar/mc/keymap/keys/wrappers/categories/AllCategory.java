package com.github.einjerjar.mc.keymap.keys.wrappers.categories;

import net.minecraft.locale.Language;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;

public class AllCategory implements CategoryHolder {
    protected static final String    CAT_ALL        = "keymap.listCatAll";
    protected final        Component translatedName = new TranslatableComponent(CAT_ALL);

    @Override public String getTranslatableName() {
        return CAT_ALL;
    }

    @Override public Component getTranslatedName() {
        return translatedName;
    }

    @Override public String getModName() {
        return Language.getInstance().getOrDefault(CAT_ALL);
    }

    @Override public String getFilterSlug() {
        return "";
    }
}
