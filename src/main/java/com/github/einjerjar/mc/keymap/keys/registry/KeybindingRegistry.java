package com.github.einjerjar.mc.keymap.keys.registry;

import com.github.einjerjar.mc.keymap.keys.registry.keymap.KeymapRegistry;
import com.github.einjerjar.mc.keymap.keys.registry.keymap.KeymapSource;
import com.github.einjerjar.mc.keymap.keys.wrappers.holders.KeyHolder;
import lombok.Getter;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Accessors(fluent = true, chain = true)
public class KeybindingRegistry {
    @Getter protected static Map<Integer, List<KeyHolder>>                    keys        = new HashMap<>();
    @Getter protected static Map<Integer, List<KeybindingRegistrySubscriber>> subscribers = new HashMap<>();

    public static void load() {
        subscribers.clear();
        loadWithoutClearingSubscribers();
    }

    public static void clearSubscribers() {
        subscribers().clear();
    }

    public static void loadWithoutClearingSubscribers() {
        keys.clear();
        if (!KeymapRegistry.collected()) KeymapRegistry.collect();
        for (KeymapSource s : KeymapRegistry.sources()) {
            for (KeyHolder k : s.getKeyHolders()) {
                Integer keyCode = (k.getCode().get(0));
                addKey(keyCode, k);
            }
        }
    }

    public static void notifyAllSubscriber() {
        notifyAllSubscriber(false);
    }

    public static void notifyAllSubscriber(boolean selected) {
        for (List<KeybindingRegistrySubscriber> s : subscribers.values()) {
            for (KeybindingRegistrySubscriber ss : s) {
                ss.keybindingRegistryUpdated(selected);
            }
        }
    }

    public static void notifySubscriber(int keyCode, boolean selected) {
        if (subscribers().containsKey(keyCode)) {
            for (KeybindingRegistrySubscriber ss : subscribers().get(keyCode)) {
                ss.keybindingRegistryUpdated(selected);
            }
        }
    }

    public static void addListener(Integer key, KeybindingRegistrySubscriber subscriber) {
        subscribers.putIfAbsent(key, new ArrayList<>());
        if (!subscribers.get(key).contains(subscriber)) {
            subscribers.get(key).add(subscriber);
        }
    }

    public static void removeListener(Integer key, KeybindingRegistrySubscriber subscriber) {
        if (!subscribers.containsKey(key)) return;
        subscribers.get(key).remove(subscriber);
    }

    private static void addKey(Integer newCode, KeyHolder k) {
        keys.putIfAbsent(newCode, new ArrayList<>());
        if (!keys.get(newCode).contains(k)) {
            keys.get(newCode).add(k);
        }
    }

    public static void updateKey(Integer lastCode, Integer newCode, KeyHolder k) {
        if (keys.containsKey(lastCode)) {
            keys.get(lastCode).remove(k);
        }
        addKey(newCode, k);
        if (subscribers().containsKey(lastCode)) {
            for (KeybindingRegistrySubscriber s : subscribers().get(lastCode)) {
                s.keybindingRegistryUpdated(false);
            }
        }
        if (subscribers().containsKey(newCode)) {
            for (KeybindingRegistrySubscriber s : subscribers().get(newCode)) {
                s.keybindingRegistryUpdated(false);
            }
        }
    }

    public interface KeybindingRegistrySubscriber {
        void keybindingRegistryUpdated(boolean selected);
    }
}
