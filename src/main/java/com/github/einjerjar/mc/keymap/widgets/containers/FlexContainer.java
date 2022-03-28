package com.github.einjerjar.mc.keymap.widgets.containers;

import com.github.einjerjar.mc.keymap.widgets.FlatContainer;
import com.github.einjerjar.mc.keymap.widgets.FlatWidget;
import net.minecraft.client.gui.Selectable;

import java.util.ArrayList;
import java.util.List;

public class FlexContainer extends FlatContainer {
    protected List<FlexChild> children = new ArrayList<>();
    protected int gap = 2;
    protected int padX = 4;
    protected int padY = 4;
    protected FlexDirection direction = FlexDirection.ROW;

    public FlexContainer setDirection(FlexDirection direction) {
        this.direction = direction;
        return this;
    }

    static class FlexChild {
        public FlatWidget<?> child;
        public float basis;

        public FlexChild(FlatWidget<?> child, float basis) {
            this.child = child;
            this.basis = basis;
        }
    }

    public enum FlexDirection {
        ROW,
        COLUMN
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

    public FlexContainer(Class<FlatContainer> self, int x, int y, int w, int h) {
        super(self, x, y, w, h);
        this.setDrawBg(false).setDrawBorder(false);
    }

    public FlexContainer addChild(FlatWidget<?> element, float basis) {
        children.add(new FlexChild(element, basis));
        return this;
    }

    public FlexContainer addChild(FlatWidget<?> element) {
        return addChild(element, 1f);
    }

    public FlexContainer arrange() {
        float maxBasis = 0;
        int fixedSize = 0;
        boolean isRow = direction == FlexDirection.ROW;
        for (FlexChild f : children) {
            if (f.basis < 0) {
                fixedSize += isRow ? f.child.getW() : f.child.getH();
            }
            maxBasis += f.basis;
        }
        int scaleRoot     = (isRow ? w : h) - fixedSize;
        int consumedSpace = 0;
        for (int i = 0; i < children.size(); i++) {
            FlexChild f = children.get(i);

            if (f.basis < 0) {
                if (isRow)
                    f.child.setH(h).setX(x + consumedSpace).setY(y);
                else
                    f.child.setW(w).setY(y + consumedSpace).setX(x);

                consumedSpace += isRow ? f.child.getW() : f.child.getH();
            } else {
                float basisRatio = f.basis / maxBasis;
                int   scaledSize = (int) ((scaleRoot - gap * children.size()) * basisRatio);

                if (scaledSize + consumedSpace > scaleRoot || i == children.size() - 1)
                    scaledSize = scaleRoot - consumedSpace;

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