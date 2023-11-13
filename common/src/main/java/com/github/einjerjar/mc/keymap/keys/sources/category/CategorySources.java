package com.github.einjerjar.mc.keymap.keys.sources.category;

import com.github.einjerjar.mc.keymap.Keymap;
import lombok.Getter;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.List;

/**
 * Registry for category sources
 */
@Accessors(fluent = true)
public class CategorySources {
    /**
     * Known list of sources
     */
    @Getter
    protected static List<CategorySource> sources = new ArrayList<>();
    /**
     * Whether the collect function has been called once
     */
    @Getter
    protected static boolean collected = false;

    private CategorySources() {}

    /**
     * Gathers the known category sources, should really only be called once to avoid
     * externally registered sources from disappearing, but eh
     * TODO: Make into a Service
     * TODO: Laugh at the reflections library, but also, wail at how tedious services are
     */
    public static void collect() {
        if (collected) {
            Keymap.logger().warn("CategoryRegistry collect() already called!");
            sources.clear();
        }

        register(new AllCategorySource());
        register(new VanillaCategorySource());

        collected = true;
    }

    /**
     * Registers a source
     *
     * @param source The source to register
     */
    public static void register(CategorySource source) {
        Keymap.logger().info("Registered CategorySource: {}", source.getClass().getName());
        if (sources.contains(source)) return;
        sources.add(source);
    }
}
