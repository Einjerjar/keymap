package com.github.einjerjar.mc.keymap.cross;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Accessors(fluent = true)
public class TickEventRegistrarShared {
    @Getter @Setter static TickEventRegistrarProvider provider;

    @FunctionalInterface
    public interface TickEventRegistrarProvider {
        void execute();
    }
}
