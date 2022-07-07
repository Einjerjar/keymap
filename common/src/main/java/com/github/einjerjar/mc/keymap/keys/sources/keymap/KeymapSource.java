package com.github.einjerjar.mc.keymap.keys.sources.keymap;

import com.github.einjerjar.mc.keymap.keys.wrappers.keys.KeyHolder;

import java.util.List;

/**
 * Simple interface for sources of keybinds
 */
public interface KeymapSource {
    /**
     * @return List of keyHolders provided by this source
     */
    List<KeyHolder> getKeyHolders();

    /**
     * @return Whether for some reason or another, this source might not be available
     *         (eg; integration disabled)
     */
    boolean canUseSource();
}
