package com.projects.server;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.*;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class DatabaseLoader {
    private final String path = System.getProperty("user.dir") + "/src/main/java/com/projects/server/data/data.json";
    private final ReadWriteLock lock;

    public DatabaseLoader() {
        lock = new ReentrantReadWriteLock();
    }

    public void write(Map<String, String> map) {
        lock.writeLock().lock();
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(path))) {
            new Gson().toJson(map, writer);
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }
        lock.writeLock().unlock();
    }

    public Map<String, String> read() {
        Map<String, String> map = null;
        lock.readLock().lock();
        Type type = new TypeToken<Map<String, String>>() {}.getType();
        try (BufferedReader reader = new BufferedReader(new FileReader(path))) {
            map = new Gson().fromJson(reader, type);
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }
        lock.readLock().unlock();
        return map == null ? new HashMap<>() : map;
    }
}
