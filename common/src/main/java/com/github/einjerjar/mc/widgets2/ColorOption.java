package com.github.einjerjar.mc.widgets2;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Builder
@Accessors(fluent = true)
public class ColorOption {
    public static final ColorOption baseBG = ColorOption.builder().build();
    public static final ColorOption baseFG = ColorOption.builder()
            .transparencyBase(0xFF << 24)
            .transparencyFocus(0xFF << 24)
            .transparencyHover(0xFF << 24)
            .transparencyActive(0xFF << 24)
            .transparencyDisabled(0xFF << 24)
            .build();

    @Builder.Default
    @Getter @Setter protected int color                = 0xFFFFFF;
    @Builder.Default
    @Getter @Setter protected int transparencyBase     = 0x00_000000;
    @Builder.Default
    @Getter @Setter protected int transparencyHover    = 0x33_000000;
    @Builder.Default
    @Getter @Setter protected int transparencyActive   = 0x55_000000;
    @Builder.Default
    @Getter @Setter protected int transparencyFocus    = 0x11_000000;
    @Builder.Default
    @Getter @Setter protected int transparencyDisabled = 0x00_000000;

    public int base() {
        return transparencyBase | color;
    }

    public int hover() {
        return transparencyHover | color;
    }

    public int active() {
        return transparencyActive | color;
    }

    public int focus() {
        return transparencyFocus | color;
    }

    public int disabled() {
        return transparencyDisabled | color;
    }

    public int fromState(WidgetState s) {
        switch (s) {
            case DISABLED -> {
                return disabled();
            }
            case ACTIVE -> {
                return active();
            }
            case HOVER -> {
                return hover();
            }
            case FOCUS -> {
                return focus();
            }
        }
        return base();
    }
}