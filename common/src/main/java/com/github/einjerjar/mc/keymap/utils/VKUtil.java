package com.github.einjerjar.mc.keymap.utils;

import com.github.einjerjar.mc.keymap.client.gui.widgets.VirtualKeyboardWidget;
import com.github.einjerjar.mc.keymap.keys.layout.KeyLayout;
import com.github.einjerjar.mc.widgets.EWidget;

import java.util.List;

/**
 * General util to reduce code repetition when generating VK layouts
 */
public class VKUtil {
    private VKUtil() {
    }

    /**
     * Generate a dumb layout without any action handlers
     *
     * @param layout The KeyLayout to use
     * @param x      The x position of the widgets
     * @param y      The y position of the widgets
     *
     * @return A list of VK widgets (basic, extra, mouse, numpad)
     */
    public static List<VirtualKeyboardWidget> genLayout(KeyLayout layout, int x, int y) {
        return genLayout(layout, x, y, null, null);
    }

    /**
     * Generate a VK layout with support for action handlers
     *
     * @param layout         The KeyLayout to use
     * @param x              The x position of the widgets
     * @param y              The y position of the widgets
     * @param onClick        The action to take when a normal key is clicked
     * @param onSpecialClick The action to take when a special key is clicked
     *
     * @return  A list of VirtualKeyboardWidget instances
     */
    public static List<VirtualKeyboardWidget> genLayout(KeyLayout layout, int x, int y, EWidget.SimpleWidgetAction<VirtualKeyboardWidget> onClick, VirtualKeyboardWidget.SpecialVKKeyClicked onSpecialClick) {
        VirtualKeyboardWidget vkBasic = new VirtualKeyboardWidget(layout.keys().basic(), x, y, 0, 0);
        VirtualKeyboardWidget vkExtra = new VirtualKeyboardWidget(layout.keys().extra(),
                vkBasic.left(),
                vkBasic.bottom() + 4,
                0,
                0);
        VirtualKeyboardWidget vkMouse = new VirtualKeyboardWidget(layout.keys().mouse(),
                vkExtra.left(),
                vkExtra.bottom() + 2,
                0,
                0);
        VirtualKeyboardWidget vkNumpad = new VirtualKeyboardWidget(layout.keys().numpad(),
                vkMouse.right() + 4,
                vkBasic.bottom() + 4,
                0,
                0);

        List<VirtualKeyboardWidget> vks = List.of(vkBasic, vkExtra, vkMouse, vkNumpad);

        for (VirtualKeyboardWidget vk : vks) {
            vk.onKeyClicked(onClick);
            vk.onSpecialKeyClicked(onSpecialClick);
        }

        return vks;
    }
}
