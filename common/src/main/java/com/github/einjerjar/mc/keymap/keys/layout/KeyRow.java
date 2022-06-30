package com.github.einjerjar.mc.keymap.keys.layout;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.util.List;

@Getter
@ToString
@AllArgsConstructor
@Accessors(fluent = true)
public class KeyRow {
    protected List<KeyData> row;
}