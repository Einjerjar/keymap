package com.github.einjerjar.mc.keymapforge.cross.services;

import com.github.einjerjar.mc.keymap.cross.services.IKeybindHelper;
import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraftforge.client.ClientRegistry;

public class ForgeKeybindHelper implements IKeybindHelper {

    @Override public KeyMapping create(InputConstants.Type type, int code, String name, String category) {
        KeyMapping k = new KeyMapping(
                name,
                type,
                code,
                category
        );

        ClientRegistry.registerKeyBinding(k);

        return k;
    }
}
