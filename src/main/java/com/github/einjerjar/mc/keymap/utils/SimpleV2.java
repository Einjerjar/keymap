package com.github.einjerjar.mc.keymap.utils;


import lombok.ToString;

@ToString
public class SimpleV2 {
    public int x;
    public int y;

    public SimpleV2(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public SimpleV2(int xy) {
        this.x = xy;
        this.y = xy;
    }
}