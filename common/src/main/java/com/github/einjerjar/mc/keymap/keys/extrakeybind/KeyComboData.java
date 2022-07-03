package com.github.einjerjar.mc.keymap.keys.extrakeybind;

import com.github.einjerjar.mc.keymap.keys.KeyType;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.util.Objects;

@Getter
@ToString
@Accessors(fluent = true, chain = true)
public class KeyComboData {
    public static final KeyComboData UNBOUND = new KeyComboData(-1);
    protected int     keyCode;
    protected KeyType keyType;
    protected boolean alt;
    protected boolean shift;
    protected boolean ctrl;

    public KeyComboData(int keyCode) {
        this(keyCode, KeyType.KEYBOARD);
    }

    public KeyComboData(int keyCode, KeyType keyType) {
        this(keyCode, keyType, false, false, false);
    }

    public KeyComboData(int keyCode, boolean alt, boolean shift, boolean ctrl) {
        this(keyCode, KeyType.KEYBOARD, alt, shift, ctrl);
    }

    public KeyComboData(int keyCode, KeyType keyType, boolean alt, boolean shift, boolean ctrl) {
        this.keyCode = keyCode;
        this.keyType = keyType;
        this.alt     = alt;
        this.shift   = shift;
        this.ctrl    = ctrl;

        if (isModifierOnly()) {
            this.alt = this.shift = this.ctrl = false;
        }
    }

    public boolean onlyKey() {
        return !(alt || shift || ctrl);
    }

    public int modifierCount() {
        int x = alt ? 1 : 0;
        x += shift ? 1 : 0;
        x += ctrl ? 1 : 0;
        return x;
    }

    public boolean isModifier() {
        return KeybindRegistry.MODIFIER_KEYS.contains(keyCode);
    }

    public boolean isModifierOnly() {
        return isModifier() && modifierCount() == 1;
    }

    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        KeyComboData that = (KeyComboData) o;
        return keyCode == that.keyCode && alt == that.alt && shift == that.shift && ctrl == that.ctrl && keyType == that.keyType;
    }

    @Override public int hashCode() {
        return Objects.hash(keyCode, keyType, alt, shift, ctrl);
    }
}
