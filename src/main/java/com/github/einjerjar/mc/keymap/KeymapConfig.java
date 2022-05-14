package com.github.einjerjar.mc.keymap;

import lombok.ToString;
import net.fabricmc.loader.api.FabricLoader;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;
import org.yaml.snakeyaml.representer.Representer;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

@ToString
public class KeymapConfig {
    static KeymapConfig instance;
    static DumperOptions dumperOptions;
    static Yaml yaml;
    static File cfgFile = new File(FabricLoader.getInstance().getConfigDir().resolve("keymap.yaml").toUri());

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
        prepYaml();
        try {
            FileWriter writer = new FileWriter(cfgFile);
            yaml.dump(instance(), writer);
        } catch (Exception e) {
            KeymapMain.LOGGER().error("!! Cant save config !!");
            e.printStackTrace();
        }
        KeymapMain.LOGGER().info(instance().toString());
    }

    public static void prepYaml() {
        if (yaml != null) return;
        dumperOptions = new DumperOptions();
        dumperOptions.setIndent(2);
        dumperOptions.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
        yaml = new Yaml(new Constructor(KeymapConfig.class), new Representer(dumperOptions), dumperOptions);
    }

    public static void load() {
        prepYaml();
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
