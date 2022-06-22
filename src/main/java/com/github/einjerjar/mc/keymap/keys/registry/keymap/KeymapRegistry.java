package com.github.einjerjar.mc.keymap.keys.registry.keymap;

import com.github.einjerjar.mc.keymap.Keymap;
import lombok.Getter;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.List;

@Accessors(fluent = true)
public class KeymapRegistry {
    @Getter protected static List<KeymapSource> sources   = new ArrayList<>();
    @Getter protected static boolean            collected = false;

    private KeymapRegistry() {
    }

    public static void collect() {
        if (collected) {
            Keymap.logger().warn("KeymapRegistry collect() already called!");
        }
        sources.clear();

        register(new VanillaKeymapSource());

        collected = true;
    }

    public static void register(KeymapSource source) {
        Keymap.logger().info("Registered KeymapSource: {}", source.getClass().getName());
        if (sources.contains(source)) return;
        sources.add(source);
    }
}
