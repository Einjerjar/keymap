package com.github.einjerjar.mc.keymap.screen.entrylist;

import com.github.einjerjar.mc.keymap.utils.SimpleV2;
import com.github.einjerjar.mc.keymap.utils.WidgetUtils;
import com.github.einjerjar.mc.keymap.widgets.containers.FlatEntryList;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.Accessors;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;

public class FlatStringList<E> extends FlatEntryList<FlatStringList.FlatStringEntry<E>> {
    public FlatStringList(int x, int y, int w, int h, int entryHeight) {
        super(x, y, w, h, entryHeight);
        pad = new SimpleV2(4);
    }

    @Override public void renderWidget(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        super.renderWidget(matrices, mouseX, mouseY, delta);
        canDeselectEntry = false;
    }

    @ToString
    @Accessors(fluent = true, chain = true)
    public static class FlatStringEntry<T> extends FlatEntryList.FlatEntry<FlatStringEntry<T>> {
        @Getter Text entryText;
        @Getter T entryIndex;

        public FlatStringEntry(T entryIndex, Text entryText) {
            this.entryIndex = entryIndex;
            this.entryText = entryText;
        }

        @Override
        public void renderWidget(MatrixStack matrices, int x, int y, int w, int h, boolean hovered, float delta) {
            WidgetUtils.drawCenteredText(matrices, tr, entryText, x, y, w, h, true, false, true, colors.text.normal);
        }
    }
}
