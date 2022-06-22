package com.github.einjerjar.mc.keymap.cross;

import com.github.einjerjar.mc.keymap.Keymap;
import com.github.einjerjar.mc.keymap.config.KeymapConfig;
import lombok.Getter;
import lombok.experimental.Accessors;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Accessors(fluent = true)
public class IntegrationRegistrar {
    @Getter protected static Map<String, Boolean> enabledIntegrations = new HashMap<>();

    public static void execute() {
        KeymapConfig k = KeymapConfig.instance();
        if (k.malilibSupport()) {
            Optional<ModContainer> malilibContainer = FabricLoader
                    .getInstance()
                    .getModContainer("malilib");
            if (malilibContainer.isPresent()) {
                ModContainer mod = malilibContainer.get();
                Keymap.logger().info("Keymap found malilib @ {}", mod.getMetadata().getVersion().getFriendlyString());
                enabledIntegrations.put("malilib", true);
            }
        }
    }

    public static boolean check(String s) {
        return enabledIntegrations.containsKey(s) && enabledIntegrations().get(s);
    }
}