package com.github.einjerjar.mc.keymap;

import net.fabricmc.api.DedicatedServerModInitializer;

public class KeymapFabricLikeServer implements DedicatedServerModInitializer {
    @Override public void onInitializeServer() {
        Keymap.logger().warn(Keymap.SERVER_WARN);
    }
}
