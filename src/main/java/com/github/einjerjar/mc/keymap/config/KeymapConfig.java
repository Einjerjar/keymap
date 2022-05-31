package com.github.einjerjar.mc.keymap.config;

import com.github.einjerjar.mc.keymap.Keymap;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import net.fabricmc.loader.api.FabricLoader;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;
import org.yaml.snakeyaml.representer.Representer;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

@ToString
@Accessors(fluent = true, chain = true)
public class KeymapConfig {
    static KeymapConfig instance;
    static DumperOptions dumperOptions;
    static Yaml yaml;
    static File cfgFile = new File(FabricLoader.getInstance().getConfigDir().resolve("keymap.yaml").toUri());

    // hidden
    @Getter @Setter public boolean firstOpenDone = false;

    // layout
    @Getter @Setter public boolean autoSelectLayout = false;
    @Getter @Setter public String customLayout = "en_us";

    // general
    @Getter @Setter public boolean replaceKeybindScreen = true;
    @Getter @Setter public boolean malilibSupport = true;
    @Getter @Setter public boolean debug = false;

    // tooltip
    @Getter @Setter public boolean keybindNameOnHover = true;
    @Getter @Setter public boolean keybindKeyOnHover = true;
    @Getter @Setter public boolean keyButtonModName = true;
    @Getter @Setter public boolean keyButtonMalilibKeybinds = true;

    public static KeymapConfig instance() {
        if (instance == null) instance = new KeymapConfig();
        return instance;
    }

    public static void save() {
        prepYaml();
        try (FileWriter writer = new FileWriter(cfgFile)) {
            yaml.dump(instance(), writer);
        } catch (Exception e) {
            Keymap.logger().error("!! Cant save config !!");
            e.printStackTrace();
        }
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
        try (FileReader reader = new FileReader(cfgFile)) {
            instance = yaml.load(reader);
        } catch (Exception e) {
            Keymap.logger().warn("!! Cant read config, using default settings !!");
            e.printStackTrace();
            save();
        }
    }
}
