package com.github.einjerjar.mc.keymap.utils;

import lombok.ToString;

@ToString
public class SimpleRect {
    public int top;
    public int left;
    public int right;
    public int bottom;
    public int x;
    public int y;
    public int w;
    public int h;

    public SimpleRect(int x, int y, int w, int h) {
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;

        this.top = y;
        this.left = x;
        this.right = x + w;
        this.bottom = y + h;
    }
}