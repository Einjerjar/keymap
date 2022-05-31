package com.github.einjerjar.mc.widgets.utils;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.Accessors;

@ToString
@EqualsAndHashCode
@Accessors(fluent = true, chain = true)
public final class ColorGroup {
    @Getter private final ColorSet normal;
    @Getter private final ColorSet hover;
    @Getter private final ColorSet active;
    @Getter private final ColorSet disabled;

    public ColorGroup(ColorSet normal, ColorSet hover, ColorSet active, ColorSet disabled) {
        this.normal = normal;
        this.hover = hover;
        this.active = active;
        this.disabled = disabled;
    }

    public ColorGroup(int color) {
        this.normal = new ColorSet(color, ColorType.NORMAL);
        this.hover = new ColorSet(color, ColorType.HOVER);
        this.active = new ColorSet(color, ColorType.ACTIVE);
        this.disabled = new ColorSet(color, ColorType.DISABLED);
    }

    public ColorSet getVariant(boolean hover, boolean active, boolean disabled) {
        if (disabled) return this.disabled;
        if (active) return this.active;
        if (hover) return this.hover;
        return this.normal;
    }
}