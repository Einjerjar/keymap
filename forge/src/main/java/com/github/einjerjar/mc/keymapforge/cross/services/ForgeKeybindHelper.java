package com.github.einjerjar.mc.keymapforge.cross.services;

import com.github.einjerjar.mc.keymap.cross.services.IKeybindHelper;
import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.fml.common.Mod;

public class ForgeKeybindHelper implements IKeybindHelper {
    public static KeyMapping k;

    @Override public KeyMapping create(InputConstants.Type type, int code, String name, String category) {
        k = new KeyMapping(
                name,
                type,
                code,
                category
        );

        Mod.EventBusSubscriber.Bus.MOD.bus().get().addListener(ForgeKeybindHelper::registerKeyBinding);

        return k;
    }

    public static void registerKeyBinding(RegisterKeyMappingsEvent e) {
        e.register(k);
    }
}
