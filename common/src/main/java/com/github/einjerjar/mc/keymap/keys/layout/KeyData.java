package com.github.einjerjar.mc.keymap.keys.layout;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * A data container for each key
 */
@Getter
@AllArgsConstructor
@ToString
@Accessors(fluent = true)
public class KeyData {
    /**
     * The keycode
     */
    protected int code;
    /**
     * The text to display on the virtual key
     */
    protected String name;
    /**
     * Whether this key can be assigned (eg; super/windows keys should be disabled)
     */
    protected boolean enabled;
    /**
     * Whether the key is a mouse key
     */
    @Setter
    protected boolean mouse;
    /**
     * Extra width for the key (can be negative)
     */
    protected int width;
    /**
     * Extra height for the key (can be negative)
     */
    protected int height;
}
