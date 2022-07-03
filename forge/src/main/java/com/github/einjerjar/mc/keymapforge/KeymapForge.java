package com.github.einjerjar.mc.keymapforge;

import com.github.einjerjar.mc.keymap.Keymap;
import com.github.einjerjar.mc.keymap.client.gui.screen.ConfigScreenShared;
import com.github.einjerjar.mc.keymap.config.KeymapConfig;
import com.github.einjerjar.mc.keymap.cross.CrossKeybindShared;
import com.github.einjerjar.mc.keymap.cross.IntegrationRegistrarShared;
import com.github.einjerjar.mc.keymap.cross.TickEventRegistrarShared;
import com.github.einjerjar.mc.keymapforge.client.gui.screen.ConfigScreen;
import com.github.einjerjar.mc.keymapforge.cross.CrossKeybind;
import com.github.einjerjar.mc.keymapforge.cross.IntegrationRegistrar;
import com.github.einjerjar.mc.keymapforge.cross.TickEventRegistrar;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.ConfigGuiHandler;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.loading.FMLPaths;

import java.io.File;

@Mod(Keymap.MOD_ID)
public class KeymapForge {
    public KeymapForge() {
        DistExecutor.safeRunWhenOn(Dist.CLIENT, () -> IDK::clientInit);
        DistExecutor.safeRunWhenOn(Dist.DEDICATED_SERVER, () -> IDK::serverInit);
    }

    public static File configDirProvider() {
        return new File(FMLPaths.GAMEDIR.get().resolve("config/keymap.json").toUri());
    }

    // Prevents referent issue from fml
    public static class IDK {
        private IDK() {
        }

        private static void serverInit() {
            Keymap.logger().warn(Keymap.SERVER_WARN);
        }

        private static void clientInit() {
            KeymapConfig.configDirProvider(KeymapForge::configDirProvider);
            ConfigScreenShared.provider(ConfigScreen::getScreen);
            CrossKeybindShared.provider(CrossKeybind::execute);
            TickEventRegistrarShared.provider(TickEventRegistrar::execute);
            IntegrationRegistrarShared.provider(IntegrationRegistrar::execute);

            Keymap.init();

            ModLoadingContext.get().registerExtensionPoint(ConfigGuiHandler.ConfigGuiFactory.class,
                    () -> new ConfigGuiHandler.ConfigGuiFactory((minecraft, screen) -> ConfigScreenShared.provider().apply(
                            screen)));
        }
    }
}
