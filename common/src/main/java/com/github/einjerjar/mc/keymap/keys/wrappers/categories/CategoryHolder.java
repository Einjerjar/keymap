package com.github.einjerjar.mc.keymap.keys.wrappers.categories;

import net.minecraft.network.chat.Component;

/**
 * Abstraction for keybind categories
 */
public interface CategoryHolder {
    /**
     * @return The translation key of the category
     */
    String getTranslatableName();

    /**
     * @return A translated name of the category as a Component
     */
    Component getTranslatedName();

    /**
     * @return The mod/source of the category
     */
    String getModName();

    /**
     * TODO: Figure out a cleaner way to make sure the categoryHolders and the keyHolders are synced up
     *
     * @return The slug for the category
     */
    String getFilterSlug();
}
