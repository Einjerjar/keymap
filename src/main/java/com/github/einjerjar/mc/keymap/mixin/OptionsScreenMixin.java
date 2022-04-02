package com.github.einjerjar.mc.keymap.mixin;

import com.github.einjerjar.mc.keymap.KeymapMain;
import com.github.einjerjar.mc.keymap.screen.KeyMappingScreen2;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.option.ControlsOptionsScreen;
import net.minecraft.client.gui.screen.option.OptionsScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(value = OptionsScreen.class)
public class OptionsScreenMixin {
    @Inject(at = @At("HEAD"), method = "method_19827", cancellable = true)
    private void openKeybindScreen(ButtonWidget button, CallbackInfo ci) {
        if (KeymapMain.cfg.replaceKeybindScreen) {
            // MinecraftClient.getInstance().setScreen(new TestingScreen((Screen) (Object) this));
            MinecraftClient.getInstance().setScreen(new KeymappingScreen2((OptionsScreen) (Object) this));
            ci.cancel();
        }
    }
}
