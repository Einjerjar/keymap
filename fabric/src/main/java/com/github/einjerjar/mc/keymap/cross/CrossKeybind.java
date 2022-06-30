package com.github.einjerjar.mc.keymap.cross;

import com.mojang.blaze3d.platform.InputConstants;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.KeyMapping;

public class CrossKeybind {
    private CrossKeybind() {
    }

    public static KeyMapping execute(InputConstants.Type inputType, int keyCode, String name, String cat) {
        return KeyBindingHelper.registerKeyBinding(new KeyMapping(
                name,
                inputType,
                keyCode,
                cat
        ));
    }
}
