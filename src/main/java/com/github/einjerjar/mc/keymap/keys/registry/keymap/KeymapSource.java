package com.github.einjerjar.mc.keymap.keys.registry.keymap;

import com.github.einjerjar.mc.keymap.keys.wrappers.holders.KeyHolder;

import java.util.List;

public interface KeymapSource {
    List<KeyHolder> getKeyHolders();

    boolean canUseSource();
}
