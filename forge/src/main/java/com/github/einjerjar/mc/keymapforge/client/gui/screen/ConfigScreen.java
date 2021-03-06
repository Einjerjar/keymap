package com.github.einjerjar.mc.keymapforge.client.gui.screen;

import com.github.einjerjar.mc.keymap.config.KeymapConfig;
import com.github.einjerjar.mc.keymap.keys.layout.KeyLayout;
import com.github.einjerjar.mc.widgets.utils.Text;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import me.shedaniel.clothconfig2.impl.builders.DropdownMenuBuilder;
import net.minecraft.client.gui.screens.Screen;

public class ConfigScreen {

    private ConfigScreen() {
    }

    public static Screen getScreen(Screen parent) {
        KeymapConfig k = KeymapConfig.instance();

        ConfigBuilder builder = ConfigBuilder.create()
                .setParentScreen(parent)
                .setTitle(Text.translatable("keymap.scrSettings"));

        ConfigEntryBuilder entryBuilder = builder.entryBuilder();

        buildGeneral(builder, entryBuilder, k);
        buildLayout(builder, entryBuilder, k);
        buildTooltips(builder, entryBuilder, k);
        buildExtra(builder, entryBuilder, k);


        builder.setSavingRunnable(KeymapConfig::save);

        return builder.build();
    }

    protected static void buildGeneral(ConfigBuilder builder, ConfigEntryBuilder entryBuilder, KeymapConfig k) {
        ConfigCategory catGeneral = builder.getOrCreateCategory(Text.translatable("keymap.catGeneral"));
        catGeneral.addEntry(
                entryBuilder.startBooleanToggle(
                                Text.translatable("keymap.optReplaceKeybindScreen"),
                                k.replaceKeybindScreen())
                        .setSaveConsumer(k::replaceKeybindScreen)
                        .build()
        );
    }

    protected static void buildLayout(ConfigBuilder builder, ConfigEntryBuilder entryBuilder, KeymapConfig k) {
        ConfigCategory catLayout = builder.getOrCreateCategory(Text.translatable("keymap.catLayout"));

        catLayout.addEntry(
                entryBuilder.startBooleanToggle(
                                Text.translatable("keymap.optAutoSelectLayout"),
                                k.autoSelectLayout())
                        .setSaveConsumer(k::autoSelectLayout)
                        .build()
        );

        catLayout.addEntry(
                entryBuilder.startDropdownMenu(
                                Text.translatable("keymap.optCustomLayout"),
                                DropdownMenuBuilder.TopCellElementBuilder.of(k.customLayout(), s -> s)
                        )
                        .setSelections(() -> KeyLayout.layouts().keySet().iterator())
                        .setDefaultValue(k.customLayout())
                        .setSaveConsumer(k::customLayout)
                        .build()
        );
    }

    protected static void buildTooltips(ConfigBuilder builder, ConfigEntryBuilder entryBuilder, KeymapConfig k) {
        ConfigCategory catTooltips = builder.getOrCreateCategory(Text.translatable("keymap.catTooltips"));

        catTooltips.addEntry(
                entryBuilder.startBooleanToggle(
                                Text.translatable("keymap.optShowHelpTooltips"),
                                k.showHelpTooltips())
                        .setSaveConsumer(k::showHelpTooltips)
                        .build()
        );
    }

    protected static void buildExtra(ConfigBuilder builder, ConfigEntryBuilder entryBuilder, KeymapConfig k) {
        ConfigCategory catExtra = builder.getOrCreateCategory(Text.translatable("keymap.catExtra"));

        catExtra.addEntry(
                entryBuilder.startBooleanToggle(
                                Text.translatable("keymap.optFirstOpenDoneExtra"),
                                k.firstOpenDone()
                        ).setSaveConsumer(k::firstOpenDone)
                        .build()
        );
        catExtra.addEntry(
                entryBuilder.startBooleanToggle(
                                Text.translatable("keymap.optDebug"),
                                k.debug())
                        .setSaveConsumer(k::debug)
                        .build()
        );
        catExtra.addEntry(
                entryBuilder.startBooleanToggle(
                                Text.translatable("keymap.optDebug2"),
                                k.debug2())
                        .setSaveConsumer(k::debug2)
                        .build()
        );
        catExtra.addEntry(
                entryBuilder.startBooleanToggle(
                                Text.translatable("keymap.optCrashOnProblematicError"),
                                k.crashOnProblematicError())
                        .setSaveConsumer(k::crashOnProblematicError)
                        .build()
        );
    }
}
