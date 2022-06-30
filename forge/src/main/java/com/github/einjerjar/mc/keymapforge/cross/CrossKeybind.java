package com.github.einjerjar.mc.keymapforge.cross;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraftforge.fmlclient.registry.ClientRegistry;

public class CrossKeybind {
    private CrossKeybind() {
    }

    public static KeyMapping execute(InputConstants.Type inputType, int keyCode, String name, String cat) {
        KeyMapping k = new KeyMapping(
                name,
                inputType,
                keyCode,
                cat
        );

        ClientRegistry.registerKeyBinding(k);

        return k;
    }
}
