package com.github.einjerjar.mc.keymap;

import com.github.einjerjar.mc.keymap.config.KeymapConfig;
import com.github.einjerjar.mc.keymap.keys.layout.KeyLayout;
import com.github.einjerjar.mc.keymap.keys.registry.KeymapRegistry;
import lombok.Getter;
import lombok.experimental.Accessors;
import net.fabricmc.api.ModInitializer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Accessors(fluent = true, chain = true)
public class Keymap implements ModInitializer {
    @Getter static final String MOD_ID = "keymap";
    @Getter static final String MOD_NAME = "keymap";
    @Getter static Logger logger = LogManager.getLogger();

    @Override
    public void onInitialize() {
        KeymapConfig.load();
        logger.info(KeymapConfig.instance().toString());

        KeyLayout.loadKeys();
        KeyLayout.layouts().forEach((s, keyLayout) -> logger.info("Layout for {} @ {}", keyLayout.meta().code(), keyLayout.meta().name()));

        KeymapRegistry.collect();
    }

}