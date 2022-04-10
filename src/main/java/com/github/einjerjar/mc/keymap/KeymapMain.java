package com.github.einjerjar.mc.keymap;

import com.github.einjerjar.mc.keymap.screen.KeyMappingScreen;
import com.mojang.brigadier.tree.LiteralCommandNode;
import lombok.Getter;
import lombok.experimental.Accessors;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.ConfigHolder;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.TranslatableText;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

@Environment(EnvType.CLIENT)
@Accessors(fluent = true, chain = true)
public class KeymapMain implements ModInitializer {
    @Getter protected static final String MOD_ID = "keymap";
    @Getter protected static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
    @Getter protected static KeymapConfig cfg;
    @Getter protected static KeyLayoutConfig keys;
    @Getter protected static boolean malilibAvailable = false;

    protected static KeyBinding KBOpenKBScreen;
    protected static ConfigHolder<KeymapConfig> holderCfg;
    protected static ConfigHolder<KeyLayoutConfig> holderKeys;

    @Override
    public void onInitialize() {
        CLog.info("Keymap Init");

        AutoConfig.register(KeymapConfig.class, GsonConfigSerializer::new);
        AutoConfig.register(KeyLayoutConfig.class, GsonConfigSerializer::new);
        holderCfg = AutoConfig.getConfigHolder(KeymapConfig.class);
        holderKeys = AutoConfig.getConfigHolder(KeyLayoutConfig.class);
        cfg = holderCfg.getConfig();
        keys = holderKeys.getConfig();

        if (cfg.malilibSupport) {
            Optional<ModContainer> malilibContainer = FabricLoader.getInstance().getModContainer("malilib");
            if (malilibContainer.isPresent()) {
                ModContainer malilibMod = malilibContainer.get();
                CLog.info(String.format("Found malilib version %s, keymap support will be enabled",
                    malilibMod.getMetadata().getVersion().getFriendlyString()));
                malilibAvailable = true;
            }
        }

        KBOpenKBScreen = KeyBindingHelper.registerKeyBinding(new KeyBinding(
            "key.keymap.cfg.open",
            InputUtil.Type.KEYSYM,
            InputUtil.GLFW_KEY_GRAVE_ACCENT,
            "key.keymap.cfg.title"
        ));

        CommandRegistrationCallback.EVENT.register((dispatcher, dedicated) -> {
            LiteralCommandNode<ServerCommandSource> keymapCommand = CommandManager
                .literal("keymap")
                .build();
            keymapCommand.addChild(CommandManager
                .literal("reload_keys")
                .executes(context -> {
                    if (holderKeys.load()) {
                        keys = holderKeys.getConfig();
                    }
                    assert MinecraftClient.getInstance().player != null;
                    MinecraftClient.getInstance().player.sendMessage(
                        new TranslatableText("key.keymap.message.reload_keys"), false);
                    return 0;
                })
                .build());

            dispatcher.getRoot().addChild(keymapCommand);
        });

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (KBOpenKBScreen.wasPressed()) {
                client.setScreen(new KeyMappingScreen());
            }
        });
    }
}
