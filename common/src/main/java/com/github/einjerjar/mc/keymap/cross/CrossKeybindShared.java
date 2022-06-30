package com.github.einjerjar.mc.keymap.cross;

import com.mojang.blaze3d.platform.InputConstants;
import lombok.Setter;
import lombok.experimental.Accessors;
import net.minecraft.client.KeyMapping;

@Accessors(fluent = true)
public class CrossKeybindShared {
    @Setter protected static CrossKeybindProvider provider;
    protected                KeyMapping           keymap;

    public CrossKeybindShared(InputConstants.Type inputType, int keyCode, String name, String cat) {
        keymap = provider.execute(inputType, keyCode, name, cat);
    }

    public boolean isDown() {
        return keymap.isDown();
    }

    public boolean isDefault() {
        return keymap.isDefault();
    }

    public boolean isUnbound() {
        return keymap.isUnbound();
    }

    public boolean consumeClick() {
        return keymap.consumeClick();
    }

    @FunctionalInterface
    public interface CrossKeybindProvider {
        KeyMapping execute(InputConstants.Type inputType, int keyCode, String name, String cat);
    }
}
