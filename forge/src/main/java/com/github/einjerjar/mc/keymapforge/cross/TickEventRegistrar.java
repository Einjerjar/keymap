package com.github.einjerjar.mc.keymapforge.cross;

import com.github.einjerjar.mc.keymap.Keymap;
import com.github.einjerjar.mc.keymap.client.gui.screen.KeymapScreen;
import net.minecraft.client.Minecraft;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.IEventBus;

public class TickEventRegistrar {
    private TickEventRegistrar() {
    }

    public static void execute() {
        IEventBus modEventBus = MinecraftForge.EVENT_BUS;
        modEventBus.addListener(TickEventRegistrar::clientTick);
    }

    public static void clientTick(TickEvent.ClientTickEvent e) {
        while (Keymap.kmOpenMapper().consumeClick()) {
            Minecraft.getInstance().setScreen(new KeymapScreen(null));
        }
    }
}
