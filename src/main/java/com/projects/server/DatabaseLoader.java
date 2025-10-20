package com.projects.server;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class DatabaseLoader {
    private final Path path = Paths.get(System.getProperty("user.dir"),
            "src", "main", "java", "com", "projects", "server", "data", "data.json");
    private final ReadWriteLock lock = new ReentrantReadWriteLock();
    private final Gson gson = new Gson();

    public DatabaseLoader() {
//        try {
//            Files.createDirectories(path.getParent());
//            if (!Files.exists(path)) {
//                write(new JsonObject());
//            }
//        } catch (IOException e) {
//            System.out.println("Error: " + e.getMessage());
//        }
        initializeDatabaseFile();
    }

    private void initializeDatabaseFile() {
        try {
            if (Files.notExists(path)) {
                Files.createDirectories(path.getParent());
                Files.writeString(path, "{}", StandardCharsets.UTF_8, StandardOpenOption.CREATE);
            } else {
                try (BufferedReader r = Files.newBufferedReader(path, StandardCharsets.UTF_8)) {
                    JsonElement parsed = JsonParser.parseReader(r);
                    if (!parsed.isJsonObject()) {
                        Files.writeString(path, "{}", StandardCharsets.UTF_8, StandardOpenOption.TRUNCATE_EXISTING);
                    }
                }
            }
        } catch (IOException e) {
            throw new UncheckedIOException("Cannot initialize DB file: " + path, e);
        }
    }

    public void write(JsonObject jsonObject) {
        lock.writeLock().lock();
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(path.toFile()))) {
            gson.toJson(jsonObject, writer);
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        } finally {
            lock.writeLock().unlock();
        }
    }

    public JsonObject read() {
        lock.readLock().lock();
        try (BufferedReader reader = new BufferedReader(new FileReader(path.toFile()))) {
            return gson.fromJson(reader, JsonObject.class);
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
            return new JsonObject();
        } finally {
            lock.readLock().unlock();
        }
    }
}
