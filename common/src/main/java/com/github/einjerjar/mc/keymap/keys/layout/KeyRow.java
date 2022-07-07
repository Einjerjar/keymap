package com.github.einjerjar.mc.keymap.keys.layout;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * A row of keys
 * TODO: Contemplate life, or rather, why is this a thing
 * TODO: No, seriously, why (actually, bec of how I designed the original yaml, but eh)
 */
@Getter
@ToString
@AllArgsConstructor
@Accessors(fluent = true)
public class KeyRow {
    /**
     * Not a column
     */
    protected List<KeyData> row;
}