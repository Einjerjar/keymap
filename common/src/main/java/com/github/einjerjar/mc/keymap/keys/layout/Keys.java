package com.github.einjerjar.mc.keymap.keys.layout;

import lombok.Getter;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.List;

/**
 * Key groups
 */
@Getter
@ToString
@Accessors(fluent = true)
public class Keys {
    /**
     * Contains keys available on most baords
     */
    protected List<KeyRow> basic  = new ArrayList<>();
    /**
     * Contains the basic mouse keys and special handler for extra keys
     */
    protected List<KeyRow> mouse  = new ArrayList<>();
    /**
     * Contains the insert/home/pgup etc keys
     */
    protected List<KeyRow> extra  = new ArrayList<>();
    /**
     * Self-explanatory :3
     */
    protected List<KeyRow> numpad = new ArrayList<>();
}