package com.github.einjerjar.mc.keymap.keys.sources;

import com.github.einjerjar.mc.keymap.Keymap;
import com.github.einjerjar.mc.keymap.config.KeymapConfig;
import com.github.einjerjar.mc.keymap.keys.sources.keymap.KeymapSource;
import com.github.einjerjar.mc.keymap.keys.sources.keymap.KeymapSources;
import com.github.einjerjar.mc.keymap.keys.wrappers.keys.KeyHolder;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

@Accessors(fluent = true, chain = true)
public class KeymappingNotifier {
    protected static final Multimap<Integer, KeyHolder>                    keys        = ArrayListMultimap.create();
    protected static final Multimap<Integer, KeybindingRegistrySubscriber> subscribers = ArrayListMultimap.create();

    public static Multimap<Integer, KeyHolder> keys() {
        return ImmutableMultimap.copyOf(keys);
    }

    public static Multimap<Integer, KeybindingRegistrySubscriber> subscribers() {
        return ImmutableMultimap.copyOf(subscribers);
    }

    public static boolean containsHolder(KeyHolder holder) {
        return keys.containsValue(holder);
    }

    public static boolean containsSubscriber(KeybindingRegistrySubscriber subscriber) {
        return subscribers.containsValue(subscriber);
    }

    public static void clearSubscribers() {
        subscribers.clear();
    }

    public static void loadKeys() {
        keys.clear();
        if (!KeymapSources.collected()) KeymapSources.collect();
        for (KeymapSource s : KeymapSources.sources()) {
            if (!s.canUseSource()) continue;
            for (KeyHolder holder : s.getKeyHolders()) {
                keys.put(holder.getSingleCode(), holder);
            }
        }
    }

    public static void load() {
        clearSubscribers();
        loadKeys();
    }

    public static void addKey(int code, KeyHolder holder) {
        if (containsHolder(holder)) {
            Keymap.logger().error("!! ADD_KEY IGNORED : REMOVE EXISTING HOLDER VALUE FIRST !!");
            return;
        }
        keys.put(code, holder);
    }

    public static void removeKey(int code, KeyHolder holder) {
        keys.remove(code, holder);
    }

    public static void notifyAllSubscriber() {
        notifyAllSubscriber(false);
    }

    public static void notifyAllSubscriber(boolean selected) {
        for (Map.Entry<Integer, KeybindingRegistrySubscriber> e : subscribers.entries()) {
            e.getValue().keybindingRegistryUpdated(selected);
        }
    }

    public static void notifySubscriber(int code, boolean selected) {
        if (!subscribers().containsKey(code)) return;

        for (KeybindingRegistrySubscriber subscriber : subscribers().get(code)) {
            subscriber.keybindingRegistryUpdated(selected);
        }
    }

    public static void addListener(Integer key, KeybindingRegistrySubscriber subscriber) {
        subscribers.put(key, subscriber);
    }

    public static void removeListener(Integer key, KeybindingRegistrySubscriber subscriber) {
        subscribers.remove(key, subscriber);
    }

    public static Integer keyOf(@NotNull KeyHolder holder) {
        if (!containsHolder(holder)) return -99;
        for (Map.Entry<Integer, KeyHolder> entry : keys.entries()) {
            if (entry.getValue().equals(holder)) return entry.getKey();
        }
        return -99;
    }

    public static void updateKey(Integer lastCode, Integer newCode, KeyHolder k) {
        removeKey(lastCode, k);

        if (containsHolder(k)) {
            String msg = String.format(
                    "KeyHolder was not removed by the last removeKey call! [lastCode=%d, newCode=%d, keyOf=%d, holder=%s]",
                    lastCode,
                    newCode,
                    keyOf(k),
                    k.getTranslatableName()
            );
            if (KeymapConfig.instance().crashOnProblematicError()) {
                throw (new RuntimeException(msg));
            } else {
                Keymap.logger().fatal(msg);
            }
        }

        addKey(newCode, k);

        for (KeybindingRegistrySubscriber s : subscribers().get(lastCode)) {
            s.keybindingRegistryUpdated(false);
        }
        for (KeybindingRegistrySubscriber s : subscribers().get(newCode)) {
            s.keybindingRegistryUpdated(false);
        }
    }

    public interface KeybindingRegistrySubscriber {
        void keybindingRegistryUpdated(boolean selected);
    }
}
