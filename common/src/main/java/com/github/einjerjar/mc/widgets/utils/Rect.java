package com.github.einjerjar.mc.widgets.utils;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.Objects;

@Accessors(fluent = true, chain = true)
public class Rect {
    @Getter
    @Setter
    int x;

    @Getter
    @Setter
    int y;

    @Getter
    @Setter
    int w;

    @Getter
    @Setter
    int h;

    public Rect(int x, int y, int w, int h) {
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Rect rect)) return false;
        return x == rect.x && y == rect.y && w == rect.w && h == rect.h;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y, w, h);
    }

    public int top() {
        return y;
    }

    public int bottom() {
        return y + h;
    }

    public int left() {
        return x;
    }

    public int right() {
        return x + w;
    }

    public void top(int t) {
        this.y = t;
    }

    public void bottom(int b) {
        this.h = b - this.y;
    }

    public void left(int l) {
        this.x = l;
    }

    public void right(int r) {
        this.w = r - this.x;
    }

    public int midX() {
        return (right() + left()) / 2;
    }

    public int midY() {
        return (bottom() + top()) / 2;
    }

    public boolean contains(double x, double y) {
        return x >= left() && x <= right() && y >= top() && y <= bottom();
    }

    public boolean contains(Point<Integer> p) {
        return contains(p.x(), p.y());
    }
}
