package com.github.einjerjar.mc.keymap.widgets;

import org.jetbrains.annotations.NotNull;

public class SimplePoint implements Comparable<SimplePoint>{
    public int x;
    public int y;

    public SimplePoint(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public int compareTo(@NotNull SimplePoint o) {
        int ix = Integer.compare(x, o.x);
        int iy = Integer.compare(y, o.y);
        return ix != 0 ? ix : iy;
    }
}
