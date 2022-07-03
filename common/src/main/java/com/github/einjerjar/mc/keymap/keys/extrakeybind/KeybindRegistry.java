package com.github.einjerjar.mc.keymap.keys.extrakeybind;

import com.github.einjerjar.mc.keymap.Keymap;
import com.github.einjerjar.mc.keymap.config.KeymapConfig;
import com.github.einjerjar.mc.keymap.keys.registry.keymap.KeymapRegistry;
import com.github.einjerjar.mc.keymap.keys.registry.keymap.KeymapSource;
import com.github.einjerjar.mc.keymap.keys.wrappers.keys.KeyHolder;
import com.github.einjerjar.mc.keymap.keys.wrappers.keys.VanillaKeymap;
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

@Accessors(fluent = true, chain = true)
public class KeybindRegistry {
    public static final            Type                          TYPE_BIND_MAP_KEY = new TypeToken<Map<String, KeyComboData>>() {
    }.getType();
    @Getter protected static final List<Integer>                 MODIFIER_KEYS     = Arrays.asList(InputConstants.KEY_LALT,
            InputConstants.KEY_RALT,
            InputConstants.KEY_LSHIFT,
            InputConstants.KEY_RSHIFT,
            InputConstants.KEY_LCONTROL,
            InputConstants.KEY_RCONTROL);
    protected static               Map<KeyComboData, KeyMapping> bindMap           = new HashMap<>();
    protected static               Map<String, KeyComboData>     bindMapKey        = new HashMap<>();
    protected static               Map<KeyMapping, KeyComboData> bindings          = new HashMap<>();
    protected static               Gson                          gson              = new GsonBuilder().setPrettyPrinting().create();
    protected static                 File                          cfgFile           = null;
    protected static boolean loaded = false;

    protected static File cfgFile() {
        return cfgFile != null ? cfgFile : KeymapConfig.configDirProvider().execute("keymap-keys.json");
    }

    public static Map<KeyComboData, KeyMapping> bindMap() {
        return Collections.unmodifiableMap(bindMap);
    }

    public static Map<String, KeyComboData> bindMapKey() {
        return Collections.unmodifiableMap(bindMapKey);
    }

    public static synchronized void load() {
        load(false);
    }

    public static synchronized void load(boolean force) {
        if (loaded && !force) return;
        try (FileReader reader = new FileReader(cfgFile())) {
            bindMapKey = gson.fromJson(reader, TYPE_BIND_MAP_KEY);
            Set<String> keys = bindMapKey.keySet();

            for (KeymapSource source : KeymapRegistry.sources()) {
                if (!source.canUseSource()) continue;

                List<KeyHolder> kh = source.getKeyHolders();
                if (kh.isEmpty()) continue;
                if (!(kh.get(0) instanceof VanillaKeymap)) continue;

                List<VanillaKeymap> vk = kh.stream().map(v -> (VanillaKeymap)v).toList();

                for (VanillaKeymap v : vk) {
                    if (keys.contains(v.map().getName())) {
                        register(v.map(), bindMapKey.get(v.map().getName()));
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

    public static synchronized void save() {
        try (FileWriter writer = new FileWriter(cfgFile())) {
            gson.toJson(bindMapKey(), writer);
        } catch (Exception e) {
            Keymap.logger().error("!! Can't save key config !!");
            e.printStackTrace();
        }
    }

    // annoying for the sake of my sanity
    public static void register(@NotNull KeyMapping kb, @NotNull KeyComboData kd) {
        if (bindings.containsKey(kb)) {
            Keymap.logger().warn("Registration ignored, keybind [{}] already registered, use update instead",
                    kb.getTranslatedKeyMessage());
            return;
        }
        bindings.put(kb, kd);
        bindMapKey.put(kb.getName(), kd);
        bindMap.put(kd, kb);
        Keymap.logger().info("Registered complex keybinding [{}]", kd);
        save();
    }

    // annoying for the sake of my sanity
    public static void update(@NotNull KeyMapping kb, @NotNull KeyComboData kd) {
        if (!bindings.containsKey(kb)) {
            Keymap.logger().warn("Update ignored, keybind [{}] not registered", kb.getTranslatedKeyMessage());
            return;
        }
        bindings.put(kb, kd);
        bindMapKey.put(kb.getName(), kd);
        bindMap.remove(bindings.get(kb));
        bindMap.put(kd, kb);
        Keymap.logger().info("Updated complex keybinding [{}]", kd);
        save();
    }

    public static void remove(@NotNull KeyMapping kb) {
        if (!bindings.containsKey(kb)) {
            Keymap.logger().warn("Remove ignore, Keymapping [{}] not registered!", kb.getName());
            return;
        }
        KeyComboData kd = bindings.get(kb);
        bindMap.remove(kd);
        bindings.remove(kb);
        bindMapKey.remove(kb.getName());
        Keymap.logger().info("Removed complex keybinding [{}]", kd);
        save();
    }

    public static boolean contains(@NotNull KeyMapping kb) {
        return bindings.containsKey(kb);
    }

    public static boolean containsKey(int k) {
        for (Map.Entry<KeyComboData, KeyMapping> e : bindMap.entrySet()) {
            if (e.getKey().keyCode() == k) {
                return true;
            }
        }

        return false;
    }

    public static List<KeyMapping> getMappings(int k) {
        List<KeyMapping> m = new ArrayList<>();

        for (Map.Entry<KeyComboData, KeyMapping> e : bindMap.entrySet()) {
            if (e.getKey().keyCode() == k) {
                m.add(e.getValue());
            }
        }

        return m;
    }
}
