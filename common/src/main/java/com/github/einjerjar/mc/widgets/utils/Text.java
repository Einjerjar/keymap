package com.github.einjerjar.mc.widgets.utils;


import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

public class Text {

    private Text() {
    }

    public static MutableComponent literal(String s) {
        return Component.literal(s);
    }

    public static MutableComponent translatable(String s) {
        return Component.translatable(s);
    }
}
