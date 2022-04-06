package com.github.einjerjar.mc.keymap.screen.entrylist;

import com.github.einjerjar.mc.keymap.KeymapMain;
import com.github.einjerjar.mc.keymap.keys.KeybindHolder;
import com.github.einjerjar.mc.keymap.keys.key.MalilibKeybind;
import com.github.einjerjar.mc.keymap.utils.Utils;
import com.github.einjerjar.mc.keymap.utils.WidgetUtils;
import com.github.einjerjar.mc.keymap.widgets.containers.FlatEntryList;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;

import java.util.List;

public class FlatKeyList extends FlatEntryList<FlatKeyList.FlatKeyListEntry> {
    FlatKeyListAction onKeyChanged;

    public FlatKeyList(int x, int y, int w, int h, int entryHeight) {
        super(x, y, w, h, entryHeight);
    }

    public void setOnKeyChanged(FlatKeyListAction onKeyChanged) {
        this.onKeyChanged = onKeyChanged;
    }

    public void resetSelected() {
        if (selectedEntry == null) return;
        selectedEntry.holder.resetHotkey();
        selectedEntry.updateDisplayText();
        setSelectedEntry(null);
    }

    public void resetAll() {
        for (FlatKeyListEntry fk : entries) {
            fk.holder.resetHotkey();
            fk.updateDisplayText();
        }
        setSelectedEntry(null);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (selectedEntry == null) return false;

        // if (selectedEntry.holder instanceof VanillaKeybind vk) {
        //     vk.assignHotKey(new Integer[]{keyCode}, false);
        // }

        if (onKeyChanged != null) onKeyChanged.run(this, keyCode);

        if (selectedEntry != null) {
            selectedEntry.selected = false;
            selectedEntry = null;
        }

        return true;
    }

    @Override
    public void appendNarrations(NarrationMessageBuilder builder) {

    }

    public interface FlatKeyListAction {
        void run(FlatKeyList fk, int key);
    }

    public static class FlatKeyListEntry extends FlatEntryList.FlatEntry<FlatKeyListEntry> {
        public KeybindHolder holder;
        Text displayText;

        public FlatKeyListEntry(KeybindHolder holder) {
            super();
            this.holder = holder;
            this.displayText = new LiteralText(String.format(
                "%s §a[%s]",
                holder.getTranslation().getString(),
                holder.getKeyTranslation().getString()
            ));
        }

        public void updateDisplayText() {
            KeymapMain.LOGGER.info("UPDATESTATE2");
            displayText = new LiteralText(String.format(
                "%s §a[%s]",
                holder.getTranslation().getString(),
                holder.getKeyTranslation().getString()
            ));
        }

        @Override
        public void updateState() {
            super.updateState();
            tooltips.clear();
            if (KeymapMain.cfg.keybindNameOnHover) tooltips.add(holder.getTranslation().getWithStyle(Utils.styleKey).get(0));
            int maxL = holder.getTranslation().getString().length();
            if (KeymapMain.cfg.keybindKeyOnHover && holder.getCode().size() > 0 && holder.getCode().get(0) != -1) {
                maxL = Math.max(maxL, holder.getKeyTranslation().getString().length());
                try {
                    if (holder instanceof MalilibKeybind ml) {
                        tooltips.add(new LiteralText(ml.getKeysString(false)));
                    } else {
                        tooltips.add(holder.getKeyTranslation().getWithStyle(Utils.stylePassive).get(0));
                    }
                } catch (Exception ignored) {
                }
                if (tooltips.size() > 1) {
                    tooltips.add(1, new LiteralText("-".repeat(maxL)).getWithStyle(Utils.stylePassive).get(0));
                }
            }
        }

        @Override
        public void renderWidget(MatrixStack matrices, int x, int y, int w, int h, boolean hovered, float delta) {
            WidgetUtils.drawCenteredText(matrices, tr, new LiteralText(tr.trimToWidth(this.displayText, w).getString()), x, y, w, h, true, false, true, colors.text.normal);
        }

        @Override
        public List<Text> getToolTips() {
            return tooltips;
        }
    }
}
