package com.github.einjerjar.mc.widgets2;

import com.github.einjerjar.mc.widgets.utils.Rect;
import net.minecraft.client.gui.GuiGraphics;
import org.jetbrains.annotations.NotNull;

public class U {

    public enum Mouse {
        LEFT,
        RIGHT,
        MIDDLE,
    }

    public static void hl(@NotNull GuiGraphics guiGraphics, int l, int r, int t, int c) {
        if (l > r) {
            int x = l;
            l = r;
            r = x;
        }

        guiGraphics.fill(l, t, r, t + 1, c);
    }

    public static void vl(@NotNull GuiGraphics guiGraphics, int t, int b, int l, int c) {
        if (t > b) {
            int x = t;
            t = b;
            b = x;
        }

        guiGraphics.fill(l, t, l + 1, b, c);
    }

    public static void outline(@NotNull GuiGraphics guiGraphics, int l, int t, int r, int b, int c) {
        hl(guiGraphics, l, r + 1, t, c);
        hl(guiGraphics, l, r + 1, b, c);
        vl(guiGraphics, t + 1, b, l, c);
        vl(guiGraphics, t + 1, b, r, c);
    }

    public static void outline(@NotNull GuiGraphics guiGraphics, Rect r, int c) {
        outline(guiGraphics, r.left(), r.top(), r.right(), r.bottom(), c);
    }

    public static void bg(@NotNull GuiGraphics guiGraphics, int l, int t, int r, int b, int c) {
        guiGraphics.fill(l, t, r, b, c);
    }

    public static void bg(@NotNull GuiGraphics guiGraphics, Rect r, int c) {
        bg(guiGraphics, r.left(), r.top(), r.right(), r.bottom(), c);
    }

    public static void bbg(@NotNull GuiGraphics guiGraphics, Rect r, int bg, int fg) {
        bg(guiGraphics, r, bg);
        outline(guiGraphics, r, fg);
    }

    public static void bbg(@NotNull GuiGraphics guiGraphics, Rect r, ColorOption bg, ColorOption fg, WidgetState s) {
        bbg(guiGraphics, r, bg.fromState(s), fg.fromState(s));
    }
}
