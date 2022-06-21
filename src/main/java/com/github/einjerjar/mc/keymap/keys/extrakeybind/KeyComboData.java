package com.github.einjerjar.mc.keymap.keys.extrakeybind;

import com.github.einjerjar.mc.keymap.keys.KeyType;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.Accessors;

@Getter
@ToString
@EqualsAndHashCode
@AllArgsConstructor
@Accessors(fluent = true, chain = true)
public class KeyComboData {
    protected int     keyCode;
    protected KeyType keyType;
    protected boolean alt;
    protected boolean shift;
    protected boolean ctrl;

    public boolean onlyKey() {
        return !(alt || shift || ctrl) && !KeybindRegistry.MODIFIER_KEYS().contains(keyCode);
    }
}
