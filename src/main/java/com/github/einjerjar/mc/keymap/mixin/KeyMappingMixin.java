package com.github.einjerjar.mc.keymap.mixin;

import com.github.einjerjar.mc.keymap.Keymap;
import com.github.einjerjar.mc.keymap.client.gui.screen.KeymapScreen;
import com.github.einjerjar.mc.keymap.keys.KeyType;
import com.github.einjerjar.mc.keymap.keys.extrakeybind.KeyComboData;
import com.github.einjerjar.mc.keymap.keys.extrakeybind.KeybindRegistry;
import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Objects;

@Mixin(KeyMapping.class)
public class KeyMappingMixin {
    @Shadow private InputConstants.Key key;

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
        KeyComboData kd = new KeyComboData(key.getValue(), KeyType.KEYBOARD, alt, shift, ctrl);
        Keymap.logger().error(KeybindRegistry.bindMap().containsKey(kd));
        Keymap.logger().warn("onKeyPressed: {}, Complex: {}, Data: {}", key.getValue(), !kd.onlyKey(), kd);

        if (KeybindRegistry.bindMap().containsKey(kd)) {
            KeyMappingAccessor kb = (KeyMappingAccessor) KeybindRegistry.bindMap().get(kd);
            Keymap.logger().error("Processing event {}, {}", KeybindRegistry.bindMap().get(kd).getName(), KeyMappingAccessor.getMap().containsValue(KeybindRegistry.bindMap().get(kd)));
            Keymap.logger().warn(kb.getClickCount());
            kb.setClickCount(kb.getClickCount() + 1);
            Keymap.logger().warn(kb.getClickCount());
            KeybindRegistry.bindMap().get(kd).setDown(true);
            ci.cancel();
        }

        if (KeyMappingAccessor.getMap().containsKey(key)) {
            Keymap.logger().error("Cancelling event");
            KeyMapping kb = KeyMappingAccessor.getMap().get(key);
            if (kb != null) {
                Keymap.logger().warn(((KeyMappingAccessor) kb).getClickCount());
            }
            ci.cancel();
        }
        ci.cancel();
    }
}
