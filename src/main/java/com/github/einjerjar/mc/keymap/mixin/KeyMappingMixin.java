package com.github.einjerjar.mc.keymap.mixin;

import com.github.einjerjar.mc.keymap.keys.extrakeybind.KeybindRegistry;
import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Objects;

@Mixin(KeyMapping.class)
public class KeyMappingMixin {
    private KeyMappingMixin() {
    }

    private static boolean isPressed(long handle, int key) {
        return InputConstants.isKeyDown(handle, key);
    }

    private static boolean isPressed(long handle, int key1, int key2) {
        return isPressed(handle, key1) || isPressed(handle, key2);
    }

    @Inject(at = @At("HEAD"), method = "click", cancellable = true)
    private static void onKeyPressed(InputConstants.Key key, CallbackInfo ci) {
        long winHandle = Minecraft.getInstance().getWindow().getWindow();
        boolean shift = isPressed(winHandle, InputConstants.KEY_LSHIFT, InputConstants.KEY_RSHIFT);
        boolean alt = isPressed(winHandle, InputConstants.KEY_LALT, InputConstants.KEY_RALT);
        boolean ctrl = isPressed(winHandle, InputConstants.KEY_LCONTROL, InputConstants.KEY_RCONTROL);
        int keyHash = Objects.hash(key.getValue(), ctrl, alt, shift);

        if (KeybindRegistry.bindMap().containsKey(keyHash)) {
            KeyMappingAccessor kb = (KeyMappingAccessor) KeybindRegistry.bindMap().get(keyHash);
            kb.setClickCount(kb.getClickCount() + 1);
            ci.cancel();
        }
    }
}
