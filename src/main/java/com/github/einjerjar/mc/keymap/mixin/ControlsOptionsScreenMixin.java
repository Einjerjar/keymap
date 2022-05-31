package com.github.einjerjar.mc.keymap.mixin;

import com.github.einjerjar.mc.keymap.client.gui.screen.KeymapScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.controls.ControlsScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ControlsScreen.class)
public class ControlsOptionsScreenMixin {
    @Inject(at = @At("HEAD"), method = "method_19872", cancellable = true)
    public void openKeybindScreen(Button button, CallbackInfo ci) {
        Minecraft.getInstance().setScreen(new KeymapScreen((Screen) (Object) this));
        ci.cancel();
    }
}
