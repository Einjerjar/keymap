package com.github.einjerjar.mc.keymap.screen.entrylist;

import com.github.einjerjar.mc.keymap.utils.WidgetUtils;
import com.github.einjerjar.mc.keymap.widgets.containers.FlatEntryList;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;

import java.util.List;

public class FlatCategoryList extends FlatEntryList<FlatCategoryList.FlatCategoryEntry> {
    CommonAction onSelectedAction;

    public FlatCategoryList(int x, int y, int w, int h, int entryHeight) {
        super(x, y, w, h, entryHeight);
    }

    public void setOnSelectedAction(CommonAction onSelectedAction) {
        this.onSelectedAction = onSelectedAction;
    }

    @Override
    public void appendNarrations(NarrationMessageBuilder builder) {
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        if (super.mouseReleased(mouseX, mouseY, button)) {
            if (onSelectedAction != null) onSelectedAction.run(this);
            return true;
        }
        return false;
    }

    @Override
    public void renderWidget(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        // WidgetUtils.drawBoxOutline(this, matrices, x, y, w, h, ColorGroup.GREEN.border.normal);

        super.renderWidget(matrices, mouseX, mouseY, delta);
    }

    public static class FlatCategoryEntry extends FlatEntryList.FlatEntry<FlatCategoryEntry> {
        public String category;
        protected Text categoryTranslation;
        String categoryTranslationString;
        String trimmedCategoryString = null;
        TextRenderer tr;

        public FlatCategoryEntry(String category) {
            this.tr = MinecraftClient.getInstance().textRenderer;
            this.category = category;
            if (category.equalsIgnoreCase("__ALL__")) {
                this.categoryTranslation = new TranslatableText("key.keymap.cat.all");
            } else {
                this.categoryTranslation = new TranslatableText(category);
            }
            this.categoryTranslationString = this.categoryTranslation.getString();
        }

        @Override
        public void renderWidget(MatrixStack matrices, int x, int y, int w, int h, boolean hovered, float delta) {
            if (trimmedCategoryString == null) {
                trimmedCategoryString = tr.trimToWidth(categoryTranslation, w).getString();
                if (!trimmedCategoryString.equalsIgnoreCase(categoryTranslationString)) {
                    tooltips.clear();
                    tooltips.add(categoryTranslation);
                }
            }
            WidgetUtils.drawCenteredText(matrices, tr, new LiteralText(trimmedCategoryString), x, y, w, h, true, false, true, colors.text.normal);
        }
    }
}
