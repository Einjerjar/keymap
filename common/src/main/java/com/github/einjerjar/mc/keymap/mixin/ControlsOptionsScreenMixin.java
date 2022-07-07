package com.github.einjerjar.mc.keymap.mixin;

import com.github.einjerjar.mc.keymap.client.gui.screen.KeymapScreen;
import com.github.einjerjar.mc.keymap.client.gui.screen.LayoutSelectionScreen;
import com.github.einjerjar.mc.keymap.config.KeymapConfig;
import com.github.einjerjar.mc.widgets.utils.Text;
import net.minecraft.client.Options;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.screens.OptionsSubScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.controls.ControlsScreen;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Allows the mod to override the default keybinds screen
 */
@Mixin(ControlsScreen.class)
public class ControlsOptionsScreenMixin extends OptionsSubScreen {
    public ControlsOptionsScreenMixin(Screen arg, Options arg2, Component arg3) {
        super(arg, arg2, arg3);
    }

    /**
     * Hijacks the default keybind screen, by locating the button with the exact location
     * as the default keybinds button, hacky and not so efficient, but better than hijacking
     * the lambda, since I can't get that to work properly for forge
     *
     * @param ci CallbackInfo
     */
    @Inject(at = @At("TAIL"), method = "init")
    private void onInit(CallbackInfo ci) {
        if (!KeymapConfig.instance().replaceKeybindScreen()) return;
        int i = this.width / 2 - 155;
        int j = i + 160;
        int k = this.height / 6 - 12;

        for (GuiEventListener child : children()) {
            if (child instanceof Button bb && bb.x == j && bb.y == k) {
                this.removeWidget(bb);
                this.addRenderableWidget(new Button(j,
                        k,
                        150,
                        20,
                        Text.translatable("keymap.keyCat"), this::clickHandler));
                break;
            }
        }
    }

    /**
     * The handler given to the button to access the keymapping screen
     *
     * @param button The click source
     */
    protected void clickHandler(Button button) {
        assert minecraft != null;
        Screen scr;
        if (KeymapConfig.instance().firstOpenDone()) scr = new KeymapScreen(this);
        else scr = new LayoutSelectionScreen(this);

        minecraft.setScreen(scr);
    }
}