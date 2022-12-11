package com.github.einjerjar.mc.keymap.keys.layout;

import com.github.einjerjar.mc.keymap.Keymap;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Stream;

/**
 * Layout instance, and Registry of layouts for the mod
 * TODO: Separate the registry
 */
@ToString
@AllArgsConstructor
@Accessors(fluent = true)
public class KeyLayout {
    /**
     * Where the default layouts are located
     */
    private static final     String                     LAYOUT_ROOT = "assets/keymap/layouts";
    /**
     * The default layout (en_us (iirc))
     * TODO: Check if this is even used at all
     */
    @Getter protected static KeyLayout                  layoutDefault;
    /**
     * The current layout
     * TODO: Check if this is even used at all
     */
    @Getter protected static KeyLayout                  layoutCurrent;
    /**
     * List of all registered layouts and their name codes
     */
    @Getter protected static HashMap<String, KeyLayout> layouts     = new HashMap<>();

    /**
     * The metadata for this layout instance
     */
    @Getter @Setter protected KeyMeta meta;
    /**
     * The actual key groups
     */
    @Getter @Setter protected Keys    keys;

    /**
     * Registers a layout
     *
     * @param layout The layout to register
     */
    public static void registerLayout(KeyLayout layout) {
        layouts.put(layout.meta.code, layout);
        updateMouseKeys(layout.keys.mouse());
        updateMouseKeys(layout.keys.basic());
        updateMouseKeys(layout.keys.numpad());
        updateMouseKeys(layout.keys.extra());
    }

    /**
     * Assures all the items under the mouse group, that they are, indeed part of the mouse group
     *
     * @param rows The rows under the mouse group, or any other group for that matter
     */
    protected static void updateMouseKeys(List<KeyRow> rows) {
        for (KeyRow row : rows) {
            for (KeyData key : row.row) {
                if (key.code() < 10) {
                    key.mouse(true);
                }
            }
        }
    }

    /**
     * Loads all the predefined layouts from the mod
     */
    public static void loadKeys() {
        layouts.clear();

        GsonBuilder  builder = new GsonBuilder().setPrettyPrinting();
        Gson         gson    = builder.create();
        ClassLoader  loader  = KeyLayout.class.getClassLoader();
        Stream<Path> files   = null;
        FileSystem   fs;

        try {
            Keymap.logger().warn(loader.getResource("/"));
            Keymap.logger().warn(loader.getResource(LAYOUT_ROOT));
            URI  layoutUri = Objects.requireNonNull(Objects.requireNonNull(loader.getResource(LAYOUT_ROOT)).toURI());
            Keymap.logger().warn("Keymap layout stream: {}", layoutUri);
            Keymap.logger().warn("Keymap layout stream scheme: {}", layoutUri.getScheme());
            Path path;
            if (layoutUri.getScheme().equals("jar")) {
                fs = getFileSystem(layoutUri);
                path = fs.getPath(LAYOUT_ROOT);
            } else {
                path = Path.of(layoutUri);
            }

            files = Files.walk(path, 1);

            for (Iterator<Path> it = files.iterator(); it.hasNext(); ) {
                Path p = it.next();
                if (!p.getFileName().toString().endsWith(".json")) continue;
                tryLoadLayout(gson, loader, p);
            }
        } catch (Exception e) {
            Keymap.logger().error(e.getMessage());
            e.printStackTrace();
        } finally {
            if (files != null) files.close();
        }
    }

    private static void tryLoadLayout(Gson gson, ClassLoader loader, Path p) {
        try (InputStreamReader reader =
                     new InputStreamReader(Objects.requireNonNull(loader.getResourceAsStream(p.toString())), StandardCharsets.UTF_8)) {
            registerLayout(gson.fromJson(reader, KeyLayout.class));
        } catch (Exception e) {
            Keymap.logger().warn("Can't load {} ; {}", p.getFileName(), e.getMessage());
            e.printStackTrace();
        }
    }

    private static FileSystem getFileSystem(URI layoutUri) throws IOException {
        FileSystem fs;
        try {
            fs = FileSystems.getFileSystem(layoutUri);
            Keymap.logger().info("GET_FS");
        } catch (Exception e) {
            fs = FileSystems.newFileSystem(layoutUri, Collections.emptyMap());
            Keymap.logger().info("NEW_FS");
        }
        return fs;
    }

    /**
     * Should return the current layout, iirc is not used at all
     * TODO: Use or remove, prolly use, idk
     *
     * @return The current layout
     */
    public static KeyLayout getCurrentLayout() {
        return layoutCurrent;
    }

    /**
     * Gets a layout based on its name code
     *
     * @param code The name code of the layout
     *
     * @return The chosen layout, or english if not found
     */
    public static KeyLayout getLayoutWithCode(String code) {
        if (layouts.containsKey(code)) {
            return layouts.get(code);
        } else {
            Keymap.logger().warn("Cannot find layout for [{}], defaulting to en_us", code);
            return layouts.get("en_us");
        }
    }
}
