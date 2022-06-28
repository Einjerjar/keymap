package com.github.einjerjar.mc.keymap.screen;

import com.github.einjerjar.mc.keymap.KeymapConfig;
import com.github.einjerjar.mc.keymap.keys.layout.KeyboardLayoutBase;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import me.shedaniel.clothconfig2.impl.builders.DropdownMenuBuilder;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

public class ConfigScreen {
    public static Screen get(Screen parent) {
        KeymapConfig k = KeymapConfig.instance();
        ConfigBuilder builder = ConfigBuilder.create()
                                             .setParentScreen(parent)
                                             .setTitle(Text.translatable("key.keymap.cfg.title"));
        ConfigEntryBuilder entryBuilder = builder.entryBuilder();
        ConfigCategory     catGeneral   = builder.getOrCreateCategory(Text.translatable("key.keymap.cfg.cat.general"));
        ConfigCategory     catLayout    = builder.getOrCreateCategory(Text.translatable("key.keymap.cfg.cat.layout"));
        ConfigCategory     catTooltips  = builder.getOrCreateCategory(Text.translatable("key.keymap.cfg.cat.tooltips"));

        builder.setSavingRunnable(KeymapConfig::save);

        // ---------- General ----------
        catGeneral.addEntry(
            entryBuilder.startBooleanToggle(Text.translatable("key.keymap.cfg.general.replace_screen"), k.replaceKeybindScreen)
                        .setSaveConsumer(b -> k.replaceKeybindScreen = b)
                        .build()
        );

        catGeneral.addEntry(
            entryBuilder.startBooleanToggle(Text.translatable("key.keymap.cfg.general.malilib_support"), k.malilibSupport)
                        .setSaveConsumer(b -> k.malilibSupport = b)
                        .build()
        );

        catGeneral.addEntry(
            entryBuilder.startBooleanToggle(Text.translatable("key.keymap.cfg.general.debug"), k.debug)
                        .setSaveConsumer(b -> k.debug = b)
                        .build()
        );

        // ---------- Layouts ----------
        catLayout.addEntry(
            entryBuilder.startBooleanToggle(Text.translatable("key.keymap.cfg.layout.auto_layout"), k.autoSelectLayout)
                        .setSaveConsumer(b -> k.autoSelectLayout = b)
                        .build()
        );

        catLayout.addEntry(
            entryBuilder.startDropdownMenu(
                            Text.translatable("key.keymap.cfg.layout.layout_override"),
                            DropdownMenuBuilder.TopCellElementBuilder.of("en_us", s -> s))
                        .setSelections(() -> KeyboardLayoutBase.layouts().keySet().iterator())
                        .setDefaultValue(k.customLayout)
                        .setSaveConsumer(s -> k.customLayout = s)
                        .setSuggestionMode(false)
                        .build()
        );

        // ---------- Tooltips ----------
        catTooltips.addEntry(
            entryBuilder.startBooleanToggle(Text.translatable("key.keymap.cfg.tooltips.keybind_name_hover"), k.keybindNameOnHover)
                        .setSaveConsumer(b -> k.keybindNameOnHover = b)
                        .build()
        );
        catTooltips.addEntry(
            entryBuilder.startBooleanToggle(Text.translatable("key.keymap.cfg.tooltips.keybind_key_hover"), k.keybindKeyOnHover)
                        .setSaveConsumer(b -> k.keybindKeyOnHover = b)
                        .build()
        );
        catTooltips.addEntry(
            entryBuilder.startBooleanToggle(Text.translatable("key.keymap.cfg.tooltips.key_button_mod_name"), k.keyButtonModName)
                        .setSaveConsumer(b -> k.keyButtonModName = b)
                        .build()
        );
        catTooltips.addEntry(
            entryBuilder.startBooleanToggle(Text.translatable("key.keymap.cfg.tooltips.key_button_malilib_binds"), k.keyButtonMalilibKeybinds)
                        .setSaveConsumer(b -> k.keyButtonMalilibKeybinds = b)
                        .build()
        );

        return builder.build();
    }
}
