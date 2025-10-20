package com.projects.server;

import com.google.gson.JsonElement;

public class Response {
    private final String response;
    private final JsonElement value;
    private final String reason;

    Response(String response, JsonElement value, String reason) {
        this.response = response;
        this.value = value;
        this.reason = reason;
    }

    public static Response success() {
        return new Response("OK", null, null);
    }

    public static Response success(JsonElement value) {
        return new Response("OK", value, null);
    }

    public static Response error(String reason) {
        return new Response("ERROR", null, reason);
    }

    public static Response emptyResponse() {
        return new Response("ERROR", null, "No such operation");
    }
}
