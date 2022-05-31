package com.github.einjerjar.mc.keymap.keys.registry;

import com.github.einjerjar.mc.keymap.keys.wrappers.KeyHolder;

import java.util.List;

public interface KeymapSource {
    List<KeyHolder> getKeyHolders();

    boolean canUseSource();
}
