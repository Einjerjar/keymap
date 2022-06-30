package com.github.einjerjar.mc.keymap.cross;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.HashMap;
import java.util.Map;

@Accessors(fluent = true)
public class IntegrationRegistrarShared {
    @Getter protected static         Map<String, Boolean>         enabledIntegrations = new HashMap<>();
    @Getter @Setter protected static IntegrationRegistrarProvider provider;

    public static boolean check(String s) {
        return enabledIntegrations.containsKey(s) && enabledIntegrations().get(s);
    }

    @FunctionalInterface
    public interface IntegrationRegistrarProvider {
        void execute();
    }
}