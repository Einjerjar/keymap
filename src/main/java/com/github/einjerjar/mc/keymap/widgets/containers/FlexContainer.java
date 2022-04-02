package com.github.einjerjar.mc.keymap.widgets.containers;

import com.github.einjerjar.mc.keymap.screen.Tooltipped;
import com.github.einjerjar.mc.keymap.utils.Utils;
import com.github.einjerjar.mc.keymap.widgets.FlatContainer;
import com.github.einjerjar.mc.keymap.widgets.FlatWidgetBase;
import net.minecraft.client.gui.Selectable;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.List;

public class FlexContainer extends FlatContainer implements Tooltipped {
    protected List<FlexChild> children = new ArrayList<>();
    protected int gap = 2;
    protected int padX = 4;
    protected int padY = 4;
    protected FlexDirection direction = FlexDirection.ROW;

    protected FlatWidgetBase focused;

    public FlexContainer setDirection(FlexDirection direction) {
        this.direction = direction;
        return this;
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

    static class FlexChild {
        public FlatWidgetBase child;
        public float basis;

        public FlexChild(FlatWidgetBase child, float basis) {
            this.child = child;
            this.basis = basis;
        }
    }

    public enum FlexDirection {
        ROW,
        COLUMN
    }

    public FlexContainer setGap(int gap) {
        this.gap = gap;
        return this;
    }

    public FlexContainer setPadX(int padX) {
        this.padX = padX;
        return this;
    }

    public FlexContainer setPadY(int padY) {
        this.padY = padY;
        return this;
    }

    public FlexContainer(int x, int y, int w, int h) {
        super(x, y, w, h);
        this.setDrawBg(false).setDrawBorder(false);
    }

    public FlexContainer addChild(FlatWidgetBase element, float basis) {
        children.add(new FlexChild(element, basis));
        return this;
    }

    public FlexContainer addChild(FlatWidgetBase element) {
        return addChild(element, 1f);
    }

    public FlexContainer arrange() {
        float   maxBasis  = 0;
        int     fixedSize = 0;
        boolean isRow     = direction == FlexDirection.ROW;

        for (FlexChild f : children) {
            if (f.basis <= 0) {
                fixedSize += isRow ? f.child.getW() : f.child.getH();
                continue;
            }
            maxBasis += f.basis;
        }

        int scaleRoot     = (isRow ? w : h) - fixedSize;
        int consumedSpace = 0;

        for (int i = 0; i < children.size(); i++) {
            FlexChild f = children.get(i);

            if (f.basis <= 0) {
                if (isRow)
                    f.child.setH(h).setX(x + consumedSpace).setY(y);
                else
                    f.child.setW(w).setY(y + consumedSpace).setX(x);

                consumedSpace += isRow ? f.child.getW() : f.child.getH() + gap;
            } else {
                float basisRatio = f.basis / maxBasis;
                int   scaledSize = (int) ((scaleRoot - gap * children.size()) * basisRatio);

                if (isRow)
                    f.child.setW(scaledSize).setH(h).setX(x + consumedSpace).setY(y);
                else
                    f.child.setH(scaledSize).setW(w).setY(y + consumedSpace).setX(x);

                consumedSpace += scaledSize + gap;
            }

            if (f.child instanceof Selectable)
                if (!childSelectable.contains(f.child)) childSelectable.add((Selectable) f.child);
                else if (!childDrawable.contains(f.child)) childDrawable.add(f.child);
            if (!childStack.contains(f.child)) childStack.add(f.child);
        }
        return this;
    }
}
