package com.github.einjerjar.mc.keymap.cross.compat;

import com.github.einjerjar.mc.keymap.Keymap;
import com.github.einjerjar.mc.keymap.client.gui.screen.KeymapScreen;
import com.github.einjerjar.mc.keymap.client.gui.screen.LayoutSelectionScreen;
import me.pieking1215.invmove.module.ModuleImpl;

public class KeymapInvMoveCompat extends ModuleImpl {
    @Override public String getId() {
        return Keymap.MOD_ID();
    }

    public KeymapInvMoveCompat() {
        super();

        register(KeymapScreen.class).movement(Movement.FORCE_DISABLE);
        register(LayoutSelectionScreen.class).movement(Movement.FORCE_DISABLE);
    }
}
