package com.github.einjerjar.mc.keymap.keys.wrappers.categories;

import com.github.einjerjar.mc.keymap.utils.Utils;
import com.github.einjerjar.mc.widgets.utils.Text;
import net.minecraft.locale.Language;
import net.minecraft.network.chat.Component;

import java.util.List;

public class VanillaCategory implements CategoryHolder {
    public static final List<String> MC_CATEGORIES = List.of(
            "key.categories.movement",
            "key.categories.misc",
            "key.categories.multiplayer",
            "key.categories.gameplay",
            "key.categories.ui",
            "key.categories.inventory",
            "key.categories.creative"
    );

    protected String    category;
    protected Component translatedName;

    public VanillaCategory(String category) {
        this.category       = category;
        this.translatedName = Text.translatable(category);
    }

    @Override public String getTranslatableName() {
        return category;
    }

    @Override public Component getTranslatedName() {
        return translatedName;
    }

    @Override public String getModName() {
        String s = category;
        if (MC_CATEGORIES.contains(category)) s = "advancements.story.root.title";
        return Language.getInstance().getOrDefault(s);
    }

    @Override public String getFilterSlug() {
        return String.format("@%s", Utils.slugify(Language.getInstance().getOrDefault(category)));
    }
}
