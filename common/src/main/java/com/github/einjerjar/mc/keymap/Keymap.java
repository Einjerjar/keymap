package com.github.einjerjar.mc.keymap;

import com.github.einjerjar.mc.keymap.config.KeymapConfig;
import com.github.einjerjar.mc.keymap.cross.CrossKeybindShared;
import com.github.einjerjar.mc.keymap.cross.IntegrationRegistrarShared;
import com.github.einjerjar.mc.keymap.cross.TickEventRegistrarShared;
import com.github.einjerjar.mc.keymap.keys.layout.KeyLayout;
import com.github.einjerjar.mc.keymap.keys.sources.category.CategorySources;
import com.github.einjerjar.mc.keymap.keys.sources.keymap.KeymapSources;
import com.mojang.blaze3d.platform.InputConstants;
import lombok.Getter;
import lombok.experimental.Accessors;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Accessors(fluent = true, chain = true)
public class Keymap {
    public static final            String             MOD_ID      = "keymap";
    public static final            String             SERVER_WARN = "Keymap is being ran on a DedicatedServer environment, even though it can only work on Client side environment";
    @Getter protected static final String             MOD_NAME    = "keymap";
    @Getter protected static final Logger             logger      = LogManager.getLogger();
    @Getter protected static       CrossKeybindShared kmOpenMapper;

    private Keymap() {
    }

    public static void init() {
        KeymapConfig.load();
        logger.info(KeymapConfig.instance().toString());

        kmOpenMapper = new CrossKeybindShared(
                InputConstants.Type.KEYSYM,
                InputConstants.KEY_GRAVE,
                "keymap.keyOpenKeymap",
                "keymap.keyCat"
        );

        KeyLayout.loadKeys();

        for (KeyLayout keyLayout : KeyLayout.layouts().values()) {
            logger.info("Layout for {} @ {}",
                    keyLayout.meta().code(),
                    keyLayout.meta().name());
        }

        KeymapSources.collect();
        CategorySources.collect();

        TickEventRegistrarShared.provider().execute();
        IntegrationRegistrarShared.provider().execute();
    }

}