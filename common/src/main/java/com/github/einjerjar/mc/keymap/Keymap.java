package com.github.einjerjar.mc.keymap;

import com.github.einjerjar.mc.keymap.config.KeymapConfig;
import com.github.einjerjar.mc.keymap.keys.layout.KeyLayout;
import com.github.einjerjar.mc.keymap.keys.sources.category.CategorySources;
import com.github.einjerjar.mc.keymap.keys.sources.keymap.KeymapSources;
import com.mojang.blaze3d.platform.InputConstants;
import lombok.Getter;
import lombok.experimental.Accessors;
import net.minecraft.client.KeyMapping;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static com.github.einjerjar.mc.keymap.cross.Services.*;

@Accessors(fluent = true, chain = true)
public class Keymap {
    public static final            String     MOD_ID      = "keymap";
    public static final            String     SERVER_WARN = "Keymap is being ran on a DedicatedServer environment, even though it can only work on Client side environment";
    @Getter protected static final String     MOD_NAME    = "keymap";
    @Getter protected static final Logger     logger      = LogManager.getLogger();
    @Getter protected static       KeyMapping kmOpenMapper;

    private Keymap() {
    }

    /**
     * General mod setup
     */
    public static void init() {
        KeymapConfig.load();
        logger.info("Keymap loaded, loader={}, dev={}", PLATFORM.loader(), PLATFORM.dev());

        kmOpenMapper = KEYBIND.create(
                InputConstants.Type.KEYSYM,
                InputConstants.KEY_GRAVE,
                "keymap.keyOpenKeymap",
                "keymap.keyCat"
        );

        KeyLayout.loadKeys();

        for (KeyLayout keyLayout : KeyLayout.layouts().values()) {
            logger.debug("Layout for {} @ {}",
                    keyLayout.meta().code(),
                    keyLayout.meta().name());
        }

        KeymapSources.collect();
        CategorySources.collect();

        TICK.registerEndClientTick(client -> {
            while (kmOpenMapper.consumeClick()) {
                client.setScreen(PLATFORM.configScreen(null));
            }
        });
    }

}