package com.github.einjerjar.mc.keymap.keys.sources.category;

import com.github.einjerjar.mc.keymap.keys.wrappers.categories.CategoryHolder;

import java.util.List;

/**
 * Simple interface for sources of categories
 */
public interface CategorySource {
    /**
     * @return List of categoryHolders provided by this source
     */
    List<CategoryHolder> getCategoryHolders();

    /**
     * @return Whether for some reason or another, this source might not be available
     *         (eg; integration disabled)
     */
    boolean canUseSource();
}
