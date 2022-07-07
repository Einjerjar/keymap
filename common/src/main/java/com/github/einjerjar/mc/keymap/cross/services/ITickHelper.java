package com.github.einjerjar.mc.keymap.cross.services;

import net.minecraft.client.Minecraft;

public interface ITickHelper {
    void registerEndClientTick(EndTickListener listener);

    interface EndTickListener {
        void execute(Minecraft client);
    }
}
