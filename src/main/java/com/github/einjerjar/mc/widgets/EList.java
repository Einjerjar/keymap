package com.github.einjerjar.mc.widgets;

import com.github.einjerjar.mc.keymap.config.KeymapConfig;
import com.github.einjerjar.mc.widgets.utils.*;
import com.mojang.blaze3d.vertex.PoseStack;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

@Accessors(fluent = true, chain = true)
public abstract class EList<T extends EList.EListEntry<T>> extends EWidget {
    @Getter protected boolean   dragging;
    @Getter protected int       itemHeight;
    @Getter protected int       scrollBarWidth = 6;
    @Getter protected int       scrollSpeed    = 8;
    @Getter protected List<T>   items          = new ArrayList<>();
    @Getter protected T         itemHovered;
    @Getter protected T         itemSelected;
    @Getter protected T         lastItemSelected;
    protected         Minecraft client;

    protected double        scrollOffset       = 0;
    protected double        lastDrag;
    protected double        lastScrollPos;
    protected boolean       canDeselectItem    = true;
    protected boolean       lastClickWasInside = false;
    protected boolean       didDrag            = false;
    protected Point<Double> lastClick          = new Point<>(0d);

    @Getter @Setter protected Point<Integer> padding    = new Point<>(4);
    @Getter @Setter protected boolean        drawBorder = true;
    @Getter @Setter protected boolean        drawBg     = true;

    @Getter @Setter ColorGroup color = ColorGroups.WHITE;

    @Setter SimpleAction<EList<T>> onItemSelected;


    // region Constructor
    protected EList(int itemHeight, int x, int y, int w, int h) {
        super(x, y, w, h);
        _init(itemHeight);
    }

    protected EList(int itemHeight, Rect rect) {
        super(rect);
        _init(itemHeight);
    }
    // endregion

    // region Helpers


    // FIXME: Redundant code
    protected void setItemSelected(T t) {
        if (itemSelected != null) {
            itemSelected.selected(false);
        }
        itemSelected = t;
        if (t != null) {
            itemSelected.selected(true);
        }
    }

    protected void setLastItemSelected(T t) {
        if (lastItemSelected != null) {
            lastItemSelected.selected(false);
        }
        lastItemSelected = t;
        if (t != null) {
            lastItemSelected.selected(true);
        }
    }

    public void addItem(T item) {
        if (items.contains(item)) return;
        items.add(item);
    }

    public void removeItem(T item) {
        items.remove(item);
    }

    public void clearItems() {
        items.clear();
    }

    public int size() {
        return items.size();
    }

    protected int scrollBarX() {
        return right() - scrollBarWidth;
    }

    protected T getHoveredItem(double mouseX, double mouseY) {
        int x = (int) mouseX;
        int y = (int) mouseY;

        if (!(x >= left() + padding.x() && x <= right() - padding.x())) return null;
        y -= top() + padding.y() - scrollOffset;
        int ix = y / itemHeight;
        if (ix < 0 || ix >= size()) return null;
        return items.get(ix);
    }

    protected int contentHeight() {
        return size() * itemHeight;
    }

    protected double maxScroll() {
        return Math.max(0, contentHeight() - (rect.h() - padding.y() * 2));
    }

    protected boolean inScrollbar(double mouseX, double mouseY) {
        return mouseY >= top() + padding.y() && mouseY <= bottom() - padding.y() &&
               mouseX >= scrollBarX() && mouseX <= scrollBarWidth + scrollBarX();
    }

    public void setScrollPos(double pos) {
        scrollOffset = WidgetUtils.clamp(pos, 0, maxScroll());
    }

    public void relativeScrollPos(double pos) {
        scrollOffset = WidgetUtils.clamp(scrollOffset + pos, 0, maxScroll());
    }

    // endregion

    protected void _init(int itemHeight) {
        this.client     = Minecraft.getInstance();
        this.itemHeight = itemHeight;
    }

    // region Render
    @Override protected void renderWidget(PoseStack poseStack, int mouseX, int mouseY, float partialTick) {
        if (KeymapConfig.instance().debug()) {
            drawOutline(poseStack, 0xff_ff0000);
        }
        renderList(poseStack, mouseX, mouseY, partialTick);
    }

    protected void renderList(PoseStack poseStack, int mouseX, int mouseY, float partialTick) {
        if (itemHovered != null) itemHovered.hovered(false);
        itemHovered = getHoveredItem(mouseX, mouseY);
        if (itemHovered != null) itemHovered.hovered(true);

        for (int i = 0; i < size(); i++) {
            T e = items.get(i);
            Rect r = new Rect(left() + padding.x(),
                    top() + i * itemHeight + padding.y() - ((int) scrollOffset),
                    rect.w() - padding.x() * 2,
                    itemHeight);

            if (r.midY() > rect.bottom() - padding.y() || r.midY() < rect.top() + padding.y()) continue;
            e.render(poseStack, r, partialTick);
        }
    }

    @Nullable
    @Override public List<Component> getTooltips() {
        if (itemHovered != null) return itemHovered.getTooltips();
        return null;
    }

    protected void sort() {
    }

    // endregion

    @Override public boolean onMouseReleased(boolean inside, double mouseX, double mouseY, int button) {
        setLastItemSelected(null);
        if (didDrag) {
            didDrag = false;
            return false;
        }
        itemHovered = getHoveredItem(mouseX, mouseY);
        if (itemHovered == null && itemSelected != null && canDeselectItem) {
            setLastItemSelected(itemSelected);
            setItemSelected(null);
            return false;
        }

        if (!inside) return false;
        // ghost hover is a pain to deal with, so just refresh everything
        for (T item : items) {
            item.selected(false);
            item.hovered(false);
        }
        setItemSelected(itemHovered);
        if (itemSelected != null) {
            itemSelected.selected(true);
            if (onItemSelected != null) onItemSelected.run(this);
        }
        return true;
    }

    @Override protected boolean onMouseScrolled(double mouseX, double mouseY, double delta) {
        relativeScrollPos(-delta * scrollSpeed);
        return true;
    }

    @Override public boolean onMouseClicked(boolean inside, double mouseX, double mouseY, int button) {
        lastClickWasInside = inside;
        lastClick.setXY(mouseX, mouseY);
        lastScrollPos = scrollOffset;
        if (!inside) {
            onMouseReleased(inside, mouseX, mouseY, button);
        }
        return false;
    }

    @Override protected boolean onMouseDragged(double mouseX, double mouseY, int button, double dragX, double dragY) {
        if (!lastClickWasInside) return false;
        didDrag = true;
        setScrollPos(lastScrollPos - (mouseY - lastClick.y()));
        return true;
    }

    @Accessors(fluent = true, chain = true)
    public abstract static class EListEntry<T extends EListEntry<T>> extends GuiComponent implements Tooltipped {
        @Getter @Setter protected boolean         selected = false;
        @Getter @Setter protected boolean         hovered  = false;
        @Getter protected         EList<T>        container;
        protected                 ColorGroup      color    = new ColorGroup(
                new ColorSet(0xffffff, ColorType.NORMAL),
                new ColorSet(0xff3333, ColorType.HOVER),
                new ColorSet(0x00ff00, ColorType.ACTIVE),
                new ColorSet(0xffffff, ColorType.DISABLED));
        protected                 Font            font;
        protected                 List<Component> tooltips = new ArrayList<>();

        protected EListEntry(EList<T> container) {
            font           = Minecraft.getInstance().font;
            this.container = container;
        }

        protected ColorSet getVariant() {
            if (selected) return color.active();
            if (hovered) return color.hover();
            return color.normal();
        }

        @Override public List<Component> getTooltips() {
            return tooltips;
        }

        public void render(@NotNull PoseStack poseStack, Rect r, float partialTick) {
            renderWidget(poseStack, r, partialTick);
        }

        public void updateTooltips() {
        }

        public abstract void renderWidget(@NotNull PoseStack poseStack, Rect r, float partialTick);
    }
}
