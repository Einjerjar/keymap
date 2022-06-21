package com.github.einjerjar.mc.keymap.keys.layout;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.fasterxml.jackson.dataformat.yaml.YAMLGenerator;
import com.github.einjerjar.mc.keymap.Keymap;
import lombok.*;
import lombok.experimental.Accessors;
import lombok.extern.jackson.Jacksonized;

import java.io.InputStream;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

@Builder
@ToString
@Jacksonized
@Accessors(fluent = true)
public class KeyLayout {
    private static final String LAYOUT_ROOT = "assets/keymap/layouts/";
    @Getter protected static KeyLayout layoutDefault;
    @Getter protected static KeyLayout layoutCurrent;
    @Getter protected static HashMap<String, KeyLayout> layouts = new HashMap<>();
    @Getter @Setter protected KeyMeta meta;
    @Getter @Setter protected Keys keys;

    public static void registerLayout(KeyLayout layout) {
        layouts.put(layout.meta.code, layout);
        updateMouseKeys(layout.keys.mouse());
        updateMouseKeys(layout.keys.basic());
        updateMouseKeys(layout.keys.numpad());
        updateMouseKeys(layout.keys.extra());
    }

    protected static void updateMouseKeys(List<KeyRow> rows) {
        for (KeyRow row : rows) {
            for (KeyData key : row.row) {
                if (key.code() < 10) {
                    key.mouse(true);
                }
            }
        }
    }

    public static void loadKeys() {
        layouts.clear();

        // Path shenanigans, cuz sonarlint is a fuken biatch
        Stream<Path> paths = null;
        try {
            ClassLoader loader = KeyLayout.class.getClassLoader();
            URI layoutUri = Objects.requireNonNull(loader.getResource(LAYOUT_ROOT)).toURI();
            ObjectMapper mapper = new ObjectMapper(new YAMLFactory().disable(YAMLGenerator.Feature.WRITE_DOC_START_MARKER));
            Path path = Paths.get(layoutUri);

            paths = Files.list(path);

            paths.forEach(p -> {
                try (InputStream is = loader.getResourceAsStream(LAYOUT_ROOT + p.getFileName())) {
                    KeyLayout layout = mapper.readValue(is, KeyLayout.class);
                    registerLayout(layout);
                } catch (Exception e) {
                    Keymap.logger().error(e.getMessage());
                    e.printStackTrace();
                }
            });

            paths.close();
        } catch (Exception e) {
            Keymap.logger().error(e.getMessage());
            e.printStackTrace();
        } finally {
            if (paths != null) paths.close();
        }
    }

    public static KeyLayout getCurrentLayout() {
        return layoutCurrent;
    }

    public static KeyLayout getLayoutWithCode(String code) {
        if (layouts.containsKey(code)) {
            return layouts.get(code);
        } else {
            Keymap.logger().warn("Cannot find layout for [{}], defaulting to en_us", code);
            return layouts.get("en_us");
        }
    }
}
