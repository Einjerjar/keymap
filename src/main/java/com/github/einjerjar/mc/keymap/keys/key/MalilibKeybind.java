package com.github.einjerjar.mc.keymap.keys.key;

import com.github.einjerjar.mc.keymap.KeymapMain;
import com.github.einjerjar.mc.keymap.keys.KeybindHolder;
import com.github.einjerjar.mc.keymap.utils.Utils;
import fi.dy.masa.malilib.hotkeys.IHotkey;
import net.minecraft.client.util.InputUtil;
import net.minecraft.text.Text;

import java.util.List;

public class MalilibKeybind extends KeybindHolder {
    public String modName = null;
    IHotkey hotkey;
    List<Integer> keyCode;
    Text boundKeyTranslation;
    Text translation;
    String translationKey;

    public MalilibKeybind(IHotkey hotkey) {
        this.hotkey = hotkey;
        updateState();
    }

    public String getKeysString(boolean format) {
        String b = String.join(" + ", hotkey.getKeybind().getKeys().stream().map(i -> (i < 0 ? InputUtil.Type.MOUSE.createFromCode((i + 100)) : InputUtil.Type.KEYSYM.createFromCode(i)).getLocalizedText().getString()).toList());
        return format ? " §a[" + b + "]" : b;
    }

    public String getKeysString() {
        return getKeysString(true);
    }

    public void setModName(String modName) {
        this.modName = modName;
        this.translationKey = (KeymapMain.cfg().keyButtonModName ? "[" + modName + "] " : "") + this.hotkey.getPrettyName() + (KeymapMain.cfg().keyButtonMalilibKeybinds ? getKeysString() : "");
    }

    @Override
    public List<Integer> code() {
        return keyCode;
    }

    @Override
    public Text keyTranslation() {
        return boundKeyTranslation;
    }

    @Override
    public Text translation() {
        return translation;
    }

    @Override
    public String translationKey() {
        return translationKey;
    }

    @Override
    public void assignHotKey(Integer[] hotkeys, boolean mouse) {
        hotkey.getKeybind().getKeys().clear();
        if (hotkeys.length != 0) {
            for (int i : hotkeys) {
                hotkey.getKeybind().addKey(i);
            }
        }
        updateState();
    }

    @Override
    public void updateState() {
        InputUtil.Key firstKey = InputUtil.Type.KEYSYM.createFromCode(Utils.safeGet(hotkey.getKeybind().getKeys(), 0, -1));
        this.boundKeyTranslation = firstKey.getLocalizedText();
        this.translation = Text.of(hotkey.getConfigGuiDisplayName());
        // this.translationKey = "config.name." + hotkey.getPrettyName().toLowerCase();
        this.keyCode = hotkey.getKeybind().getKeys();

        if (modName != null) {
            this.translationKey = (KeymapMain.cfg().keyButtonModName ? "[" + modName + "] " : "") + hotkey.getPrettyName() + (KeymapMain.cfg().keyButtonMalilibKeybinds ? getKeysString() : "");
        } else {
            this.translationKey = hotkey.getPrettyName() + (KeymapMain.cfg().keyButtonMalilibKeybinds ? getKeysString() : "");
        }
    }

    @Override
    public void resetHotkey() {
        hotkey.getKeybind().resetToDefault();
        updateState();
    }
}
