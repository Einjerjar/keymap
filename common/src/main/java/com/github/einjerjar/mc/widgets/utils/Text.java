package com.github.einjerjar.mc.widgets.utils;

import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;

public class Text {

    private Text() {
    }

    public static MutableComponent literal(String s) {
        return new TextComponent(s);
    }

    public static MutableComponent translatable(String s) {
        return new TranslatableComponent(s);
    }
}
