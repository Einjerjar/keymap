package com.github.einjerjar.mc.keymapforge.cross.services;

import com.github.einjerjar.mc.keymap.cross.services.IKeybindHelper;
import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Options;
import org.apache.commons.lang3.ArrayUtils;

public class ForgeKeybindHelper implements IKeybindHelper {

    @Override
    public KeyMapping create(InputConstants.Type type, int code, String name, String category) {
        KeyMapping k = new KeyMapping(name, type, code, category);

        Options options = Minecraft.getInstance().options;
        options.keyMappings = ArrayUtils.add(options.keyMappings, k);
        return k;
    }
}
