package com.github.einjerjar.mc.keymapforge.mixin;

import com.github.einjerjar.mc.keymap.client.gui.screen.KeymapScreen;
import com.github.einjerjar.mc.keymap.client.gui.screen.LayoutSelectionScreen;
import com.github.einjerjar.mc.keymap.config.KeymapConfig;
import net.minecraft.client.Options;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.OptionsSubScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.controls.ControlsScreen;
import net.minecraft.client.gui.screens.controls.KeyBindsScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ControlsScreen.class)
public class ControlsOptionsScreenMixin extends OptionsSubScreen {
    public ControlsOptionsScreenMixin(Screen arg, Options arg2, Component arg3) {
        super(arg, arg2, arg3);
    }

    // @Inject(at = @At("HEAD"), method = "method_19872", cancellable = true)
    // public void openKeybindScreen(Button button, CallbackInfo ci) {
    //     Screen scr;
    //     if (KeymapConfig.instance().firstOpenDone()) {
    //         scr = new KeymapScreen((Screen) (Object) this);
    //     } else {
    //         scr = new LayoutSelectionScreen((Screen) (Object) this);
    //     }
    //     Minecraft.getInstance().setScreen(scr);
    //     ci.cancel();
    // }

    // Forge mixins are kinda fucky wucky
    @Inject(at = @At("TAIL"), method = "init")
    private void onInit(CallbackInfo ci) {
        int i = this.width / 2 - 155;
        int j = i + 160;
        int k = this.height / 6 - 12;

        if (!children().isEmpty() && children().get(1) instanceof Button bb) {
            this.removeWidget(bb);

            this.addRenderableWidget(new Button(j,
                    k,
                    150,
                    20,
                    new TranslatableComponent("controls.keybinds"),
                    (button) -> {
                        if (KeymapConfig.instance().replaceKeybindScreen()) {
                            Screen scr;
                            if (KeymapConfig.instance().firstOpenDone()) {
                                scr = new KeymapScreen(this);
                            } else {
                                scr = new LayoutSelectionScreen(this);
                            }
                            this.minecraft.setScreen(scr);
                        } else {
                            this.minecraft.setScreen(new KeyBindsScreen(this, this.options));
                        }
                    }));
        }
    }
}
