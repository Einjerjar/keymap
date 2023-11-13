package com.github.einjerjar.mc.keymap.keys.wrappers.categories;

import net.minecraft.locale.Language;
import net.minecraft.network.chat.Component;

/**
 * Hacky solution to allow for a category to cover all keybinds
 */
public class AllCategory implements CategoryHolder {
    /**
     * Translation key
     */
    protected static final String CAT_ALL = "keymap.listCatAll";
    /**
     * Cached reference to the translated Component
     */
    protected final Component translatedName = Component.translatable(CAT_ALL);

    @Override
    public String getTranslatableName() {
        return CAT_ALL;
    }

    @Override
    public Component getTranslatedName() {
        return translatedName;
    }

    @Override
    public String getModName() {
        return Language.getInstance().getOrDefault(CAT_ALL);
    }

    /**
     * @return Empty string to remove all filters, and show all categories
     */
    @Override
    public String getFilterSlug() {
        return "";
    }
}
