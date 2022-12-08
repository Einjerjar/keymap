package com.github.einjerjar.mc.widgets2;

import com.github.einjerjar.mc.widgets.utils.Point;
import com.github.einjerjar.mc.widgets.utils.Rect;
import com.mojang.blaze3d.vertex.PoseStack;
import lombok.Getter;
import lombok.experimental.Accessors;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

@Accessors(fluent = true)
public abstract class EScreen2Utils extends Screen {
    @Getter protected final Point<Integer> margin  = new Point<>(6);
    @Getter protected final Point<Integer> padding = new Point<>(6);

    @Getter protected int targetScreenWidth = -1;
    @Getter protected int minScreenWidth    = 10;

    @Getter protected List<EWidget2> children = new ArrayList<>();

    // Last active
    private EWidget2 focusWidget;
    // Mouse over
    private EWidget2 hoverWidget;
    // Mouse clicking
    private EWidget2 activeWidget;

    @Getter protected Rect scr;

    @Getter protected Screen parent;

    @Nullable protected EWidget2 focusWidget() {
        if (focusWidget != null) {
            if (focusWidget.focused) return focusWidget;
            focusWidget(null);
        }
        return null;
    }

    @Nullable protected EWidget2 hoverWidget() {
        if (hoverWidget != null) {
            if (hoverWidget.hovered) return hoverWidget;
            hoverWidget(null);
        }
        return null;
    }

    @Nullable protected EWidget2 activeWidget() {
        if (activeWidget != null) {
            if (activeWidget.active) return activeWidget;
            activeWidget(null);
        }
        return null;
    }

    protected void focusWidget(EWidget2 w) {
        if (focusWidget != w && focusWidget != null)
            focusWidget.focused(false);
        focusWidget = w;
        if (w != null) w.focused(true);
    }

    protected void hoverWidget(EWidget2 w) {
        if (hoverWidget != w && hoverWidget != null)
            hoverWidget.hovered(false);
        hoverWidget = w;
        if (hoverWidget != null) {
            hoverWidget.hovered(true);
        }
    }

    protected void activeWidget(EWidget2 w) {
        if (activeWidget != w && activeWidget != null)
            activeWidget.active(false);
        activeWidget = w;
        if (w != null) w.active(true);
    }

    /**
     * If the user has previously mouseClicked on this screen
     * prevents mouseReleased from other screen to pass through
     */
    protected boolean clickState = false;

    protected EScreen2Utils(Component component) {
        super(component);
    }

    protected void drawOutline(@NotNull PoseStack ps, int l, int t, int r, int b, int c) {
        U.outline(ps, l, t, r, b, c);
    }

    protected void drawOutline(@NotNull PoseStack ps, Rect r, int c) {
        drawOutline(ps, r.left(), r.top(), r.right(), r.bottom(), c);
    }

    protected void drawOutline(@NotNull PoseStack ps, int c) {
        drawOutline(ps, scr, c);
    }

    protected void drawOutsideOutline(@NotNull PoseStack ps, Rect r, int c) {
        drawOutline(ps, r.left() - 1, r.top() - 1, r.right() - 1, r.bottom() - 1, c);
    }

    protected void drawOutsideOutline(@NotNull PoseStack ps, int c) {
        drawOutsideOutline(ps, scr, c);
    }

    protected Rect scrFromWidth(int w) {
        if (w == -1) w = width;
        w = Math.max(Math.min(w, width - margin.x() * 2), minScreenWidth);
        return new Rect(
                Math.max((width - w) / 2, 0) + margin.x(),
                margin.y(),
                w - margin.x() * 2,
                height - margin.y() * 2
        );
    }

    protected Point<Integer> center() {
        return new Point<>(
                (scr.left() + scr.right()) / 2,
                (scr.top() + scr.bottom()) / 2
        );
    }
}
