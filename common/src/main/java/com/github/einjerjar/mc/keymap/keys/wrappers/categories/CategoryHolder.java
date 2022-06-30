package com.github.einjerjar.mc.keymap.keys.wrappers.categories;

import net.minecraft.network.chat.Component;

public interface CategoryHolder {
    String getTranslatableName();

    Component getTranslatedName();

    String getModName();

    String getFilterSlug();
}
