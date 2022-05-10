package com.github.einjerjar.mc.keymap.widgets;

import com.github.einjerjar.mc.keymap.screen.Tooltipped;
import com.github.einjerjar.mc.keymap.utils.Utils;
import lombok.Setter;
import lombok.experimental.Accessors;
import net.minecraft.client.gui.Selectable;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.List;

@Accessors(fluent = true, chain = true)
public abstract class FlatSelectableWidget<T extends FlatSelectableWidget<?>> extends FlatWidget<T> implements Selectable, Tooltipped {
    @Setter protected List<Text> tooltips = new ArrayList<>();

    public FlatSelectableWidget(Class<T> self, int x, int y, int w, int h) {
        super(self, x, y, w, h);
    }

    @Override public List<Text> getToolTips() {
        return tooltips;
    }

    @Override public Text getFirstToolTip() {
        return Utils.safeGet(tooltips, 0);
    }

    public FlatSelectableWidget<T> tooltip(Text tooltip) {
        return tooltips(new ArrayList<>() {{
            add(tooltip);
        }});
    }

    @Override
    public SelectionType getType() {
        return focused
               ? SelectionType.FOCUSED
               : hovered
                 ? SelectionType.HOVERED
                 : SelectionType.NONE;
    }

    @Override public void appendNarrations(NarrationMessageBuilder builder) {
    }
}
