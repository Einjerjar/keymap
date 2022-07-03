package com.github.einjerjar.mc.keymap.client.gui.screen;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import net.minecraft.client.gui.screens.Screen;

@Accessors(fluent = true)
public class ConfigScreenShared {
    @Getter @Setter protected static ConfigScreenProvider provider;

    private ConfigScreenShared() {
    }

    @FunctionalInterface
    public interface ConfigScreenProvider {
        Screen execute(Screen parent);
    }
}
