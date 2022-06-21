package com.github.einjerjar.mc.keymap.keys.layout;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.Accessors;

@Getter
@ToString
@AllArgsConstructor
@Accessors(fluent = true)
public class KeyMeta {
    protected String author;
    protected String name;
    protected String code;
}
