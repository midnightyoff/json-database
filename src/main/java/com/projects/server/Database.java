package com.projects.server;

import java.util.LinkedHashMap;
import java.util.Map;

public class Database {
    private final Map<String,String> data;

    Database(){
        data = new LinkedHashMap<>();
    }

    public Response setCommand(String key, String value) {
        Response response = new Response();
        data.put(key,value);
        response.setResponse("OK");
        return response;
    }

    public Response getCommand(String key) {
        Response response = new Response();
        if (data.containsKey(key)) {
            response.setValue(data.get(key));
            response.setResponse("OK");
        } else {
            response.setResponse("ERROR");
            response.setReason("No such key");
        }
        return response;
    }

    public Response deleteCommand(String key) {
        Response response = new Response();
        if (data.containsKey(key)) {
            data.remove(key);
            response.setResponse("OK");
        } else {
            response.setResponse("ERROR");
            response.setReason("No such key");
        }
        return response;
    }
}
