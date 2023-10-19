package com.github.einjerjar.mc.keymapforge;

import com.github.einjerjar.mc.keymap.Keymap;
import com.github.einjerjar.mc.keymap.client.gui.screen.ConfigScreen;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.ConfigScreenHandler;
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

    public static File configDirProvider(String name) {
        return new File(FMLPaths.GAMEDIR.get().resolve("config/" + name).toUri());
    }

    // Prevents referent issue from fml
    public static class IDK {
        private IDK() {}

        private static void serverInit() {
            Keymap.logger().warn(Keymap.SERVER_WARN);
        }

        private static void clientInit() {
            Keymap.init();

            ModLoadingContext.get()
                    .registerExtensionPoint(
                            ConfigScreenHandler.ConfigScreenFactory.class,
                            () -> new ConfigScreenHandler.ConfigScreenFactory(
                                    (minecraft, parent) -> new ConfigScreen(parent)));
        }
    }
}
