package com.projects.server;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

public class Database {
    private final DatabaseLoader loader;

    Database() {
        loader = new DatabaseLoader();
    }

    public synchronized Response setCommand(JsonElement key, JsonElement value) {
        List<String> path = toPath(key);
        if (path.isEmpty()) {
            return Response.createErrorResponse("No such key");
        }

        try {
            JsonObject root = loader.read();
            JsonObject parent = ensureParent(root, path);
            String last = path.get(path.size() - 1);
            parent.add(last, value);
            loader.write(root);
            return Response.createSuccessResponse();
        } catch (Exception e) {
            return Response.createErrorResponse("Operation failed: " + e.getMessage());
        }
    }

    public synchronized Response getCommand(JsonElement key) {
        List<String> path = toPath(key);
        if (path.isEmpty()) {
            return Response.createErrorResponse("No such key");
        }

        try {
            JsonObject root = loader.read();
            JsonElement value = getByPath(root, path);
            if (value == null) {
                return Response.createErrorResponse("No such key");
            }
            return Response.createSuccessResponse(value);
        } catch (Exception e) {
            return Response.createErrorResponse("Operation failed: " + e.getMessage());
        }
    }

    public synchronized Response deleteCommand(JsonElement key) {
        List<String> path = toPath(key);
        if (path.isEmpty()) {
            return Response.createErrorResponse("No such key");
        }

        try {
            JsonObject root = loader.read();
            JsonObject parent = getParentByPath(root, path);
            String last = path.get(path.size() - 1);

            if (parent == null || !parent.has(last)) {
                return Response.createErrorResponse("No such key");
            }

            parent.remove(last);
            loader.write(root);
            return Response.createSuccessResponse();
        } catch (Exception e) {
            return Response.createErrorResponse("Operation failed: " + e.getMessage());
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
        JsonObject cur = root;
        for (int i = 0; i < path.size() - 1; i++) {
            String k = path.get(i);
            if (!cur.has(k) || !cur.get(k).isJsonObject()) {
                JsonObject created = new JsonObject();
                cur.add(k, created);
                cur = created;
            } else {
                cur = cur.getAsJsonObject(k);
            }
        }
        return cur;
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
