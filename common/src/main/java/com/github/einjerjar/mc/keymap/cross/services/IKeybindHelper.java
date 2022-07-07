package com.github.einjerjar.mc.keymap.cross.services;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;


/**
 * Helper service to be overridden loader specific ones
 */
public interface IKeybindHelper {
    /**
     * Creates a keymap for triggering actions ingame
     *
     * @param type     The type of key (mouse/board)
     * @param code     The default keyCode
     * @param name     The translation key of the item
     * @param category The translation key of the item's category
     *
     * @return A KeyMapping instance
     */
    KeyMapping create(
            InputConstants.Type type,
            int code,
            String name,
            String category
    );
}
