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
public class Keys {
    @Getter protected List<KeyRow> basic = new ArrayList<>();
    @Getter protected List<KeyRow> mouse = new ArrayList<>();
    @Getter protected List<KeyRow> extra = new ArrayList<>();
    @Getter protected List<KeyRow> numpad = new ArrayList<>();

    public void setBasic(List<KeyRow> basic) {
        this.basic = basic;
    }

    public void setMouse(List<KeyRow> mouse) {
        this.mouse = mouse;
    }

    public void setExtra(List<KeyRow> extra) {
        this.extra = extra;
    }

    public void setNumpad(List<KeyRow> numpad) {
        this.numpad = numpad;
    }
}