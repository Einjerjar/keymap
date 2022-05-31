package com.github.einjerjar.mc.keymap.keys.wrappers;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;

public class VanillaCategory implements CategoryHolder{
    String category;
    Component translatedName;

    public VanillaCategory(String category) {
        this.category = category;
        this.translatedName = new TranslatableComponent(category);
    }

    @Override public String getTranslatableName() {
        return category;
    }

    @Override public Component getTranslatedName() {
        return translatedName;
    }

    @Override public String getModName() {
        return "Minecraft";
    }
}
