package com.github.einjerjar.mc.keymapfabric;

import com.github.einjerjar.mc.keymap.Keymap;
import net.fabricmc.api.DedicatedServerModInitializer;

public class KeymapFabricServer implements DedicatedServerModInitializer {
    @Override public void onInitializeServer() {
        Keymap.logger().warn(Keymap.SERVER_WARN);
    }
}
