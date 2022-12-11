package com.github.einjerjar.mc.keymap.mixin;

import com.github.einjerjar.mc.keymap.keys.extrakeybind.KeymapRegistry;
import com.github.einjerjar.mc.keymap.keys.layout.KeyLayout;
import net.minecraft.client.gui.screens.TitleScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Best way I can think of to register the saved combos, without
 * having to open world to allow changing of the keybinds via settings
 */
@Mixin(TitleScreen.class)
public class TitleScreenMixin {

    /**
     * Hijack the init call and load the saved keymaps
     *
     * @param ci CallbackInfo
     */
    @Inject(at = @At("HEAD"), method = "init")
    private void onInit(CallbackInfo ci) {
        KeyLayout.loadKeys();
        KeymapRegistry.load();
    }
}
