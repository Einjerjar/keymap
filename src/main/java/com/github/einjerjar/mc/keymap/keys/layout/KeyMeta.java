package com.github.einjerjar.mc.keymap.keys.layout;

import lombok.*;
import lombok.experimental.Accessors;
import lombok.extern.jackson.Jacksonized;

@Getter
@Setter
@Builder
@ToString
@Jacksonized
@Accessors(fluent = true)
public class KeyMeta {
    @Builder.Default protected String author = null;
    protected String name;
    protected String code;
}
