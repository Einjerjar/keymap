package com.github.einjerjar.mc.keymap.keys.registry.keymap;

import com.github.einjerjar.mc.keymap.keys.wrappers.keys.KeyHolder;

import java.util.List;

public interface KeymapSource {
    List<KeyHolder> getKeyHolders();

    boolean canUseSource();
}
