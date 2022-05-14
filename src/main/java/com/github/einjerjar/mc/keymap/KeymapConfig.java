package com.github.einjerjar.mc.keymap;

import lombok.ToString;
import net.fabricmc.loader.api.FabricLoader;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

@ToString
public class KeymapConfig {
    static KeymapConfig instance;
    static Yaml yaml = new Yaml(new Constructor(KeymapConfig.class));
    static File cfgFile = new File(FabricLoader.getInstance().getConfigDir().resolve("keymapConfig.yaml").toUri());

    // layout
    public boolean autoSelectLayout = true;
    public String customLayout = "en_us";

    // general
    public boolean replaceKeybindScreen = true;
    public boolean malilibSupport = true;
    public boolean debug = false;

    // tooltip
    public boolean keybindNameOnHover = true;
    public boolean keybindKeyOnHover = true;
    public boolean keyButtonModName = true;
    public boolean keyButtonMalilibKeybinds = true;

    public static KeymapConfig instance() {
        if (instance == null) instance = new KeymapConfig();
        return instance;
    }

    public static void save() {
        try {
            FileWriter writer = new FileWriter(cfgFile);
            yaml.dump(instance(), writer);
        } catch (Exception e) {
            KeymapMain.LOGGER().error("!! Cant save config !!");
            e.printStackTrace();
        }
        KeymapMain.LOGGER().info(instance().toString());
    }

    public static void load() {
        try {
            FileReader reader = new FileReader(cfgFile);
            instance = yaml.load(reader);
        } catch (Exception e) {
            KeymapMain.LOGGER().warn("!! Cant read config, using default settings !!");
            e.printStackTrace();
            save();
        }
    }
}
