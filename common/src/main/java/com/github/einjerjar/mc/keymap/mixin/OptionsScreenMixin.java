package com.github.einjerjar.mc.keymap.mixin;

import com.github.einjerjar.mc.keymap.client.gui.screen.KeymapScreen;
import com.github.einjerjar.mc.keymap.client.gui.screen.LayoutSelectionScreen;
import com.github.einjerjar.mc.keymap.config.KeymapConfig;
import net.minecraft.client.Options;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.screens.OptionsScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.controls.ControlsScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(OptionsScreen.class)
public class OptionsScreenMixin extends Screen {
    @Shadow private Options options;

    protected OptionsScreenMixin(Component arg) {
        super(arg);
    }

    @Inject(at = @At("TAIL"), method = "init")
    private void onInit(CallbackInfo ci) {
        if (!KeymapConfig.instance().replaceKeybindScreen()) return;
        for (GuiEventListener child : children()) {
            if (child instanceof Button bb && bb.x == this.width / 2 + 5 && bb.y == this.height / 6 + 72 - 6) {
                this.removeWidget(bb);

                this.addRenderableWidget(new Button(this.width / 2 + 5,
                        this.height / 6 + 72 - 6,
                        150,
                        20,
                        new TranslatableComponent("keymap.keyCat"),
                        button -> {
                            if (KeymapConfig.instance().replaceKeybindScreen()) {
                                Screen scr;
                                if (KeymapConfig.instance().firstOpenDone()) {
                                    scr = new KeymapScreen(this);
                                } else {
                                    scr = new LayoutSelectionScreen(this);
                                }
                                this.minecraft.setScreen(scr);
                            } else {
                                this.minecraft.setScreen(new ControlsScreen(this, this.options));
                            }
                        }));
                return;
            }
        }
    }
}
