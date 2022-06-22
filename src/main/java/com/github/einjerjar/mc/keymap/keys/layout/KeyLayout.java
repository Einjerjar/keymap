package com.github.einjerjar.mc.keymap.keys.layout;

import com.github.einjerjar.mc.keymap.Keymap;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.io.InputStreamReader;
import java.net.URI;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Stream;

@ToString
@AllArgsConstructor
@Accessors(fluent = true)
public class KeyLayout {
    private static final      String                     LAYOUT_ROOT = "assets/keymap/layouts/";
    @Getter protected static  KeyLayout                  layoutDefault;
    @Getter protected static  KeyLayout                  layoutCurrent;
    @Getter protected static  HashMap<String, KeyLayout> layouts     = new HashMap<>();
    @Getter @Setter protected KeyMeta                    meta;
    @Getter @Setter protected Keys                       keys;

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

        GsonBuilder  builder = new GsonBuilder().setPrettyPrinting();
        Gson         gson    = builder.create();
        ClassLoader  loader  = KeyLayout.class.getClassLoader();
        FileSystem   fs      = null;
        Stream<Path> files   = null;

        try {
            URI  layoutUri = Objects.requireNonNull(loader.getResource(LAYOUT_ROOT).toURI());
            Path path;
            if (layoutUri.getScheme().equals("jar")) {
                fs   = FileSystems.newFileSystem(layoutUri, Collections.emptyMap());
                path = fs.getPath(LAYOUT_ROOT);
            } else {
                path = Path.of(layoutUri);
            }

            files = Files.walk(path, 1);

            for (Iterator<Path> it = files.iterator(); it.hasNext(); ) {
                Path p = it.next();
                if (!p.getFileName().toString().endsWith(".json")) continue;

                // simple notice, since sonarlint/intellij is complaining about readability
                // ----------------- another try-catch block starts here -----------------
                try (InputStreamReader reader =
                             new InputStreamReader(Objects.requireNonNull(loader.getResourceAsStream(LAYOUT_ROOT + p.getFileName())))) {
                    registerLayout(gson.fromJson(reader, KeyLayout.class));
                } catch (Exception e) {
                    Keymap.logger().warn("Can't load {} ; {}", p.getFileName(), e.getMessage());
                    e.printStackTrace();
                }
                // ----------------- another try-catch block ends here -----------------
            }
        } catch (Exception e) {
            Keymap.logger().error(e.getMessage());
            e.printStackTrace();
        } finally {
            if (files != null) files.close();
            try {
                if (fs != null) fs.close();
            } catch (Exception e) {
                Keymap.logger().error("Can't close fs");
            }
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
