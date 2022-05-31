package com.github.einjerjar.mc.keymap.keys.registry;

import com.github.einjerjar.mc.keymap.keys.wrappers.KeyHolder;
import com.github.einjerjar.mc.keymap.keys.wrappers.VanillaKeymap;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;

import java.util.ArrayList;
import java.util.List;

public final class VanillaSource implements KeymapSource {

    @Override public List<KeyHolder> getKeyHolders() {
        List<KeyHolder> keymaps = new ArrayList<>();
        for (KeyMapping km : Minecraft.getInstance().options.keyMappings) {
            KeyHolder kh = new VanillaKeymap(km);
            keymaps.add(kh);
        }
        return keymaps;
    }

    @Override public boolean canUseSource() {
        return true;
    }
}
