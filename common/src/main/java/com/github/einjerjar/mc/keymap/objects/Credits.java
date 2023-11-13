package com.github.einjerjar.mc.keymap.objects;

import com.github.einjerjar.mc.keymap.Keymap;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.*;
import lombok.experimental.Accessors;

import java.io.InputStreamReader;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystems;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Accessors(fluent = true)
public class Credits {
    private static final String CREDITS_ROOT = "assets/keymap/credits.json";
    static Credits instance;

    List<LanguageCredits> language;
    List<LayoutCredits> layout;
    List<CoreCredits> core;

    @Getter
    @Setter
    @ToString
    @NoArgsConstructor
    @AllArgsConstructor
    @Accessors(fluent = true)
    public static class LanguageCredits {
        String lang;
        List<String> name;
    }

    @Getter
    @Setter
    @ToString
    @NoArgsConstructor
    @AllArgsConstructor
    @Accessors(fluent = true)
    public static class LayoutCredits {
        String key;
        List<String> name;
    }

    @Getter
    @Setter
    @ToString
    @NoArgsConstructor
    @AllArgsConstructor
    @Accessors(fluent = true)
    public static class CoreCredits {
        String name;
        List<String> contributions;
    }

    public static synchronized Credits instance() {
        if (instance == null) {
            loadCredits();
        }
        return instance;
    }

    public static void loadCredits() {
        GsonBuilder builder = new GsonBuilder().setPrettyPrinting();
        Gson gson = builder.create();
        ClassLoader loader = Credits.class.getClassLoader();

        try {
            URI creditUri =
                    Objects.requireNonNull(loader.getResource(CREDITS_ROOT)).toURI();

            if (creditUri.getScheme().equals("jar")) {
                // simple notice, since sonarlint/intellij is complaining about readability
                // ----------------- another try-catch block starts here -----------------
                try {
                    FileSystems.getFileSystem(creditUri);
                } catch (Exception e) {
                    FileSystems.newFileSystem(creditUri, Collections.emptyMap());
                }
                // ----------------- another try-catch block ends here -----------------
            }

            try (InputStreamReader reader = new InputStreamReader(
                    Objects.requireNonNull(loader.getResourceAsStream(CREDITS_ROOT)), StandardCharsets.UTF_8)) {
                instance = gson.fromJson(reader, Credits.class);
            }

        } catch (Exception e) {
            Keymap.logger().warn("CANT LOAD CREDITS");
            e.printStackTrace();
        }
    }
}
