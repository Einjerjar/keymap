package com.github.einjerjar.mc.keymap.keys.layout;

import lombok.Getter;
import lombok.ToString;
import lombok.experimental.Accessors;

@ToString
@Accessors(fluent = true, chain = true)
public class KeyData {
    @Getter protected int code;
    @Getter protected String name;
    @Getter protected boolean enabled = true;
    @Getter protected boolean mouse = false;
    @Getter protected int width = 0;
    @Getter protected int height = 0;

    public void setCode(int code) {
        this.code = code;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public void setMouse(boolean mouse) {
        this.mouse = mouse;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public void setHeight(int height) {
        this.height = height;
    }
}
