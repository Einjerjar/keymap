package com.github.einjerjar.mc.keymap.keys.layout;

import lombok.Getter;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.List;

@Getter
@ToString
@Accessors(fluent = true)
public class Keys {
    protected List<KeyRow> basic  = new ArrayList<>();
    protected List<KeyRow> mouse  = new ArrayList<>();
    protected List<KeyRow> extra  = new ArrayList<>();
    protected List<KeyRow> numpad = new ArrayList<>();
}