package com.github.einjerjar.mc.keymap.screen.entrylist;

import com.github.einjerjar.mc.keymap.utils.WidgetUtils;
import com.github.einjerjar.mc.keymap.widgets.containers.FlatEntryList;
import lombok.Getter;
import lombok.experimental.Accessors;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;

@Accessors(fluent = true, chain = true)
public class FlatCategoryList extends FlatEntryList<FlatCategoryList.FlatCategoryEntry> {
    public FlatCategoryList(int x, int y, int w, int h, int entryHeight) {
        super(x, y, w, h, entryHeight);
    }

    @Accessors(fluent = true, chain = true)
    public static class FlatCategoryEntry extends FlatEntryList.FlatEntry<FlatCategoryEntry> {
        protected final String categoryTranslationString;
        protected final TextRenderer tr;
        @Getter protected String category;
        @Getter protected Text categoryTranslation;
        protected String trimmedCategoryString = null;

        public FlatCategoryEntry(String category) {
            this.tr = MinecraftClient.getInstance().textRenderer;
            this.category = category;
            if (category.equalsIgnoreCase("__ALL__")) {
                this.categoryTranslation = Text.translatable("key.keymap.cat.all");
            } else {
                this.categoryTranslation = Text.translatable(category);
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
            WidgetUtils.drawCenteredText(matrices, tr, Text.of(trimmedCategoryString), x, y, w, h, true, false, true, colors.text.normal);
        }
    }
}
