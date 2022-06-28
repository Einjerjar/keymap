package com.github.einjerjar.mc.keymap.screen.entrylist;

import com.github.einjerjar.mc.keymap.KeymapMain;
import com.github.einjerjar.mc.keymap.keys.KeybindHolder;
import com.github.einjerjar.mc.keymap.keys.key.MalilibKeybind;
import com.github.einjerjar.mc.keymap.utils.Utils;
import com.github.einjerjar.mc.keymap.utils.WidgetUtils;
import com.github.einjerjar.mc.keymap.widgets.containers.FlatEntryList;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;

@Accessors(fluent = true, chain = true)
public class FlatKeyList extends FlatEntryList<FlatKeyList.FlatKeyListEntry> {
    @Setter protected FlatKeyListAction onKeyChanged;

    public FlatKeyList(int x, int y, int w, int h, int entryHeight) {
        super(x, y, w, h, entryHeight);
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

        if (onKeyChanged != null) onKeyChanged.run(this, keyCode);

        if (selectedEntry != null) {
            selectedEntry.selected = false;
            selectedEntry = null;
        }

        return true;
    }

    public interface FlatKeyListAction {
        void run(FlatKeyList fk, int key);
    }

    @Accessors(fluent = true, chain = true)
    public static class FlatKeyListEntry extends FlatEntryList.FlatEntry<FlatKeyListEntry> {
        @Getter protected KeybindHolder holder;
        protected Text displayText;

        public FlatKeyListEntry(KeybindHolder holder) {
            super();
            this.holder = holder;
            String keys = holder.keyTranslation().getString();

            if (holder instanceof MalilibKeybind mk) {
                keys = mk.getKeysString(false);
            }

            this.displayText = Text.of(String.format(
                "%s §a[%s]",
                holder.translation().getString(),
                keys
            ));
        }

        public void updateDisplayText() {
            String keys = holder.keyTranslation().getString();

            if (holder instanceof MalilibKeybind mk) {
                keys = mk.getKeysString(false);
            }

            displayText = Text.of(String.format(
                "%s §a[%s]",
                holder.translation().getString(),
                keys
            ));
        }

        @Override
        public void updateState() {
            super.updateState();
            tooltips.clear();
            if (KeymapMain.cfg().keybindNameOnHover)
                tooltips.add(holder.translation().getWithStyle(Utils.styleKey).get(0));
            int maxL = holder.translation().getString().length();
            if (KeymapMain.cfg().keybindKeyOnHover && holder.code().size() > 0 && holder.code().get(0) != -1) {
                maxL = Math.max(maxL, holder.keyTranslation().getString().length());
                try {
                    if (holder instanceof MalilibKeybind ml) {
                        tooltips.add(Text.of(ml.getKeysString(false)).getWithStyle(Utils.stylePassive).get(0));
                    } else {
                        tooltips.add(holder.keyTranslation().getWithStyle(Utils.stylePassive).get(0));
                    }
                } catch (Exception ignored) {
                }
                if (tooltips.size() > 1) {
                    tooltips.add(1, Text.of("-".repeat(maxL)).getWithStyle(Utils.stylePassive).get(0));
                }
            }
        }

        @Override
        public void renderWidget(MatrixStack matrices, int x, int y, int w, int h, boolean hovered, float delta) {
            WidgetUtils.drawCenteredText(matrices, tr, Text.of(tr.trimToWidth(this.displayText, w).getString()), x, y, w, h, true, false, true, colors.text.normal);
        }
    }
}
