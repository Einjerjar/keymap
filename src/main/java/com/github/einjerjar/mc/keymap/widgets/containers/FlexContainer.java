package com.github.einjerjar.mc.keymap.widgets.containers;

import com.github.einjerjar.mc.keymap.screen.Tooltipped;
import com.github.einjerjar.mc.keymap.utils.Utils;
import com.github.einjerjar.mc.keymap.widgets.FlatContainer;
import com.github.einjerjar.mc.keymap.widgets.FlatWidgetBase;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import net.minecraft.client.gui.Selectable;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.List;

@Accessors(fluent = true, chain = true)
public class FlexContainer extends FlatContainer {
    @Getter @Setter protected int gap = 2;
    @Getter @Setter protected int padX = 4;
    @Getter @Setter protected int padY = 4;
    @Getter @Setter protected FlexDirection direction = FlexDirection.ROW;

    protected List<FlexChild> children = new ArrayList<>();

    public FlexContainer(int x, int y, int w, int h) {
        super(x, y, w, h);
        this.drawBg(false)
            .drawBorder(false);
    }

    public FlexContainer addChild(FlatWidgetBase element, float basis) {
        children.add(new FlexChild(element, basis));
        return this;
    }

    public FlexContainer addChild(FlatWidgetBase element) {
        return addChild(element, 1f);
    }

    public void arrange() {
        // TODO: random voodoo that i came up on the fly, needs refactoring (probably)
        float   maxBasis  = 0;
        int     fixedSize = 0;
        boolean isRow     = direction == FlexDirection.ROW;

        for (FlexChild f : children) {
            if (f.basis <= 0) {
                fixedSize += isRow ? f.child.w() : f.child.h();
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
                    f.child.h(h)
                           .x(x + consumedSpace)
                           .y(y);
                else
                    f.child.w(w)
                           .y(y + consumedSpace)
                           .x(x);

                consumedSpace += isRow ? f.child.w() : f.child.h() + gap;
            } else {
                float basisRatio = f.basis / maxBasis;
                int   scaledSize = (int) ((scaleRoot - gap * children.size()) * basisRatio);

                if (isRow)
                    f.child.w(scaledSize)
                           .h(h)
                           .x(x + consumedSpace)
                           .y(y);
                else
                    f.child.h(scaledSize)
                           .w(w)
                           .y(y + consumedSpace)
                           .x(x);

                consumedSpace += scaledSize + gap;
            }
            f.child.updateSize();

            if (f.child instanceof Selectable)
                if (!childSelectable.contains(f.child)) childSelectable.add((Selectable) f.child);
                else if (!childDrawable.contains(f.child)) childDrawable.add(f.child);
            if (!childStack.contains(f.child)) childStack.add(f.child);
        }
    }

    public enum FlexDirection {
        ROW,
        COLUMN
    }

    static class FlexChild {
        protected FlatWidgetBase child;
        protected float basis;

        public FlexChild(FlatWidgetBase child, float basis) {
            this.child = child;
            this.basis = basis;
        }

        public FlatWidgetBase getChild() {
            return child;
        }

        public float getBasis() {
            return basis;
        }

        public void setBasis(float basis) {
            this.basis = basis;
        }
    }
}
