package com.github.einjerjar.mc.keymap.widgets;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.Drawable;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.Element;
import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;

@SuppressWarnings("UnusedReturnValue")
@Accessors(fluent = true, chain = true)
public abstract class FlatWidgetBase extends DrawableHelper implements Element, Drawable {
    @Getter @Setter protected boolean hovered = false;
    @Getter @Setter protected boolean focused = false;
    @Getter @Setter protected boolean visible = true;
    @Getter @Setter protected boolean enabled = true;

    @Setter protected boolean drawBg = false;
    @Setter protected boolean drawBorder = false;
    @Setter protected boolean drawShadow = false;

    @Getter @Setter protected int x;
    @Getter @Setter protected int y;
    @Getter @Setter protected int w;
    @Getter @Setter protected int h;

    protected TextRenderer tr;

    public FlatWidgetBase(int x, int y, int w, int h) {
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
        this.tr = MinecraftClient.getInstance().textRenderer;
    }

    public void playSound(PositionedSoundInstance p) {
        MinecraftClient.getInstance().getSoundManager().play(p);
    }

    public void playSound(SoundEvent sound, float pitch) {
        playSound(PositionedSoundInstance.master(sound, pitch));
    }

    public void playClickSound() {
        playSound(SoundEvents.UI_BUTTON_CLICK, 1.0F);
    }

    public void updateSize() {
    }

    public boolean active() {
        return enabled && visible;
    }

    public FlatWidgetBase active(boolean active) {
        this.enabled = active;
        this.visible = active;
        return this;
    }

    public interface CommonAction {
        void run(FlatWidgetBase w);
    }
}
