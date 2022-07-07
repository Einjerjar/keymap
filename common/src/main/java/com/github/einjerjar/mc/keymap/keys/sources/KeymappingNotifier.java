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

/**
 * Wonky registry for vanilla keys
 */
@Accessors(fluent = true, chain = true)
public class KeymappingNotifier {
    /**
     * List of known assigned keys and their keybinds (yes, not the other way around)
     * Multimap since you can assign multiple binds to one key in vanilla mc
     */
    protected static final Multimap<Integer, KeyHolder> keys = ArrayListMultimap.create();

    /**
     * List of listeners that listens to changes within the &lt;keys&gt; variable,
     * allows for deferred updates
     */
    protected static final Multimap<Integer, KeybindingRegistrySubscriber> subscribers = ArrayListMultimap.create();

    /**
     * @return Immutable reference to all keys
     */
    public static Multimap<Integer, KeyHolder> keys() {
        return ImmutableMultimap.copyOf(keys);
    }

    /**
     * @return Immutable reference to all subscribers
     */
    public static Multimap<Integer, KeybindingRegistrySubscriber> subscribers() {
        return ImmutableMultimap.copyOf(subscribers);
    }

    /**
     * Checks if a specific keyholder is assigned to a key
     *
     * @param holder The holder to look for
     *
     * @return Whether the holder is assigned to a key
     */
    public static boolean containsHolder(KeyHolder holder) {
        return keys.containsValue(holder);
    }

    /**
     * Checks if a subscriber is already subscribed, no use afaik
     * TODO: Use or remove
     *
     * @param subscriber The subscriber o look for
     *
     * @return Whether the subscriber is found
     */
    public static boolean containsSubscriber(KeybindingRegistrySubscriber subscriber) {
        return subscribers.containsValue(subscriber);
    }

    /**
     * Clears the list of subscribers, specially useful to avoid persistent
     * references to discarded widgets
     */
    public static void clearSubscribers() {
        subscribers.clear();
    }

    /**
     * Load the keys to the map via the sources
     * TODO: This prolly isn't very flexible, and might only apply to vanilla
     */
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

    /**
     * Refreshes the notifier, kinda
     */
    public static void load() {
        clearSubscribers();
        loadKeys();
    }

    /**
     * Assign a holder to its key
     *
     * @param code   The key's code
     * @param holder The holder to assign
     */
    public static void addKey(int code, KeyHolder holder) {
        if (containsHolder(holder)) {
            Keymap.logger().error("!! ADD_KEY IGNORED : REMOVE EXISTING HOLDER VALUE FIRST !!");
            return;
        }
        keys.put(code, holder);
    }

    /**
     * Remove a holder from its current key
     *
     * @param code   The key's code
     * @param holder The holder to remove
     */
    public static void removeKey(int code, KeyHolder holder) {
        keys.remove(code, holder);
    }

    /**
     * Loops over all subscriber and forces them to update
     */
    public static void notifyAllSubscriber() {
        notifyAllSubscriber(false);
    }

    /**
     * Loops over all subscriber and forces them to update
     *
     * @param selected Whether the subscriber should mark itself as selected or not (visually)
     */
    public static void notifyAllSubscriber(boolean selected) {
        for (Map.Entry<Integer, KeybindingRegistrySubscriber> e : subscribers.entries()) {
            e.getValue().keybindingRegistryUpdated(selected);
        }
    }

    /**
     * Notifies all subscribers subscribed to a specific key
     *
     * @param code     The key's code
     * @param selected Whether the subscriber should mark itself as selected or not (visually)
     */
    public static void notifySubscriber(int code, boolean selected) {
        if (!subscribers().containsKey(code)) return;

        for (KeybindingRegistrySubscriber subscriber : subscribers().get(code)) {
            subscriber.keybindingRegistryUpdated(selected);
        }
    }

    /**
     * Adds a subscriber
     *
     * @param key        The key to subscribe to
     * @param subscriber The subscriber
     */
    public static void subscribe(Integer key, KeybindingRegistrySubscriber subscriber) {
        subscribers.put(key, subscriber);
    }

    /**
     * Removes a subscriber
     *
     * @param key        The key to subscribe to
     * @param subscriber The subscriber
     */
    public static void unsubscribe(Integer key, KeybindingRegistrySubscriber subscriber) {
        subscribers.remove(key, subscriber);
    }

    /**
     * Find the key that a holder is assigned to
     *
     * @param holder The holder to find
     *
     * @return The key the holder is assigned to
     */
    public static Integer keyOf(@NotNull KeyHolder holder) {
        if (!containsHolder(holder)) return -99;
        for (Map.Entry<Integer, KeyHolder> entry : keys.entries()) {
            if (entry.getValue().equals(holder)) return entry.getKey();
        }
        return -99;
    }

    /**
     * Updates the status of a holder, moves it from one key to another,
     * then finally notifies the subscriber of the previous and new keys
     *
     * @param lastCode The previous key
     * @param newCode  The new key
     * @param holder   The holder
     */
    public static void updateKey(Integer lastCode, Integer newCode, KeyHolder holder) {
        removeKey(lastCode, holder);

        if (containsHolder(holder)) {
            String msg = String.format(
                    "KeyHolder was not removed by the last removeKey call! [lastCode=%d, newCode=%d, keyOf=%d, holder=%s]",
                    lastCode,
                    newCode,
                    keyOf(holder),
                    holder.getTranslatableName()
            );
            if (KeymapConfig.instance().crashOnProblematicError()) {
                throw (new RuntimeException(msg));
            } else {
                Keymap.logger().fatal(msg);
            }
        }

        addKey(newCode, holder);

        for (KeybindingRegistrySubscriber s : subscribers().get(lastCode)) {
            s.keybindingRegistryUpdated(false);
        }
        for (KeybindingRegistrySubscriber s : subscribers().get(newCode)) {
            s.keybindingRegistryUpdated(false);
        }
    }

    /**
     * Any subscriber to this notifier
     */
    public interface KeybindingRegistrySubscriber {
        void keybindingRegistryUpdated(boolean selected);
    }
}
