package com.projects.server;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class DatabaseLoader {
    private final Path path = Paths.get(System.getProperty("user.dir"),
            "src", "main", "java", "com", "projects", "server", "data", "data.json");
    private final ReadWriteLock lock;
    private final Gson gson;

    public DatabaseLoader() {
        lock = new ReentrantReadWriteLock();
        gson = new Gson();
        try {
            Files.createDirectories(path.getParent());
            if (!Files.exists(path)) {
                write(new JsonObject());
            }
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
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
