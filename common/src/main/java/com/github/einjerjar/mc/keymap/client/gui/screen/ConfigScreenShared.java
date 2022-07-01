package com.github.einjerjar.mc.keymap.client.gui.screen;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import net.minecraft.client.gui.screens.Screen;

import java.util.function.Function;

@Accessors(fluent = true)
public class ConfigScreenShared {
    @Getter @Setter protected static Function<Screen, Screen> provider;

    private ConfigScreenShared() {
    }
}
