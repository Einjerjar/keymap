package com.github.einjerjar.mc.keymap.screen.entrylist;

import com.github.einjerjar.mc.keymap.KeymapMain;
import com.github.einjerjar.mc.keymap.keys.KeybindHolder;
import com.github.einjerjar.mc.keymap.utils.Utils;
import com.github.einjerjar.mc.keymap.utils.WidgetUtils;
import com.github.einjerjar.mc.keymap.widgets.containers.FlatEntryList;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;

import java.util.List;

public class FlatKeyList extends FlatEntryList<FlatKeyList.FlatKeyListEntry> {

    public FlatKeyList(int x, int y, int w, int h, int entryHeight) {
        super(x, y, w, h, entryHeight);
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        super.render(matrices, mouseX, mouseY, delta);
        renderScrollbar(matrices);
        renderList(matrices);

        // renderT

        if (hoveredEntry != null) {
            WidgetUtils.drawCenteredText(matrices, tr, hoveredEntry.holder.getTranslation(), x, y, w, h, true, true, false, 0xff_ffffff);
        }
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
        public void updateState() {
            super.updateState();
            tooltips.clear();
            tooltips.add(holder.getTranslation().getWithStyle(Utils.styleKey).get(0));
            int maxl = holder.getTranslation().getString().length();
            if (holder.getCode().size() > 0 && holder.getCode().get(0) != -1) {
                maxl = Math.max(maxl, holder.getKeyTranslation().getString().length());
                tooltips.add(new LiteralText("-".repeat(maxl)).getWithStyle(Utils.stylePassive).get(0));
                tooltips.add(holder.getKeyTranslation().getWithStyle(Utils.stylePassive).get(0));
            }
        }

        @Override
        public void render(MatrixStack matrices, int x, int y, int w, int h, boolean hovered, float delta) {
            this.hovered = hovered;
            updateState();
            WidgetUtils.drawCenteredText(matrices, tr, holder.getTranslation(), x, y, w, h, true, false, true, colors.text.normal);
        }

        @Override
        public List<Text> getToolTips() {
            return tooltips;
        }
    }
}
