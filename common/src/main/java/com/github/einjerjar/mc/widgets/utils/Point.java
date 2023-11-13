package com.github.einjerjar.mc.widgets.utils;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.Objects;

@Accessors(fluent = true, chain = true)
public class Point<T> {
    @Getter
    @Setter
    T x;

    @Getter
    @Setter
    T y;

    public Point(T x, T y) {
        this.x = x;
        this.y = y;
    }

    public Point(T xy) {
        this.x = xy;
        this.y = xy;
    }

    public void setXY(T x, T y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Point<?> point = (Point<?>) o;
        return Objects.equals(x, point.x) && Objects.equals(y, point.y);
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }
}
