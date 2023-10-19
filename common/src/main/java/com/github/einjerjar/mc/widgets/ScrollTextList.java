package com.github.einjerjar.mc.widgets;

import com.github.einjerjar.mc.widgets.utils.Rect;
import lombok.Getter;
import lombok.experimental.Accessors;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import org.jetbrains.annotations.NotNull;

@Accessors(fluent = true)
public class ScrollTextList extends EList<ScrollTextList.ScrollTextEntry> {
    protected ScrollTextList(int x, int y, int w, int h) {
        super(Minecraft.getInstance().font.lineHeight, x, y, w, h);
    }

    public static ScrollTextList createFromString(String s, int x, int y, int w, int h) {
        String[] lines = s.split("\n");
        return createFromString(lines, x, y, w, h);
    }

    public static ScrollTextList createFromString(String[] lines, int x, int y, int w, int h) {
        ScrollTextList list = new ScrollTextList(x, y, w, h);
        Font f = Minecraft.getInstance().font;
        for (String line : lines) {
            int fullWidth = f.width(line);
            int lineWidth = w - list.padding.x() * 2;

            if (fullWidth <= lineWidth) {
                list.addLine(line);
                continue;
            }

            String currentLine = line;
            String trimmedLine = f.plainSubstrByWidth(line, lineWidth);
            String tempLine;
            int ix = 0;
            while (!trimmedLine.equals(currentLine)) {
                tempLine = currentLine;
                currentLine = currentLine.substring(trimmedLine.length());
                if (currentLine.startsWith(" ") || trimmedLine.endsWith(" ")) {
                    list.addLine(trimmedLine, ix);
                } else {
                    int li = trimmedLine.lastIndexOf(" ");
                    trimmedLine = trimmedLine.substring(0, li);
                    currentLine = (tempLine.substring(trimmedLine.length())).trim();
                    list.addLine(trimmedLine, ix);
                }
                trimmedLine = f.plainSubstrByWidth(currentLine, lineWidth);
                ix++;
            }
            list.addLine(trimmedLine, ix);
        }

        return list;
    }

    public void addLine(String line, int ix) {
        addLine((ix == 0 ? "" : " ") + line.trim());
    }

    public void addLine(String line) {
        addItem(new ScrollTextEntry(line, this));
    }

    public static class ScrollTextEntry extends EListEntry<ScrollTextEntry> {
        @Getter
        protected String text;

        protected ScrollTextEntry(String text, EList<ScrollTextEntry> container) {
            super(container);
            this.text = text;
        }

        @Override
        public void renderWidget(@NotNull GuiGraphics guiGraphics, Rect r, float partialTick) {
            guiGraphics.drawString(font, text, r.left(), r.top(), getVariant().text());
        }
    }
}
