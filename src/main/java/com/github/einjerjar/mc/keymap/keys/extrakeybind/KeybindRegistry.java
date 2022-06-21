package com.github.einjerjar.mc.keymap.keys.extrakeybind;

import com.github.einjerjar.mc.keymap.Keymap;
import com.mojang.blaze3d.platform.InputConstants;
import lombok.Getter;
import lombok.experimental.Accessors;
import net.minecraft.client.KeyMapping;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

@Accessors(fluent = true, chain = true)
public class KeybindRegistry {
    @Getter protected static final List<Integer>                     MODIFIER_KEYS = Arrays.asList(InputConstants.KEY_LALT,
            InputConstants.KEY_RALT,
            InputConstants.KEY_LSHIFT,
            InputConstants.KEY_RSHIFT,
            InputConstants.KEY_LCONTROL,
            InputConstants.KEY_RCONTROL);
    protected static               KeybindRegistry                   instance;
    @Getter protected static       HashMap<KeyComboData, KeyMapping> bindMap       = new HashMap<>();
    protected                      HashMap<KeyMapping, KeyComboData> bindings      = new HashMap<>();

    public static KeybindRegistry instance() {
        if (instance == null) instance = new KeybindRegistry();
        return instance;
    }

    // annoying for the sake of my sanity
    public static void register(@NotNull KeyMapping kb, @NotNull KeyComboData kd) {
        if (instance().bindings.containsKey(kb)) {
            Keymap.logger().warn("Registration ignored, keybind [{}] already registered, use update instead",
                    kb.getTranslatedKeyMessage());
            return;
        }
        instance().bindings.put(kb, kd);
        bindMap.put(kd, kb);
        Keymap.logger().info("Registered complex keybinding [{}]", kd);
    }

    // annoying for the sake of my sanity
    public static void update(@NotNull KeyMapping kb, @NotNull KeyComboData kd) {
        if (!instance().bindings.containsKey(kb)) {
            Keymap.logger().warn("Update ignored, keybind [{}] not registered", kb.getTranslatedKeyMessage());
            return;
        }
        instance().bindings.put(kb, kd);
        bindMap.remove(instance().bindings.get(kb));
        bindMap.put(kd, kb);
        Keymap.logger().info("Updated complex keybinding [{}]", kd);
    }

    public static void remove(@NotNull KeyMapping kb) {
        if (!instance().bindings.containsKey(kb)) {
            Keymap.logger().warn("Remove ignore, Keymapping [{}] not registered!", kb.getName());
            return;
        }
        KeyComboData kd = instance().bindings.get(kb);
        bindMap().remove(kd);
        instance().bindings.remove(kb);
        Keymap.logger().info("Removed complex keybinding [{}]", kd);
    }

    public static boolean contains(@NotNull KeyMapping kb) {
        return instance().bindings.containsKey(kb);
    }
}
