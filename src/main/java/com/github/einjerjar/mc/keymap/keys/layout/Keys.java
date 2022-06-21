package com.github.einjerjar.mc.keymap.keys.layout;

import lombok.*;
import lombok.experimental.Accessors;
import lombok.extern.jackson.Jacksonized;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@ToString
@Jacksonized
@Accessors(fluent = true)
public class Keys {
    @Builder.Default protected List<KeyRow> basic = new ArrayList<>();
    @Builder.Default protected List<KeyRow> mouse = new ArrayList<>();
    @Builder.Default protected List<KeyRow> extra = new ArrayList<>();
    @Builder.Default protected List<KeyRow> numpad = new ArrayList<>();
}