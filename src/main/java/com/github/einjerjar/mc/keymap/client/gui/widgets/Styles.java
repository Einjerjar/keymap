package com.github.einjerjar.mc.keymap.client.gui.widgets;

import lombok.Getter;
import lombok.experimental.Accessors;
import net.minecraft.network.chat.Style;

@Accessors(fluent = true, chain = true)
public class Styles {
    private Styles() {}

    @Getter protected static final Style header = Style.EMPTY
            .withBold(true)
            .withColor(0x00ff00)
            .withItalic(true);

    @Getter protected static final Style muted = Style.EMPTY
            .withColor(0x555555);

    @Getter protected static final Style muted2 = Style.EMPTY
            .withColor(0x888888);

    @Getter protected static final Style normal = Style.EMPTY;

    @Getter protected static final Style red = Style.EMPTY
            .withColor(0xff0000);

    @Getter protected static final Style green = Style.EMPTY
            .withColor(0x00ff00);

    @Getter protected static final Style blue = Style.EMPTY
            .withColor(0x0000ff);

    @Getter protected static final Style yellow = Style.EMPTY
            .withColor(0xffff00);

    @Getter protected static final Style cyan = Style.EMPTY
            .withColor(0x00ffff);

    @Getter protected static final Style purple = Style.EMPTY
            .withColor(0xff00ff);
}
