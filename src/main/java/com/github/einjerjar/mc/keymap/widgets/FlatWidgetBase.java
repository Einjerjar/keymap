package com.github.einjerjar.mc.keymap.widgets;

import net.minecraft.client.gui.Drawable;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.Element;

public abstract class FlatWidgetBase extends DrawableHelper implements Element, Drawable {
    public boolean enabled = true;
}
