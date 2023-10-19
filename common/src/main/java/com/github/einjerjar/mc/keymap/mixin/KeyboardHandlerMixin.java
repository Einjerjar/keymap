package com.github.einjerjar.mc.keymap.mixin;

import com.github.einjerjar.mc.keymap.keys.extrakeybind.KeyComboData;
import com.github.einjerjar.mc.keymap.keys.extrakeybind.KeymapRegistry;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.KeyboardHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Allows the mod to hijack keyboard input
 */
@Mixin(KeyboardHandler.class)
public class KeyboardHandlerMixin {
    private KeyComboData lastValidCombo = null;

    @Shadow
    @Final
    private Minecraft minecraft;

    /**
     * Listens for keypress and checks if there is any matching keybind from the registry,
     * if so, call that keybind, the cancel the execution of the rest of the function
     *
     * @param windowPointer WindowPointer
     * @param key           Key
     * @param scanCode      ScanCode
     * @param action        Action
     * @param modifiers     Modifiers
     * @param ci            CallbackInfo
     */
    @Inject(at = @At("HEAD"), method = "keyPress", cancellable = true)
    private void onKeyPress(long windowPointer, int key, int scanCode, int action, int modifiers, CallbackInfo ci) {
        if (windowPointer != minecraft.getWindow().getWindow()) return;
        Screen screen = minecraft.screen;

        if (screen == null) {
            KeyComboData kd =
                    new KeyComboData(key, Screen.hasAltDown(), Screen.hasShiftDown(), Screen.hasControlDown());
            if (lastValidCombo != null
                    && action == 0
                    && lastValidCombo.keyCode() == kd.keyCode()
                    && KeymapRegistry.bindMap().inverse().containsKey(lastValidCombo)) {
                KeyMapping k = KeymapRegistry.bindMap().inverse().get(lastValidCombo);
                k.setDown(false);

                lastValidCombo = null;
                ci.cancel();
                return;
            }
            if (kd.onlyKey()) return;
            if (KeymapRegistry.bindMap().inverse().containsKey(kd)) {
                KeyMapping k = KeymapRegistry.bindMap().inverse().get(kd);
                k.setDown(action == 1 || action == 2);
                k.clickCount++;
                lastValidCombo = kd;
                ci.cancel();
            }
        }
    }
}
