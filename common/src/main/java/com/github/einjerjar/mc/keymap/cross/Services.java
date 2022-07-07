package com.github.einjerjar.mc.keymap.cross;

import com.github.einjerjar.mc.keymap.cross.services.IKeybindHelper;
import com.github.einjerjar.mc.keymap.cross.services.IPlatformHelper;
import com.github.einjerjar.mc.keymap.cross.services.ITickHelper;

import java.util.ServiceLoader;

public class Services {
    // Was planning on having multiple serviceloader, but apparently it wasn't
    //  as magical as I thought it out to be, you still need to tell java which
    //  classes can be loaded by the service loader, so I just went with this weird
    //  pattern.

    /**
     * Abstracts common platform utilities for each loader
     */
    public static final IPlatformHelper PLATFORM = load(IPlatformHelper.class);

    /**
     * Abstracts the creation of keybinds, should probably get merged with plaform,
     *  but eh, probably next time
     */
    public static final IKeybindHelper  KEYBIND  = PLATFORM.keybindHelper();

    /**
     * Kinda hacky way of dealing with events, should probably scrap this
     *  and go with loader specific versions instead
     */
    public static final ITickHelper     TICK     = PLATFORM.tickHelper();

    private Services() {}

    /**
     * Loads a class via the ServiceLoader, throws a RuntimeException when there is
     *  no implementation for the class
     * @param c The class to load
     * @return  An instance of the class
     * @param <T>
     */
    public static <T> T load(Class<T> c) {
        return ServiceLoader.load(c)
                .findFirst()
                .orElseThrow(() ->
                        new RuntimeException(String.format("Can't load service for %s", c.getName()))
                );
    }
}
