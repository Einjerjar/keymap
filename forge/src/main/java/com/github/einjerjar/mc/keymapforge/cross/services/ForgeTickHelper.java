package com.github.einjerjar.mc.keymapforge.cross.services;

import com.github.einjerjar.mc.keymap.cross.services.ITickHelper;
import net.minecraft.client.Minecraft;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.IEventBus;

public class ForgeTickHelper implements ITickHelper {
    static final IEventBus       modEventBus     = MinecraftForge.EVENT_BUS;
    static       EndTickListener endTickListener = null;

    @Override public void registerEndClientTick(EndTickListener listener) {
        // hacky af, lol
        endTickListener = listener;
        modEventBus.addListener(ForgeTickHelper::clientTick);
    }

    public static void clientTick(TickEvent.ClientTickEvent e) {
        endTickListener.execute(Minecraft.getInstance());
    }
}
