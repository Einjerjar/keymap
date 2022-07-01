package com.github.einjerjar.mc.keymap.keys.wrappers.keys;

import com.github.einjerjar.mc.keymap.keys.extrakeybind.KeyComboData;
import com.github.einjerjar.mc.keymap.keys.wrappers.categories.VanillaCategory;
import com.github.einjerjar.mc.keymap.utils.Utils;
import com.github.einjerjar.mc.widgets.utils.Text;
import com.mojang.blaze3d.platform.InputConstants;
import lombok.Getter;
import lombok.experimental.Accessors;
import net.minecraft.client.KeyMapping;
import net.minecraft.locale.Language;
import net.minecraft.network.chat.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Accessors(fluent = true, chain = true)
public class VanillaKeymap implements KeyHolder {
    @Getter protected KeyMapping    map;
    protected         List<Integer> codes = new ArrayList<>();
    protected         Component     translatedName;
    protected         Component     translatedKey;
    protected         boolean       complex;
    protected         String        searchString;

    public VanillaKeymap(KeyMapping map) {
        this.map = map;
        this.codes.add(map.key.getValue());
        this.translatedName = Text.translatable(map.getName());
        this.translatedKey  = map.key.getDisplayName();
        updateSearchString();
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
        return Objects.hash(map.key.getValue());
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
        return map.key.getName();
    }

    @Override public Component getTranslatedKey() {
        return translatedKey;
    }

    @Override public String getSearchString() {
        return searchString;
    }

    @Override public boolean setKey(List<Integer> keys, boolean mouse) {
        if (keys == null || keys.isEmpty()) return false;
        InputConstants.Type type = mouse ? InputConstants.Type.MOUSE : InputConstants.Type.KEYSYM;
        InputConstants.Key  key  = type.getOrCreate(keys.get(0));
        updateProps(key);
        KeyMapping.resetMapping();
        return true;
    }

    @Override public String getModName() {
        String s = getCategory();
        if (VanillaCategory.MC_CATEGORIES.contains(getCategory())) s = "advancements.story.root.title";
        return Language.getInstance().getOrDefault(s);
    }

    protected void updateSearchString() {
        String cat = Language.getInstance().getOrDefault(getCategory());
        searchString = String.format(
                "%s [%s] $%s {%s} #%s (%s) @%s",
                translatedName.getString(),                 // name
                translatedKey.getString(),                  // [key]
                Utils.slugify(translatedKey.getString()),   // $key-slug
                getModName(),                               // {mod}
                Utils.slugify(getModName()),                // #mod-slug
                cat,                                        // (cat)
                Utils.slugify(cat)                          // @cat-slug
        ).toLowerCase();
    }

    protected void updateProps(InputConstants.Key key) {
        map.setKey(key);
        codes.clear();
        codes.add(key.getValue());
        translatedKey = key.getDisplayName();
        updateSearchString();

    }

    @Override
    public boolean resetKey() {
        updateProps(map.getDefaultKey());
        return true;
    }

    @Override public boolean isAssigned() {
        return map.key.getValue() != -1;
    }
}
