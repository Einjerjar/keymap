package com.github.einjerjar.mc.keymap.cross.services;

import net.minecraft.client.Minecraft;

/**
 * Helper service to be overridden loader specific ones
 */
public interface ITickHelper {
    /**
     * Registers event to the client tick, loosely based of Fabric's END_TICK_CALLBACK
     * which is why the forge implementation is kinda hacky
     *
     * @param listener  The listener to register
     */
    void registerEndClientTick(EndTickListener listener);

    /**
     * The expected format for the listener
     */
    interface EndTickListener {
        void execute(Minecraft client);
    }
}
