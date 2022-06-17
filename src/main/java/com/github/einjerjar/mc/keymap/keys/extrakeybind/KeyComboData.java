package com.github.einjerjar.mc.keymap.keys.extrakeybind;

import com.github.einjerjar.mc.keymap.keys.KeyType;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.Accessors;

@ToString
@EqualsAndHashCode
@AllArgsConstructor
@Accessors(fluent = true, chain = true)
public class KeyComboData {
    @Getter protected int keyCode;
    @Getter protected KeyType keyType;
    @Getter protected boolean alt;
    @Getter protected boolean shift;
    @Getter protected boolean ctrl;

    public boolean onlyKey() {
        return !(alt || shift || ctrl) && !KeybindRegistry.MODIFIER_KEYS().contains(keyCode);
    }
}
