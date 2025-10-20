package com.projects.server;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

public class Database {
    private final DatabaseLoader loader = new DatabaseLoader();;

    public Response setCommand(JsonElement key, JsonElement value) {
        List<String> path = toPath(key);
        if (path.isEmpty()) {
            return Response.error("No such key");
        }
        try {
            JsonObject root = loader.read();
            JsonObject parent = ensureParent(root, path);
            String last = path.get(path.size() - 1);
            parent.add(last, value);
            loader.write(root);
            return Response.success();
        } catch (Exception e) {
            return Response.error("Operation failed: " + e.getMessage());
        }
    }

    public Response getCommand(JsonElement key) {
        List<String> path = toPath(key);
        if (path.isEmpty()) {
            return Response.error("No such key");
        }

        try {
            JsonObject root = loader.read();
            JsonElement value = getByPath(root, path);
            if (value == null) {
                return Response.error("No such key");
            }
            return Response.success(value);
        } catch (Exception e) {
            return Response.error("Operation failed: " + e.getMessage());
        }
    }

    public Response deleteCommand(JsonElement key) {
        List<String> path = toPath(key);
        if (path.isEmpty()) {
            return Response.error("No such key");
        }

        try {
            JsonObject root = loader.read();
            JsonObject parent = getParentByPath(root, path);
            String last = path.get(path.size() - 1);

            if (parent == null || !parent.has(last)) {
                return Response.error("No such key");
            }

            parent.remove(last);
            loader.write(root);
            return Response.success();
        } catch (Exception e) {
            return Response.error("Operation failed: " + e.getMessage());
        }
    }

    private static List<String> toPath(JsonElement key) {
        List<String> path = new ArrayList<>();
        if (key == null || key.isJsonNull()) return path;
        if (key.isJsonArray()) {
            JsonArray arr = key.getAsJsonArray();
            for (JsonElement el : arr) path.add(el.getAsString());
        } else {
            path.add(key.getAsString());
        }
        return path;
    }

    private static JsonObject ensureParent(JsonObject root, List<String> path) {
        JsonObject current = root;
        for (int i = 0; i < path.size() - 1; i++) {
            String key = path.get(i);
            if (!current.has(key) || !current.get(key).isJsonObject()) {
                JsonObject created = new JsonObject();
                current.add(key, created);
                current = created;
            } else {
                current = current.getAsJsonObject(key);
            }
        }
        return current;
    }

    private static JsonElement getByPath(JsonObject root, List<String> path) {
        JsonElement current = root;
        for (String segment : path) {
            if (current == null || !current.isJsonObject()) {
                return null;
            }
            JsonObject obj = current.getAsJsonObject();
            if (!obj.has(segment)) {
                return null;
            }
            current = obj.get(segment);
        }
        return current;
    }

    private static JsonObject getParentByPath(JsonObject root, List<String> path) {
        JsonObject parent = root;
        for (int i = 0; i < path.size() - 1; i++) {
            String segment = path.get(i);
            if (parent == null || !parent.has(segment) || !parent.get(segment).isJsonObject()) {
                return null;
            }
            parent = parent.getAsJsonObject(segment);
        }
        return parent;
    }

}
