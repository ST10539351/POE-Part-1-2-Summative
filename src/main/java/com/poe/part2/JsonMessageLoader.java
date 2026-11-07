package com.poe.part2;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * Reads the stored-messages JSON file (the one Message.storeMessage() writes) into a list,
 * and rewrites it after a stored message is deleted.
 * Reference: Google (2024) 'Gson User Guide', GitHub.
 * Available at: https://github.com/google/gson/blob/main/UserGuide.md
 */
public class JsonMessageLoader {

    private static final Type LIST_TYPE = new TypeToken<List<Message>>() { }.getType();

    public static List<Message> load(String filePath) throws IOException {
        try (Reader reader = Files.newBufferedReader(Path.of(filePath), StandardCharsets.UTF_8)) {
            List<Message> messages = new Gson().fromJson(reader, LIST_TYPE);
            return messages == null ? new ArrayList<>() : messages;
        }
    }

    public static void save(String filePath, List<Message> messages) throws IOException {
        Gson gson = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
        try (Writer writer = Files.newBufferedWriter(Path.of(filePath), StandardCharsets.UTF_8)) {
            gson.toJson(messages, LIST_TYPE, writer);
        }
    }
}
