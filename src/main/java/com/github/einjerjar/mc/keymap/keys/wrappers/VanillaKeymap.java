package com.github.einjerjar.mc.keymap.keys.wrappers;

import com.github.einjerjar.mc.keymap.mixin.KeyMappingAccessor;
import com.mojang.blaze3d.platform.InputConstants;
import lombok.Getter;
import lombok.experimental.Accessors;
import net.minecraft.client.KeyMapping;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;

import java.util.ArrayList;
import java.util.List;

@Accessors(fluent = true, chain = true)
public class VanillaKeymap implements KeyHolder {
    @Getter protected KeyMapping map;
    protected List<Integer> codes = new ArrayList<>();
    protected Component translatedName;
    protected Component translatedKey;

    public VanillaKeymap(KeyMapping map) {
        this.map = map;
        this.codes.add(((KeyMappingAccessor) map).getKey().getValue());
        this.translatedName = new TranslatableComponent(map.getName());
        this.translatedKey = new TranslatableComponent(((KeyMappingAccessor) map).getKey().getName());
    }

    @Override public List<Integer> getCode() {
        return codes;
    }

    @Override public String getTranslatableName() {
        return map.getName();
    }

    @Override public Component getTranslatedName() {
        return translatedName;
    }

    @Override public String getTranslatableKey() {
        return ((KeyMappingAccessor) map).getKey().getName();
    }

    @Override public Component getTranslatedKey() {
        return translatedKey;
    }

    @Override public boolean setKey(List<Integer> keys, boolean mouse) {
        if (keys == null || keys.isEmpty()) return false;
        InputConstants.Type type = mouse ? InputConstants.Type.MOUSE : InputConstants.Type.KEYSYM;
        InputConstants.Key key = type.getOrCreate(keys.get(0));
        map.setKey(key);
        return true;
    }

    @Override
    public boolean resetKey() {
        map.setKey(map.getDefaultKey());
        return true;
    }
}
