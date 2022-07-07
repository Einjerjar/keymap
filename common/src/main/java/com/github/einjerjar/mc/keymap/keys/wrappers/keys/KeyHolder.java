package com.github.einjerjar.mc.keymap.keys.wrappers.keys;

import com.github.einjerjar.mc.keymap.keys.extrakeybind.KeyComboData;
import net.minecraft.network.chat.Component;

import java.util.List;

/**
 * A generic interface to allow support for different keymappings from minecraft, and other mods
 */
public interface KeyHolder {
    /**
     * @return The series of keyCodes that triggers the keybind
     */
    List<Integer> getCode();

    /**
     * @return The first keyCode of the keyCodes for the keybind
     */
    Integer getSingleCode();

    /**
     * @return The hash of the keys, should prolly remove
     */
    Integer getKeyHash();

    /**
     * @return Whether the keybind is simple (single key) or complex (multikey)
     */
    boolean isComplex();

    /**
     * @return TODO: REMOVE
     */
    KeyComboData getComplexCode();

    /**
     * @return The translation key for the keybind
     */
    String getTranslatableName();

    /**
     * @return The category for the keybind
     */
    String getCategory();

    /**
     * @return The translated name of the keybind as a component
     */
    Component getTranslatedName();

    /**
     * @return The translation key for the assigned key
     */
    String getTranslatableKey();

    /**
     * @return The translated name for the assigned key
     */
    Component getTranslatedKey();

    /**
     * @return A series of strings that can be used to filter the list of keybinds
     */
    String getSearchString();

    /**
     * Assign a new key for this keybind
     *
     * @param keys  The new key
     * @param mouse Whether the input is from a mouse
     *
     * @return If the assignment was a succcess
     */
    boolean setKey(List<Integer> keys, boolean mouse);

    /**
     * @return The name of the mod that provided this keybind
     */
    String getModName();

    /**
     * Resets the keybind to its default key
     *
     * @return Whether the action was a success
     */
    boolean resetKey();

    /**
     * @return Whether the keybind is currently assigned to a key
     */
    boolean isAssigned();
}
