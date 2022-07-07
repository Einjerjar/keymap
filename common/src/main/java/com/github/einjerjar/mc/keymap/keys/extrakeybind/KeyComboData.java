package com.github.einjerjar.mc.keymap.keys.extrakeybind;

import com.github.einjerjar.mc.keymap.keys.KeyType;
import com.mojang.blaze3d.platform.InputConstants;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * A combo object
 */
@Getter
@ToString
@Accessors(fluent = true, chain = true)
public class KeyComboData {
    /**
     * Static reference to an empty/unbound key
     */
    public static final KeyComboData UNBOUND = new KeyComboData(-1);

    /**
     * The primary key's code
     */
    protected int     keyCode;
    /**
     * The type of key, mouse or board
     * TODO: Migrate to InputConstants
     */
    protected KeyType keyType;
    /**
     * Whether the alt key is pressed
     */
    protected boolean alt;
    /**
     * Whether the shift key is pressed
     */
    protected boolean shift;
    /**
     * Whether the control key is pressed
     */
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

    public KeyComboData(InputConstants.Key k) {
        this(k.getValue(), k.getType() == InputConstants.Type.MOUSE ? KeyType.MOUSE : KeyType.KEYBOARD);
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

    /**
     * Convert the combo to a vanilla key class
     *
     * @return The key
     */
    public InputConstants.Key toKey() {
        InputConstants.Type t = keyType == KeyType.KEYBOARD ? InputConstants.Type.KEYSYM : InputConstants.Type.MOUSE;
        return t.getOrCreate(keyCode);
    }

    /**
     * @return The translated name of the primary key of the combo
     */
    public String searchString() {
        return toKey().getDisplayName().getString();
    }

    /**
     * @return A string to reference this key, can be a single letter/word to a series of words
     */
    public String toKeyString() {
        List<String> x = new ArrayList<>();
        if (ctrl) x.add("ctrl");
        if (alt) x.add("alt");
        if (shift) x.add("shift");
        x.add(toKey().getDisplayName().getString());
        return String.join(" + ", x);
    }

    /**
     * Checks if only the primary key is pressed, aka not a combo
     *
     * @return Whether the check succeeded
     */
    public boolean onlyKey() {
        return !(alt || shift || ctrl);
    }

    /**
     * @return Number of modifiers activated fo this key
     */
    public int modifierCount() {
        int x = alt ? 1 : 0;
        x += shift ? 1 : 0;
        x += ctrl ? 1 : 0;
        return x;
    }

    /**
     * @return Whether the primary key is a modifier
     */
    public boolean isModifier() {
        return KeymapRegistry.MODIFIER_KEYS.contains(keyCode);
    }

    /**
     * @return Whether the primary key is a modifier, and the only thing pressed
     *         FIXME: Might cause a bug where a key combo is just modifier keys, should not be allowed
     */
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
