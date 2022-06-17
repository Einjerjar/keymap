package com.github.einjerjar.mc.keymap.keys.wrappers;

import com.github.einjerjar.mc.keymap.keys.extrakeybind.KeyComboData;
import com.github.einjerjar.mc.keymap.mixin.KeyMappingAccessor;
import com.mojang.blaze3d.platform.InputConstants;
import lombok.Getter;
import lombok.experimental.Accessors;
import net.minecraft.client.KeyMapping;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Accessors(fluent = true, chain = true)
public class VanillaKeymap implements KeyHolder {
    @Getter protected KeyMapping map;
    protected List<Integer> codes = new ArrayList<>();
    protected Component translatedName;
    protected Component translatedKey;
    protected boolean complex;
    public VanillaKeymap(KeyMapping map) {
        this.map = map;
        this.codes.add(((KeyMappingAccessor) map).getKey().getValue());
        this.translatedName = new TranslatableComponent(map.getName());
        this.translatedKey = ((KeyMappingAccessor) map).getKey().getDisplayName();
    }

    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        VanillaKeymap that = (VanillaKeymap) o;
        return complex == that.complex && map.equals(that.map) && codes.equals(that.codes);
    }

    @Override public int hashCode() {
        return Objects.hash(map, codes, complex);
    }

    @Override public List<Integer> getCode() {
        return codes;
    }

    @Override public Integer getKeyHash() {
        return Objects.hash(((KeyMappingAccessor) map).getKey().getValue());
    }

    @Override public boolean isComplex() {
        return complex;
    }

    @Override public KeyComboData getComplexCode() {
        return null;
    }

    @Override public String getTranslatableName() {
        return map.getName();
    }

    @Override public String getCategory() {
        return map.getCategory();
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
        setKey(key);
        KeyMapping.resetMapping();
        return true;
    }

    protected void setKey(InputConstants.Key key) {
        map.setKey(key);
        codes.clear();
        codes.add(key.getValue());
        translatedKey = key.getDisplayName();
    }

    @Override
    public boolean resetKey() {
        setKey(map.getDefaultKey());
        return true;
    }
}
