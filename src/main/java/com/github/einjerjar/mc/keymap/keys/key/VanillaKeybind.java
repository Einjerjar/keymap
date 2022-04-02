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
    public void assignHotKey(Integer[] hotkey, boolean mouse) {
        int           hk = hotkey[0];
        InputUtil.Key newKey;
        if (hk == InputUtil.GLFW_KEY_ESCAPE) {
            newKey = InputUtil.UNKNOWN_KEY;
        } else {
            newKey = mouse
                     ? InputUtil.Type.MOUSE.createFromCode(hk)
                     : InputUtil.Type.KEYSYM.createFromCode(hk);
        }
        key.setBoundKey(newKey);

        this.keyCode = Collections.singletonList(key.boundKey.getCode());
        this.boundKeyTranslation = key.getBoundKeyLocalizedText();
        this.translation = new TranslatableText(key.getTranslationKey());
        this.translationKey = key.getTranslationKey();

        KeyBinding.updateKeysByCode();
    }

    @Override
    public void resetHotkey() {
        key.setBoundKey(key.getDefaultKey());

        this.keyCode = Collections.singletonList(key.boundKey.getCode());
        this.boundKeyTranslation = key.getBoundKeyLocalizedText();
        this.translation = new TranslatableText(key.getTranslationKey());
        this.translationKey = key.getTranslationKey();

        KeyBinding.updateKeysByCode();
    }
}
