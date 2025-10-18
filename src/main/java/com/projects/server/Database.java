package com.projects.server;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class Database {
    private final DatabaseLoader loader;

    Database() {
        loader = new DatabaseLoader();
    }

    public synchronized Response setCommand(String key, String value) {
        Map<String, String> list = loader.read();
        Response response = new Response();
        list.put(key, value);
        response.setResponse("OK");
        loader.write(list);
        return response;
    }

    public synchronized Response getCommand(String key) {
        Map<String, String> list = loader.read();
        Response response = new Response();
        if (list.containsKey(key)) {
            response.setValue(list.get(key));
            response.setResponse("OK");
        } else {
            response.setResponse("ERROR");
            response.setReason("No such key");
        }
        loader.write(list);
        return response;
    }

    public synchronized Response deleteCommand(String key) {
        Map<String, String> list = loader.read();
        Response response = new Response();
        if (list.containsKey(key)) {
            list.remove(key);
            response.setResponse("OK");
        } else {
            response.setResponse("ERROR");
            response.setReason("No such key");
        }
        loader.write(list);
        return response;
    }
}
