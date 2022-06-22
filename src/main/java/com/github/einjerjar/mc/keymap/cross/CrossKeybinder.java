package com.github.einjerjar.mc.keymap.cross;

import com.mojang.blaze3d.platform.InputConstants;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.KeyMapping;

public class CrossKeybinder {
    protected KeyMapping keymap;

    public CrossKeybinder(InputConstants.Type inputType, int keyCode) {
        keymap = KeyBindingHelper.registerKeyBinding(new KeyMapping(
                "keymap.keyOpenKeymap",
                inputType,
                keyCode,
                "keymap.keyCat"
        ));
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
}
