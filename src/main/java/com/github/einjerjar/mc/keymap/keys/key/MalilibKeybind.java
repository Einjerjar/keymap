package com.github.einjerjar.mc.keymap.keys.key;

import com.github.einjerjar.mc.keymap.keys.KeybindHolder;
import com.github.einjerjar.mc.keymap.utils.Utils;
import fi.dy.masa.malilib.config.options.ConfigHotkey;
import fi.dy.masa.malilib.event.InputEventHandler;
import net.minecraft.client.util.InputUtil;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;

import java.util.List;

public class MalilibKeybind implements KeybindHolder {
    ConfigHotkey hotkey;
    List<Integer> keyCode;
    Text boundKeyTranslation;
    Text translation;
    String translationKey;
    String modName = "";

    public MalilibKeybind(ConfigHotkey hotkey) {
        this.hotkey = hotkey;
        InputUtil.Key firstKey = InputUtil.Type.KEYSYM.createFromCode(Utils.safeGet(hotkey.getKeybind().getKeys(), 0, -1));
        this.boundKeyTranslation = firstKey.getLocalizedText();
        this.translation = new LiteralText(hotkey.getConfigGuiDisplayName());
        // this.translationKey = "config.name." + hotkey.getName().toLowerCase();
        this.translationKey = hotkey.getName() + getKeysString();
        this.keyCode = hotkey.getKeybind().getKeys();
    }

    public String getKeysString(boolean format) {
        String b = String.join(" + ", hotkey.getKeybind().getKeys().stream().map(i -> {
            String a = (i < 0
                        ? InputUtil.Type.MOUSE.createFromCode((i + 100))
                        : InputUtil.Type.KEYSYM.createFromCode(i)).getLocalizedText().getString();
            return a;
        }).toList());
        return format
               ? " §a[" + b + "]"
               : b;
    }

    public String getKeysString() {
        return getKeysString(true);
    }

    public void setModName(String modName) {
        this.modName = modName;
        this.translationKey = "[" + modName + "] " + this.hotkey.getName() + getKeysString();
    }

    @Override
    public List<Integer> getCode() {
        return keyCode;
    }

    @Override
    public Text getKeyTranslation() {
        return boundKeyTranslation;
    }

    @Override
    public Text getTranslation() {
        return translation;
    }

    @Override
    public String getTranslationKey() {
        return translationKey;
    }

    @Override
    public void assignHotKey(int[] hotkeys, boolean mouse) {
        InputEventHandler handler = (InputEventHandler) InputEventHandler.getInputManager();
        hotkey.getKeybind().getKeys().clear();
        for (int i : hotkeys) {
            hotkey.getKeybind().addKey(i);
        }
    }

    @Override
    public void resetHotkey() {

    }
}
