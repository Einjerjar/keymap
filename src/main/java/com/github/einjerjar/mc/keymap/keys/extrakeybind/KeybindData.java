package com.github.einjerjar.mc.keymap.keys.extrakeybind;

import com.github.einjerjar.mc.keymap.keys.KeyType;
import lombok.Getter;
import lombok.experimental.Accessors;

@Accessors(fluent = true, chain = true)
public class KeybindData {
    @Getter private int keyCode;
    @Getter private KeyType keyType;
    @Getter private boolean alt;
    @Getter private boolean shift;
    @Getter private boolean ctrl;
}
