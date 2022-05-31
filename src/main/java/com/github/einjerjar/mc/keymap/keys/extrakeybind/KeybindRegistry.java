package com.github.einjerjar.mc.keymap.keys.extrakeybind;

import com.github.einjerjar.mc.keymap.Keymap;
import lombok.Getter;
import lombok.experimental.Accessors;
import net.minecraft.client.KeyMapping;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Objects;

@Accessors(fluent = true, chain = true)
public class KeybindRegistry {
    protected static KeybindRegistry instance;
    // Integer is hashcode of keycode + modifier booleans
    @Getter protected static HashMap<Integer, KeyMapping> bindMap = new HashMap<>();
    protected HashMap<KeyMapping, KeybindData> bindings = new HashMap<>();

    public static KeybindRegistry instance() {
        if (instance == null) instance = new KeybindRegistry();
        return instance;
    }

    private static int getDataHash(@NotNull KeybindData data) {
        return Objects.hash(data.keyCode(), data.ctrl(), data.alt(), data.shift());
    }

    // annoying for the sake of sanity
    public static void register(@NotNull KeyMapping kb, @NotNull KeybindData data) {
        if (!instance().bindings.containsKey(kb)) {
            instance().bindings.put(kb, data);
            int hashCode = getDataHash(data);
            bindMap.put(hashCode, kb);
        } else
            Keymap.logger().warn("Registration ignored, keybind [{}] already registered, use update instead", kb.getTranslatedKeyMessage());
    }

    // annoying for the sake of sanity
    public static void update(@NotNull KeyMapping kb, @NotNull KeybindData data) {
        if (instance().bindings.containsKey(kb)) {
            int hashCode1 = getDataHash(instance().bindings.get(kb));
            int hashCode2 = getDataHash(data);
            instance().bindings.put(kb, data);
            bindMap.remove(hashCode1);
            bindMap.put(hashCode2, kb);
        } else Keymap.logger().warn("Update ignored, keybind [{}] not registered", kb.getTranslatedKeyMessage());
    }
}
