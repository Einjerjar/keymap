package com.github.einjerjar.mc.keymap.screen;

import net.minecraft.text.Text;

import java.util.List;

public interface Tooltipped {
    List<Text> getToolTips();

    Text getFirstToolTip();
}
