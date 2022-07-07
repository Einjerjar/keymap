package com.github.einjerjar.mc.keymap.keys.layout;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * Metadata for a KeyLayout
 */
@Getter
@ToString
@AllArgsConstructor
@Accessors(fluent = true)
public class KeyMeta {
    /**
     * The contributor of the layout, empty if Einjerjar
     */
    protected String author;
    /**
     * The name to display for this layout (eg; English (US))
     */
    protected String name;
    /**
     * The code for this layout (eg; en_us)
     */
    protected String code;
}
