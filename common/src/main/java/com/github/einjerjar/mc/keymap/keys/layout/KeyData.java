package com.github.einjerjar.mc.keymap.keys.layout;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

@Getter
@AllArgsConstructor
@ToString
@Accessors(fluent = true)
public class KeyData {
    protected         int     code;
    protected         String  name;
    protected         boolean enabled;
    @Setter protected boolean mouse;
    protected         int     width;
    protected         int     height;
}
