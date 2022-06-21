package com.github.einjerjar.mc.keymap.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.fasterxml.jackson.dataformat.yaml.YAMLGenerator;
import com.github.einjerjar.mc.keymap.Keymap;
import lombok.*;
import lombok.experimental.Accessors;
import lombok.extern.jackson.Jacksonized;
import net.fabricmc.loader.api.FabricLoader;

import java.io.File;

@Getter
@Setter
@Builder
@ToString
@Jacksonized
@NoArgsConstructor
@AllArgsConstructor
@Accessors(fluent = true)
public class KeymapConfig {
    static KeymapConfig instance;
    static ObjectMapper mapper = new ObjectMapper(new YAMLFactory().disable(YAMLGenerator.Feature.WRITE_DOC_START_MARKER));
    static File cfgFile = new File(FabricLoader.getInstance().getConfigDir().resolve("keymap.yaml").toUri());

    // hidden
    @Builder.Default protected boolean firstOpenDone = false;

    // layout
    @Builder.Default protected boolean autoSelectLayout = false;
    @Builder.Default protected String customLayout = "en_us";

    // general
    @Builder.Default protected boolean replaceKeybindScreen = true;
    @Builder.Default protected boolean malilibSupport = true;
    @Builder.Default protected boolean showHelpTooltips = true;
    @Builder.Default protected boolean debug = false;

    // tooltip
    @Builder.Default protected boolean keybindNameOnHover = true;
    @Builder.Default protected boolean keybindKeyOnHover = true;
    @Builder.Default protected boolean keyButtonModName = true;
    @Builder.Default protected boolean keyButtonMalilibKeybinds = true;

    public static KeymapConfig instance() {
        if (instance == null) instance = new KeymapConfig();
        return instance;
    }

    public static void save() {
        try {
            mapper.writeValue(cfgFile, instance());
        } catch (Exception e) {
            Keymap.logger().error("!! Cant save config !!");
            e.printStackTrace();
        }
    }

    public static void load() {
        try {
            instance = mapper.readValue(cfgFile, KeymapConfig.class);
        } catch (Exception e) {
            Keymap.logger().warn("!! Cant read config, using default settings !!");
            e.printStackTrace();
            save();
        }
    }
}
