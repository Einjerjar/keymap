package com.github.einjerjar.mc.keymap.testing;

import lombok.ToString;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

public class yaml {
    public static void main(String[] args) throws IOException, URISyntaxException {
        String      resRoot = "assets/keymap/layouts/";
        ClassLoader loader  = yaml.class.getClassLoader();
        Yaml        y       = new Yaml(new Constructor(KeyLayoutConfig.class));
        URI         uri     = loader.getResource(resRoot).toURI();
        Path        path    = Paths.get(uri);
        Files.list(path).forEach(path1 -> {
            System.out.println(resRoot + path1.getFileName());
            InputStream     is  = loader.getResourceAsStream(resRoot + path1.getFileName());
            KeyLayoutConfig cfg = y.load(is);
            System.out.println(cfg.keys.basic.get(0).row.get(0));
        });
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
