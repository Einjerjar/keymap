package com.github.einjerjar.mc.keymap.mixin;

import com.github.einjerjar.mc.keymap.Keymap;
import com.github.einjerjar.mc.keymap.keys.extrakeybind.KeyComboData;
import com.github.einjerjar.mc.keymap.keys.extrakeybind.KeybindRegistry;
import com.mojang.blaze3d.platform.InputConstants;
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

import java.util.Map;

@Mixin(KeyboardHandler.class)
public class KeyboardHandlerMixin {
    @Shadow @Final private Minecraft minecraft;
    private static KeyComboData lastValidCombo = null;

    @Inject(at=@At("HEAD"), method = "keyPress", cancellable = true)
    private void onKeyPress(long windowPointer, int key, int scanCode, int action, int modifiers, CallbackInfo ci) {
        if (windowPointer != minecraft.getWindow().getWindow()) return;
        Screen screen = minecraft.screen;

        if (screen == null || screen.passEvents) {
            KeyComboData kd = new KeyComboData(key, Screen.hasAltDown(), Screen.hasShiftDown(), Screen.hasControlDown());
            if (lastValidCombo != null && action == 0) {
                Keymap.logger().warn("LV: {}, CV: {}", lastValidCombo.keyCode(), kd.keyCode());
                if (lastValidCombo.keyCode() == kd.keyCode()) {
                    if (KeybindRegistry.bindMap().containsKey(lastValidCombo)) {
                        KeyMapping k = KeybindRegistry.bindMap().get(lastValidCombo);
                        k.setDown(false);
                        Keymap.logger().error("UNPRESS: {} {}", k.getName(), action);

                        lastValidCombo = null;
                        ci.cancel();
                        return;
                    }
                }
            }
            if (kd.onlyKey()) return;
            if (KeybindRegistry.bindMap().containsKey(kd)) {
                KeyMapping k = KeybindRegistry.bindMap().get(kd);
                k.setDown(action == 1 || action == 2);
                k.clickCount ++;
                lastValidCombo = kd;
                Keymap.logger().error("PRESS: {} {}", k.getName(), action);
                ci.cancel();
            }
        }
    }
}
