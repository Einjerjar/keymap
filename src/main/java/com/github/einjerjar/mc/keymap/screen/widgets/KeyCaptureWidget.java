package com.github.einjerjar.mc.keymap.screen.widgets;

import com.github.einjerjar.mc.keymap.utils.ColorGroup;
import com.github.einjerjar.mc.keymap.utils.Utils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.Selectable;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.List;

public class KeyCaptureWidget extends ClickableWidget {
    TextRenderer tr;
    FlatTextWidget captureLabel;
    FlatTextWidget hotkeyLabel;
    FlatButtonWidget btnOk;
    FlatButtonWidget btnCancel;
    FlatButtonWidget btnReset;

    Element lastClickedElement;
    Element hoveredElement;

    List<Element> selectables = new ArrayList<>();

    public KeyCaptureWidget(int x, int y, int width, int height, Text text) {
        super(x, y, width, height, null);
        this.tr = MinecraftClient.getInstance().textRenderer;

        this.captureLabel = FlatTextWidget.builder(width / 2, height / 2 - tr.fontHeight * 2, text);
        this.hotkeyLabel = FlatTextWidget.builder(width / 2, height / 2, text);
        this.btnOk = new FlatButtonWidget(width / 2 - 50 - 5, height / 2 + tr.fontHeight * 2, 50, 16, new LiteralText("ok"), null);
        this.btnReset = new FlatButtonWidget(width / 2 - 50 - 5, height / 2 + tr.fontHeight * 2, 50, 16, new LiteralText("ok"), null);
        this.btnCancel = new FlatButtonWidget(width / 2 + 5, height / 2 + tr.fontHeight * 2, 50, 16, new LiteralText("cancel"), null);

        selectables.add(btnOk);
        selectables.add(btnReset);
        selectables.add(btnCancel);
    }

    @Override
    public void render(MatrixStack m, int mouseX, int mouseY, float delta) {
        hoveredElement = null;
        for (Element e : selectables) {
            if (e.isMouseOver(mouseX, mouseY)) {
                hoveredElement = e;
                break;
            }
        }

        Utils.fillBox(this, m, x, y, width, height, ColorGroup.NORMAL.bg.normal);
        captureLabel.render(m, mouseX, mouseY, delta);
        hotkeyLabel.render(m, mouseX, mouseY, delta);
        btnOk.render(m, mouseX, mouseY, delta);
        btnCancel.render(m, mouseX, mouseY, delta);
    }

    @Override
    public void appendNarrations(NarrationMessageBuilder builder) {}

    @Override
    public void mouseMoved(double mouseX, double mouseY) {
        super.mouseMoved(mouseX, mouseY);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double amount) {
        return super.mouseScrolled(mouseX, mouseY, amount);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public boolean keyReleased(int keyCode, int scanCode, int modifiers) {
        return super.keyReleased(keyCode, scanCode, modifiers);
    }

    @Override
    public boolean charTyped(char chr, int modifiers) {
        return super.charTyped(chr, modifiers);
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        if (lastClickedElement != null && hoveredElement != lastClickedElement) {
            lastClickedElement.mouseReleased(mouseX, mouseY, button);
        }
        if (hoveredElement != null) return hoveredElement.mouseReleased(mouseX, mouseY, button);
        return super.mouseReleased(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (this.active && this.visible) {
            if (this.isValidClickButton(button)) {
                boolean bl = this.clicked(mouseX, mouseY);
                if (bl) {
                    for (Element e : selectables) {
                        if (e.mouseClicked(mouseX, mouseY, button)) {
                            lastClickedElement = e;
                            return true;
                        }
                    }
                    return true;
                }
            }

        }
        return false;
    }
}
