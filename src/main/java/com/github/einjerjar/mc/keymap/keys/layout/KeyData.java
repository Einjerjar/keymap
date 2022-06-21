package com.github.einjerjar.mc.keymap.keys.layout;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import lombok.extern.jackson.Jacksonized;

@Getter
@Setter
@Builder
@ToString
@Jacksonized
@Accessors(fluent = true)
public class KeyData {
    protected int code;
    protected String name;
    @Builder.Default protected boolean enabled = true;
    @Builder.Default protected boolean mouse = false;
    @Builder.Default protected int width = 0;
    @Builder.Default protected int height = 0;
}
