package com.github.einjerjar.mc.keymap.keys.layout;

import com.github.einjerjar.mc.keymap.KeymapMain;
import com.github.einjerjar.mc.keymap.keys.BasicKeyData;
import lombok.ToString;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

import java.io.InputStream;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class KeyLayoutLoader {
    static String layoutRoot = "assets/keymap/layouts/";

    private List<List<BasicKeyData>> populateKeyList(List<KeyLayoutRow> keys) {
        List<List<BasicKeyData>> keyList = new ArrayList<>();
        keys.forEach(row -> {
            ArrayList<BasicKeyData> rowData = new ArrayList<>();
            row.row.forEach(key -> rowData.add(new BasicKeyData(
                key.name,
                key.code,
                key.width,
                key.height)));
            keyList.add(rowData);
        });

        return keyList;
    }

    public void loadLayouts() {
        KeyboardLayoutBase.layouts().clear();
        try {
            ClassLoader loader    = this.getClass().getClassLoader();
            URI         layoutUri = loader.getResource(layoutRoot).toURI();
            Yaml        yaml      = new Yaml(new Constructor(KeyLayoutConfig.class));
            Path        path      = Paths.get(layoutUri);
            Files.list(path).forEach(path1 -> {
                try {
                    InputStream     is  = loader.getResourceAsStream(layoutRoot + path1.getFileName());
                    KeyLayoutConfig cfg = yaml.load(is);

                    KeyboardLayoutBase klb = new KeyboardLayoutBase(cfg.meta.name, cfg.meta.code) {{
                        basic = populateKeyList(cfg.keys.basic);
                        extra = populateKeyList(cfg.keys.extra);
                        mouse = populateKeyList(cfg.keys.mouse);
                        numpad = populateKeyList(cfg.keys.numpad);
                    }};

                    KeymapMain.LOGGER().info("loaded keymap layout: {}", path1.getFileName());

                    if (cfg.meta.code.equalsIgnoreCase(KeyboardLayoutBase.default_code())) {
                        KeyboardLayoutBase.default_layout(klb);
                    }

                } catch (Exception e) {
                    KeymapMain.LOGGER().warn("!! failed to load {} !!", path1.getFileName());
                    KeymapMain.LOGGER().warn(e.getMessage());
                    e.printStackTrace();
                }
            });
        } catch (Exception e) {
            KeymapMain.LOGGER().error(e.getMessage());
            e.printStackTrace();
        }
    }

    @ToString
    public static class KeyLayoutMeta {
        public String name;
        public String code;

        public KeyLayoutMeta() {
        }
    }

    @ToString
    public static class KeyLayoutKey {
        public String name;
        public Integer code = 0;
        public Integer width = 0;
        public Integer height = 0;
        public Boolean disabled = false;
        public Boolean mouse = false;

        public KeyLayoutKey() {
        }
    }

    @ToString
    public static class KeyLayoutRow {
        public ArrayList<KeyLayoutKey> row;

        public KeyLayoutRow() {
        }
    }

    @ToString
    public static class KeyLayoutKeys {
        public ArrayList<KeyLayoutRow> basic;
        public ArrayList<KeyLayoutRow> extra;
        public ArrayList<KeyLayoutRow> mouse;
        public ArrayList<KeyLayoutRow> numpad;

        public KeyLayoutKeys() {
        }
    }

    @ToString
    public static class KeyLayoutConfig {
        public KeyLayoutMeta meta;
        public KeyLayoutKeys keys;

        public KeyLayoutConfig() {
        }
    }
}
