package com.github.einjerjar.mc.keymapfabric.cross.services;

import com.github.einjerjar.mc.keymap.cross.services.IKeybindHelper;
import com.mojang.blaze3d.platform.InputConstants;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.KeyMapping;

public class FabricKeybindHelper implements IKeybindHelper {
    @Override public KeyMapping create(InputConstants.Type type, int code, String name, String category) {
        return KeyBindingHelper.registerKeyBinding(new KeyMapping(
                name,
                type,
                code,
                category
        ));
    }
}
