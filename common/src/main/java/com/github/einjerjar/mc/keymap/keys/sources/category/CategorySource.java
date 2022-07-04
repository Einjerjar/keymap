package com.github.einjerjar.mc.keymap.keys.sources.category;

import com.github.einjerjar.mc.keymap.keys.wrappers.categories.CategoryHolder;

import java.util.List;

public interface CategorySource {
    List<CategoryHolder> getCategoryHolders();

    boolean canUseSource();
}
