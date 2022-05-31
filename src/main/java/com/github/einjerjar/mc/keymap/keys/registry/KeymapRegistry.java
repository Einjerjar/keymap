package com.github.einjerjar.mc.keymap.keys.registry;

import com.github.einjerjar.mc.keymap.Keymap;
import lombok.Getter;
import lombok.experimental.Accessors;
import org.reflections.Reflections;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Accessors(fluent = true, chain = true)
public class KeymapRegistry {
    private KeymapRegistry() {}

    @Getter protected static List<KeymapSource> sources = new ArrayList<>();

    public static void collect() {
        Reflections reflections = new Reflections("com.github.einjerjar.mc");
        List<Class<? extends KeymapSource>> subClasses = reflections.getSubTypesOf(KeymapSource.class).stream().toList();
        for (Class<? extends KeymapSource> subClass : subClasses) {
            try {
                Constructor<? extends KeymapSource> c = subClass.getConstructor();
                KeymapSource s = c.newInstance();
                register(s);
            } catch (Exception e) {
                Keymap.logger().error(e);
                e.printStackTrace();
            }
        }
    }

    public static void register(KeymapSource source) {
        if (sources.contains(source)) return;
        sources.add(source);
    }
}
