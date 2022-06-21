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
public class KeyRow {
    @Builder.Default protected List<KeyData> row = new ArrayList<>();
}