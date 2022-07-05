package com.github.einjerjar.mc.keymap.keys.sources.category;

import com.github.einjerjar.mc.keymap.keys.wrappers.categories.AllCategory;
import com.github.einjerjar.mc.keymap.keys.wrappers.categories.CategoryHolder;

import java.util.List;

public class AllCategorySource implements CategorySource {


    @Override public List<CategoryHolder> getCategoryHolders() {
        return List.of(new AllCategory());
    }

    @Override public boolean canUseSource() {
        return true;
    }
}
