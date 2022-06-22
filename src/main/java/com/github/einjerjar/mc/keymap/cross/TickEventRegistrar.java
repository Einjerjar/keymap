package com.github.einjerjar.mc.keymap.cross;

import com.github.einjerjar.mc.keymap.Keymap;
import com.github.einjerjar.mc.keymap.client.gui.screen.KeymapScreen;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;

public class TickEventRegistrar {
    public static void execute() {
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (Keymap.kmOpenMapper().consumeClick()) {
                client.setScreen(new KeymapScreen(null));
            }
        });
    }
}
