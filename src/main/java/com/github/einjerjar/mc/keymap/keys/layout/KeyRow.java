package com.github.einjerjar.mc.keymap.keys.layout;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.List;

@ToString
@AllArgsConstructor
@NoArgsConstructor
@Accessors(fluent = true, chain = true)
public class KeyRow {
    @Getter protected List<KeyData> row = new ArrayList<>();

    public void setRow(List<KeyData> row) {
        this.row = row;
    }
}