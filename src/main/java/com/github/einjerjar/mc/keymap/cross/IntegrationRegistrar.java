package com.github.einjerjar.mc.keymap.cross;

import com.github.einjerjar.mc.keymap.config.KeymapConfig;
import com.github.einjerjar.mc.keymap.cross.compat.KeymapInvMoveCompat;
import lombok.Getter;
import lombok.experimental.Accessors;
import me.pieking1215.invmove.InvMove;
import net.fabricmc.loader.api.FabricLoader;

import java.util.HashMap;
import java.util.Map;

@Accessors(fluent = true)
public class IntegrationRegistrar {
    @Getter protected static Map<String, Boolean> enabledIntegrations = new HashMap<>();

    public static void execute() {
        KeymapConfig k = KeymapConfig.instance();
        if (FabricLoader.getInstance().isModLoaded("invmove")) {
            InvMove.instance().modules.add(new KeymapInvMoveCompat());
        }
    }

    public static boolean check(String s) {
        return enabledIntegrations.containsKey(s) && enabledIntegrations().get(s);
    }
}