package com.github.einjerjar.mc.keymap.config;

import com.github.einjerjar.mc.keymap.Keymap;
import com.github.einjerjar.mc.keymap.cross.Services;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.*;
import lombok.experimental.Accessors;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;

/**
 * The mod's configuration, too lazy to document
 * TODO: Document this
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Accessors(fluent = true)
public class KeymapConfig {
    static KeymapConfig instance;
    static Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private static File cfgFile = null;

    // extra
    protected boolean firstOpenDone = false;
    // layout
    protected boolean autoSelectLayout = false;
    protected String customLayout = "en_us";
    // general
    protected boolean replaceKeybindScreen = true;
    protected boolean malilibSupport = true;
    protected boolean debug = false;
    protected boolean debug2 = false;
    protected boolean crashOnProblematicError = false;
    // tooltip
    protected boolean showHelpTooltips = true;

    private static synchronized File cfgFile() {
        if (cfgFile == null) cfgFile = Services.PLATFORM.config("keymap.json");
        return cfgFile;
    }

    public static synchronized KeymapConfig instance() {
        if (instance == null) instance = new KeymapConfig();
        return instance;
    }

    public static void save() {
        try (FileWriter writer = new FileWriter(cfgFile())) {
            gson.toJson(instance(), writer);
        } catch (Exception e) {
            Keymap.logger().error("!! Cant save config !!");
            e.printStackTrace();
        }
    }

    public static void load() {
        try (FileReader reader = new FileReader(cfgFile())) {
            instance = gson.fromJson(reader, KeymapConfig.class);
        } catch (FileNotFoundException e) {
            Keymap.logger().warn("!! Config not found, using default settings !!");
            save();
        } catch (Exception e) {
            Keymap.logger().warn("!! Cant read config, using default settings !!");
            e.printStackTrace();
            save();
        }
    }

    @FunctionalInterface
    public interface KeymapConfigDirProvider {
        File execute(String name);
    }
}
