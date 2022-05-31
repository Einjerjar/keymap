package com.github.einjerjar.mc.keymap.keys.layout;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;

@ToString
@Accessors(fluent = true, chain = true)
@NoArgsConstructor
public class KeyMeta {
    @Getter protected String author = null;
    @Getter protected String name;
    @Getter protected String code;

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
