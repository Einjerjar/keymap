package com.github.einjerjar.mc.keymap.screen.entrylist;

import com.github.einjerjar.mc.keymap.KeymapMain;
import com.github.einjerjar.mc.keymap.keys.KeybindHolder;
import com.github.einjerjar.mc.keymap.utils.WidgetUtils;
import com.github.einjerjar.mc.keymap.widgets.containers.FlatEntryList;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.util.math.MatrixStack;

public class FlatKeyList extends FlatEntryList<FlatKeyList.FlatKeyListEntry> {

    public FlatKeyList(int x, int y, int w, int h, int entryHeight) {
        super(x, y, w, h, entryHeight);
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        super.render(matrices, mouseX, mouseY, delta);
        renderScrollbar(matrices);
        renderList(matrices);
    }

    @Override
    public void appendNarrations(NarrationMessageBuilder builder) {

    }

    public static class FlatKeyListEntry extends FlatEntryList.FlatEntry<FlatKeyListEntry> {
        public KeybindHolder holder;


        public FlatKeyListEntry(KeybindHolder holder) {
            super();
            this.holder = holder;
        }

        @Override
        public void render(MatrixStack matrices, int x, int y, int w, int h, boolean hovered, float delta) {
            WidgetUtils.drawCenteredText(matrices, tr, holder.getTranslation(), x, y, w, h, true, false, true, colors.text.normal);
        }
    }
}
