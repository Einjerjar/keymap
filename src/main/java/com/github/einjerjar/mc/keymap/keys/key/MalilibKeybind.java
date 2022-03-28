package com.github.einjerjar.mc.keymap.keys.key;

import com.github.einjerjar.mc.keymap.keys.KeybindHolder;
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

    public MalilibKeybind(ConfigHotkey hotkey) {
        this.hotkey = hotkey;
        InputUtil.Key firstKey = InputUtil.Type.KEYSYM.createFromCode(hotkey.getKeybind().getKeys().get(0));
        this.boundKeyTranslation = firstKey.getLocalizedText();
        this.translation = new LiteralText(hotkey.getConfigGuiDisplayName());
        this.translationKey = "config.name." + hotkey.getName().toLowerCase();
        this.keyCode = hotkey.getKeybind().getKeys();
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
}
