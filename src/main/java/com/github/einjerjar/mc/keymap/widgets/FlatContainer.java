package com.github.einjerjar.mc.keymap.widgets;

import com.github.einjerjar.mc.keymap.screen.Tooltipped;
import com.github.einjerjar.mc.keymap.utils.Utils;
import com.github.einjerjar.mc.keymap.utils.WidgetUtils;
import com.mojang.blaze3d.systems.RenderSystem;
import lombok.Getter;
import lombok.experimental.Accessors;
import net.minecraft.client.gui.Drawable;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.Selectable;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("UnusedReturnValue")
@Accessors(fluent = true, chain = true)
public abstract class FlatContainer extends FlatWidget<FlatContainer> implements Selectable, Tooltipped {
    protected final List<Element> childStack = new ArrayList<>();
    protected final List<Selectable> childSelectable = new ArrayList<>();
    protected final List<Drawable> childDrawable = new ArrayList<>();

    @Getter protected Element focusedElement;
    @Getter protected Element hoveredElement;

    public FlatContainer(int x, int y, int w, int h) {
        super(FlatContainer.class, x, y, w, h);
        this.drawBg(true)
            .drawBorder(true);
    }

    @Override
    public SelectionType getType() {
        if (focused) return SelectionType.FOCUSED;
        if (hovered) return SelectionType.HOVERED;
        return SelectionType.NONE;
    }

    @Override
    public List<Text> getToolTips() {
        if (hoveredElement != null && hoveredElement instanceof Tooltipped tipped) return tipped.getToolTips();
        return new ArrayList<>();
    }

    @Override
    public Text getFirstToolTip() {
        return Utils.safeGet(getToolTips(), 0, new LiteralText(""));
    }

    public <T extends Element & Drawable> FlatContainer addDrawable(T element) {
        if (!childDrawable.contains(element)) childDrawable.add(element);
        if (!childStack.contains(element)) childStack.add(element);
        return self;
    }

    public <T extends Element & Selectable> FlatContainer addSelectable(T element) {
        if (!childSelectable.contains(element)) childSelectable.add(element);
        if (!childStack.contains(element)) childStack.add(element);
        return self;
    }

    public FlatContainer removeElement(Element e) {
        childStack.remove(e);
        if (e instanceof Drawable) childDrawable.remove(e);
        if (e instanceof Selectable) childSelectable.remove(e);
        return self;
    }

    @Override
    public void renderWidget(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        if (drawBg) WidgetUtils.fillBox(this, matrices, x, y, w, h, color.bg.normal);
        if (drawBorder) WidgetUtils.drawBoxOutline(this, matrices, x, y, w, h, color.border.normal);

        RenderSystem.enableBlend();

        for (Selectable e : childSelectable) {
            if (((Element) e).isMouseOver(mouseX, mouseY)) {
                hoveredElement = (Element) e;
                break;
            }
        }

        for (Element e : childStack) {
            if (e instanceof Drawable) {
                ((Drawable) e).render(matrices, mouseX, mouseY, delta);
            }
        }

        RenderSystem.disableBlend();
    }

    @Override
    public void mouseMoved(double mouseX, double mouseY) {
        for (Element e : childStack) {
            e.mouseMoved(mouseX, mouseY);
        }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (hoveredElement != null) {
            if (hoveredElement.mouseClicked(mouseX, mouseY, button)) {
                focusedElement = hoveredElement;
                return true;
            }
        }
        focusedElement = null;
        return hovered;
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        if (focusedElement != null && hoveredElement != focusedElement)
            focusedElement.mouseReleased(mouseX, mouseY, button);
        if (hoveredElement != null)
            return hoveredElement.mouseReleased(mouseX, mouseY, button);

        return false;
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        if (focusedElement != null) {
            return focusedElement.mouseDragged(mouseX, mouseY, button, deltaX, deltaY);
        }

        return false;
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double amount) {
        if (hoveredElement != null) {
            return hoveredElement.mouseScrolled(mouseX, mouseY, amount);
        }
        return false;
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (focusedElement != null) {
            return focusedElement.keyPressed(keyCode, scanCode, modifiers);
        }
        return false;
    }

    @Override
    public boolean keyReleased(int keyCode, int scanCode, int modifiers) {
        if (focusedElement != null)
            return focusedElement.keyReleased(keyCode, scanCode, modifiers);
        return false;
    }

    @Override
    public boolean charTyped(char chr, int modifiers) {
        return super.charTyped(chr, modifiers);
    }

    @Override
    public boolean changeFocus(boolean lookForwards) {
        if (focusedElement != null) {
            if (focusedElement.changeFocus(lookForwards)) {
                return true;
            }
            int add        = lookForwards ? 1 : -1;
            int focusIndex = childStack.indexOf(focusedElement);
            int nextIndex  = focusIndex + add;
            if (nextIndex >= childStack.size()) nextIndex -= childStack.size();
            if (nextIndex < 0) nextIndex += childStack.size();
            focusedElement = childStack.get(nextIndex);
            return true;
        }
        if (childStack.size() > 0) {
            focusedElement = childStack.get(0);
            return true;
        }
        return false;
    }

    @Override
    public void appendNarrations(NarrationMessageBuilder builder) {
    }
}
