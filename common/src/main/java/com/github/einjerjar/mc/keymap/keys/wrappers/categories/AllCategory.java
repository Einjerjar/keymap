package com.github.einjerjar.mc.keymap.keys.wrappers.categories;

import com.github.einjerjar.mc.widgets.utils.Text;
import net.minecraft.locale.Language;
import net.minecraft.network.chat.Component;

public class AllCategory implements CategoryHolder {
    protected static final String    CAT_ALL        = "keymap.listCatAll";
    protected final        Component translatedName = Text.translatable(CAT_ALL);

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
