package com.github.einjerjar.mc.keymap.keys.extrakeybind;

import com.github.einjerjar.mc.keymap.Keymap;
import com.github.einjerjar.mc.keymap.config.KeymapConfig;
import com.github.einjerjar.mc.keymap.cross.Services;
import com.github.einjerjar.mc.keymap.keys.sources.keymap.KeymapSource;
import com.github.einjerjar.mc.keymap.keys.sources.keymap.KeymapSources;
import com.github.einjerjar.mc.keymap.keys.wrappers.keys.KeyHolder;
import com.github.einjerjar.mc.keymap.keys.wrappers.keys.VanillaKeymap;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.common.collect.ImmutableBiMap;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mojang.blaze3d.platform.InputConstants;
import lombok.Getter;
import lombok.experimental.Accessors;
import net.minecraft.client.KeyMapping;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.lang.reflect.Type;
import java.util.*;

/**
 * Another key registry, this time for combo keys
 */
@Accessors(fluent = true, chain = true)
public class KeymapRegistry {
    /**
     * The expected output of the parse json, also the type of the actual registry
     */
    public static final            Type          TYPE_BIND_MAP_KEY = new TypeToken<Map<String, KeyComboData>>() {
    }.getType();
    /**
     * List of keys that are marked as modifiers
     */
    @Getter protected static final List<Integer> MODIFIER_KEYS     = Arrays.asList(InputConstants.KEY_LALT,
            InputConstants.KEY_RALT,
            InputConstants.KEY_LSHIFT,
            InputConstants.KEY_RSHIFT,
            InputConstants.KEY_LCONTROL,
            InputConstants.KEY_RCONTROL);

    /**
     * TODO: Make only one instance in a centralized location, Keymap class probably?
     */
    protected static final Gson                            gson       = new GsonBuilder().setPrettyPrinting().create();
    /**
     * The actual registry, bimap so we can search the other way around, also guava is lit
     */
    protected static final BiMap<KeyMapping, KeyComboData> bindMap    = HashBiMap.create();
    /**
     * The registry, except, the string keys of the keymaps, the data that is saved and loaded
     */
    protected static       Map<String, KeyComboData>       bindMapKey = new HashMap<>();
    /**
     * Reference to this class's config file
     */
    protected static       File                            cfgFile    = null;
    /**
     * Whether the load function has been run
     */
    protected static       boolean                         loaded     = false;

    private KeymapRegistry() {
    }

    /**
     * The config file instance, if not available create it
     *
     * @return The config file instance
     */
    protected synchronized static File cfgFile() {
        return cfgFile != null ? cfgFile : Services.PLATFORM.config("keymap-keys.json");
    }

    /**
     * @return Immutable reference to the registry
     */
    public static BiMap<KeyMapping, KeyComboData> bindMap() {
        return ImmutableBiMap.copyOf(bindMap);
    }

    /**
     * @return Immutable reference to the lightweight registry
     */
    public static Map<String, KeyComboData> bindMapKey() {
        return Collections.unmodifiableMap(bindMapKey);
    }

    /**
     * Load the saved combos
     */
    public static synchronized void load() {
        load(false);
    }

    /**
     * Load the saved combos
     *
     * @param force Whether to reload the saved keys, even if it has already been loaded once
     */
    public static synchronized void load(boolean force) {
        if (loaded && !force) return;
        try (FileReader reader = new FileReader(cfgFile())) {
            bindMapKey = gson.fromJson(reader, TYPE_BIND_MAP_KEY);
            Set<String> keys = bindMapKey.keySet();

            for (KeymapSource source : KeymapSources.sources()) {
                if (!source.canUseSource()) continue;

                List<KeyHolder> keyHolders = source.getKeyHolders();
                if (keyHolders.isEmpty() || !(keyHolders.get(0) instanceof VanillaKeymap)) continue;

                List<VanillaKeymap> vk = keyHolders.stream().map(VanillaKeymap.class::cast).toList();

                for (VanillaKeymap v : vk) {
                    if (keys.contains(v.map().getName())) {
                        add(v.map(), bindMapKey.get(v.map().getName()));
                    }
                }
            }
        } catch (FileNotFoundException e) {
            Keymap.logger().info("No keymap complex-key save found");
        } catch (Exception e) {
            Keymap.logger().error("!! Can't read keymap's complex-key save !!");
            e.printStackTrace();
        } finally {
            loaded = true;
        }
    }

    /**
     * Saves the combos
     */
    public static synchronized void save() {
        try (FileWriter writer = new FileWriter(cfgFile())) {
            gson.toJson(bindMapKey(), writer);
        } catch (Exception e) {
            Keymap.logger().error("!! Can't save key config !!");
            e.printStackTrace();
        }
    }

    /**
     * Map a keymap to a combo
     * TODO: Consistency between keybind and keymap
     *
     * @param kb The Keymap/Keybind
     * @param kd The combo
     */
    public static void add(@NotNull KeyMapping kb, @NotNull KeyComboData kd) {
        if (bindMap.containsKey(kb)) {
            String msg = "Attempted to add an already existing keybind";

            if (KeymapConfig.instance().crashOnProblematicError()) throw new RuntimeException(msg);
            else Keymap.logger().error(msg);
        }

        bindMap.put(kb, kd);
        bindMapKey.put(kb.getName(), kd);
        Keymap.logger().info("Registered complex keybinding [{}]", kd);
        save();
    }

    /**
     * Updates a keymap to and its combo, or the other way around, idk
     * TODO: Check later, remove idk
     * TODO: Consistency between keybind and keymap
     *
     * @param kb The Keymap/Keybind
     * @param kd The combo
     */
    public static void put(@NotNull KeyMapping kb, @NotNull KeyComboData kd) {
        remove(kd);
        if (!bindMap.containsKey(kb)) {
            add(kb, kd);
            return;
        }
        bindMapKey.put(kb.getName(), kd);
        bindMap.put(kb, kd);
        Keymap.logger().info("Updated complex keybinding [{}]", kd);
        save();
    }

    /**
     * Removes a combo
     * TODO: Consistency between keybind and keymap
     *
     * @param kd The combo to remove
     */
    public static void remove(@NotNull KeyComboData kd) {
        if (bindMap.inverse().containsKey(kd)) remove(bindMap.inverse().get(kd));
    }

    /**
     * Removes a keymap
     * TODO: Consistency between keybind and keymap
     *
     * @param kb The Keymap/Keybind to remove
     */
    public static void remove(@NotNull KeyMapping kb) {
        if (!bindMap.containsKey(kb)) {
            Keymap.logger().warn("Remove ignored, Keymapping [{}] not registered!", kb.getName());
            return;
        }
        KeyComboData kd = bindMap.get(kb);
        bindMap.remove(kb);
        bindMapKey.remove(kb.getName());
        Keymap.logger().info("Removed complex keybinding [{}]", kd);
        save();
    }

    /**
     * Wipe registry
     */
    public static void resetAll() {
        bindMap.clear();
        bindMapKey.clear();
        save();
    }

    /**
     * Checks if keybind is registered
     *
     * @param kb The Keymap/Keybind to look for
     *
     * @return Whether the check succeeded
     */
    public static boolean contains(@NotNull KeyMapping kb) {
        return bindMap.containsKey(kb);
    }

    /**
     * Checks if a combo is registered
     *
     * @param k The combo to look for
     *
     * @return Whether the check succeeded
     */
    public static boolean containsKey(int k) {
        for (Map.Entry<KeyMapping, KeyComboData> entry : bindMap.entrySet()) {
            if (entry.getValue().keyCode == k) return true;
        }

        return false;
    }

    /**
     * Get a list of keymaps bound to a key
     *
     * @param k The key to look for
     *
     * @return The keymaps
     */
    public static List<KeyMapping> getMappings(int k) {
        List<KeyMapping> m = new ArrayList<>();

        for (Map.Entry<KeyMapping, KeyComboData> entry : bindMap.entrySet()) {
            if (entry.getValue().keyCode == k) m.add(entry.getKey());
        }

        return m;
    }
}
