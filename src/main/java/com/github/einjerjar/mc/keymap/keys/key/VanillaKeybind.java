package com.github.einjerjar.mc.keymap.keys.key;

import com.github.einjerjar.mc.keymap.keys.KeybindHolder;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;

import java.util.Collections;
import java.util.List;

public class VanillaKeybind extends KeybindHolder {
    KeyBinding key;
    List<Integer> keyCode;
    Text boundKeyTranslation;
    Text translation;
    String translationKey;

    public VanillaKeybind(KeyBinding key) {
        this.key = key;
        this.keyCode = Collections.singletonList(key.boundKey.getCode());
        this.boundKeyTranslation = key.getBoundKeyLocalizedText();
        this.translation = new TranslatableText(key.getTranslationKey());
        this.translationKey = key.getTranslationKey();
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
    public void assignHotKey(Integer[] hotkey, boolean mouse) {
        int           hk;
        InputUtil.Key newKey;
        if (hotkey.length != 0) {
            hk = hotkey[0];
            if (hk == InputUtil.GLFW_KEY_ESCAPE) {
                newKey = InputUtil.UNKNOWN_KEY;
            } else {
                newKey = mouse
                         ? InputUtil.Type.MOUSE.createFromCode(hk)
                         : InputUtil.Type.KEYSYM.createFromCode(hk);
            }
        } else {
            newKey = InputUtil.UNKNOWN_KEY;
        }
        key.setBoundKey(newKey);

        updateState();
    }

    @Override
    public void resetHotkey() {
        key.setBoundKey(key.getDefaultKey());

        updateState();

        KeyBinding.updateKeysByCode();
    }

    @Override
    public void updateState() {
        // CLog.info("UPDATESTATE" + key.getBoundKeyLocalizedText().getString());
        this.keyCode = Collections.singletonList(key.boundKey.getCode());
        this.boundKeyTranslation = key.getBoundKeyLocalizedText();
        this.translation = new TranslatableText(key.getTranslationKey());
        this.translationKey = key.getTranslationKey();
    }
}
