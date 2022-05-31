package com.github.einjerjar.mc.keymap.client.gui.screen;

import com.github.einjerjar.mc.keymap.Keymap;
import com.github.einjerjar.mc.keymap.client.gui.widgets.KeymapListWidget;
import com.github.einjerjar.mc.keymap.client.gui.widgets.VirtualKeyboardWidget;
import com.github.einjerjar.mc.keymap.keys.layout.KeyLayout;
import com.github.einjerjar.mc.keymap.keys.registry.KeymapRegistry;
import com.github.einjerjar.mc.keymap.keys.registry.KeymapSource;
import com.github.einjerjar.mc.keymap.keys.wrappers.KeyHolder;
import com.github.einjerjar.mc.widgets.EScreen;
import com.github.einjerjar.mc.widgets.EWidget;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.TextComponent;

public class KeymapScreen extends EScreen {
    VirtualKeyboardWidget vkBasic;
    VirtualKeyboardWidget vkExtra;
    VirtualKeyboardWidget vkMouse;
    VirtualKeyboardWidget vkNumpad;

    KeymapListWidget listKm;

    public KeymapScreen(Screen parent) {
        super(parent, new TextComponent("Keymap"));
    }

    @Override protected void onInit() {
        KeyLayout layout = KeyLayout.getLayoutWithCode("en_us");

        vkBasic = new VirtualKeyboardWidget(layout.keys().basic(), 0, 0, 0, 0);
        vkExtra = new VirtualKeyboardWidget(layout.keys().extra(), vkBasic.left(), vkBasic.bottom() + 4, 0, 0);
        vkMouse = new VirtualKeyboardWidget(layout.keys().mouse(), vkExtra.left(), vkExtra.bottom() + 16 + 4, 0, 0);
        vkNumpad = new VirtualKeyboardWidget(layout.keys().numpad(), vkMouse.right() + 4, vkBasic.bottom() + 4, 0, 0);

        vkBasic.onKeyClicked(this::onVKKeyClicked);
        vkExtra.onKeyClicked(this::onVKKeyClicked);
        vkMouse.onKeyClicked(this::onVKKeyClicked);
        vkNumpad.onKeyClicked(this::onVKKeyClicked);

        for (EWidget w : vkBasic.childKeys()) {
            addRenderableWidget(w);
        }
        for (EWidget w : vkExtra.childKeys()) {
            addRenderableWidget(w);
        }
        for (EWidget w : vkMouse.childKeys()) {
            addRenderableWidget(w);
        }
        for (EWidget w : vkNumpad.childKeys()) {
            addRenderableWidget(w);
        }

        listKm = new KeymapListWidget(font.lineHeight, vkBasic.right() + 10, vkBasic.top(), width - vkBasic.right(), height);

        listKm.onItemSelected(source -> this.onKeyListClicked((KeymapListWidget) source));

        assert minecraft != null;
        for(KeymapSource source : KeymapRegistry.sources()) {
            if (!source.canUseSource()) continue;
            for(KeyHolder kh : source.getKeyHolders()) {
                listKm.addItem(new KeymapListWidget.KeymapListEntry(kh, listKm));
            }
        }

        addRenderableWidget(listKm);
    }

    private void onVKKeyClicked(VirtualKeyboardWidget source) {
        if (source.lastActionFrom() == null) return;
        Keymap.logger().warn("Code: [{}], Key: [{}], Mouse: [{}]",
                source.lastActionFrom().key().code(),
                source.lastActionFrom().key().name(),
                source.lastActionFrom().key().mouse());
    }

    private void onKeyListClicked(KeymapListWidget source) {
        if (source.itemSelected() == null) return;
        Keymap.logger().info(source.itemSelected().map().getTranslatableName());
    }

    @Override protected void renderScreen(PoseStack poseStack, int mouseX, int mouseY, float partialTick) {
        // empty just because
    }
}
