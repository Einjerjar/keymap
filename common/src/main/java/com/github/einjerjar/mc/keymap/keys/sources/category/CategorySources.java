package com.github.einjerjar.mc.keymap.keys.sources.category;

import com.github.einjerjar.mc.keymap.Keymap;
import lombok.Getter;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.List;

@Accessors(fluent = true)
public class CategorySources {
    @Getter protected static List<CategorySource> sources   = new ArrayList<>();
    @Getter protected static boolean              collected = false;

    private CategorySources() {
    }

    public static void collect() {
        if (collected) {
            Keymap.logger().warn("CategoryRegistry collect() already called!");
            sources.clear();
        }

        register(new AllCategorySource());
        register(new VanillaCategorySource());

        collected = true;
    }

    public static void register(CategorySource source) {
        Keymap.logger().info("Registered CategorySource: {}", source.getClass().getName());
        if (sources.contains(source)) return;
        sources.add(source);
    }
}
