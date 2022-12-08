package com.github.einjerjar.mc.widgets2;

import com.github.einjerjar.mc.widgets.utils.Rect;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.GuiComponent;
import org.jetbrains.annotations.NotNull;

public class U {

    public enum Mouse {
        LEFT,
        RIGHT,
        MIDDLE,
    }

    public static void hl(@NotNull PoseStack ps, int l, int r, int t, int c) {
        if (l > r) {
            int x = l;
            l = r;
            r = x;
        }

        GuiComponent.fill(ps, l, t, r, t + 1, c);
    }

    public static void vl(@NotNull PoseStack ps, int t, int b, int l, int c) {
        if (t > b) {
            int x = t;
            t = b;
            b = x;
        }

        GuiComponent.fill(ps, l, t, l + 1, b, c);
    }

    public static void outline(@NotNull PoseStack ps, int l, int t, int r, int b, int c) {
        hl(ps, l, r + 1, t, c);
        hl(ps, l, r + 1, b, c);
        vl(ps, t + 1, b, l, c);
        vl(ps, t + 1, b, r, c);
    }

    public static void outline(@NotNull PoseStack ps, Rect r, int c) {
        outline(ps, r.left(), r.top(), r.right(), r.bottom(), c);
    }

    public static void bg(@NotNull PoseStack ps, int l, int t, int r, int b, int c) {
        GuiComponent.fill(ps, l, t, r, b, c);
    }

    public static void bg(@NotNull PoseStack ps, Rect r, int c) {
        bg(ps, r.left(), r.top(), r.right(), r.bottom(), c);
    }

    public static void bbg(@NotNull PoseStack ps, Rect r, int bg, int fg) {
        bg(ps, r, bg);
        outline(ps, r, fg);
    }

    public static void bbg(@NotNull PoseStack ps, Rect r, ColorOption bg, ColorOption fg, WidgetState s) {
        bbg(ps, r, bg.fromState(s), fg.fromState(s));
    }
}
