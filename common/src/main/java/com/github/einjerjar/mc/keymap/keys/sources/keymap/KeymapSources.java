package com.github.einjerjar.mc.keymap.keys.sources.keymap;

import com.github.einjerjar.mc.keymap.Keymap;
import lombok.Getter;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.List;

/**
 * Registry for keymap sources
 */
@Accessors(fluent = true)
public class KeymapSources {
    /**
     * Known list of sources
     */
    @Getter protected static List<KeymapSource> sources   = new ArrayList<>();
    /**
     * Whether the collect function has been called once
     */
    @Getter protected static boolean            collected = false;

    private KeymapSources() {
    }

    /**
     * Gathers the known keymap sources, should really only be called once to avoid
     * externally registered sources from disappearing, but eh
     * TODO: Make into a Service
     * TODO: Laugh at the reflections library, but also, wail at how tedious services are
     */
    public static void collect() {
        if (collected) {
            Keymap.logger().warn("KeymapRegistry collect() already called!");
            sources.clear();
        }

        register(new VanillaKeymapSource());

        collected = true;
    }

    /**
     * Registers a source
     *
     * @param source The source to register
     */
    public static void register(KeymapSource source) {
        Keymap.logger().debug("Registered KeymapSource: {}", source.getClass().getName());
        if (sources.contains(source)) return;
        sources.add(source);
    }
}
