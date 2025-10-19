package com.projects.server;

import com.google.gson.JsonElement;

public class Response {
    private String response;
    private JsonElement value;
    private String reason;

    public void setResponse(String response) {
        this.response = response;
    }

    public void setValue(JsonElement value) {
        this.value = value;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public static Response emptyResponse() {
        Response response = new Response();
        response.setResponse("ERROR");
        response.setReason("No such operation");
        return response;
    }

    public String getResponse() {
        return response;
    }

    public JsonElement getValue() {
        return value;
    }

    public String getReason() {
        return reason;
    }

    public static Response createSuccessResponse() {
        Response response = new Response();
        response.setResponse("OK");
        return response;
    }

    public static Response createSuccessResponse(JsonElement value) {
        Response response = createSuccessResponse();
        response.setValue(value);
        return response;
    }

    public static Response createErrorResponse(String reason) {
        Response response = new Response();
        response.setResponse("ERROR");
        response.setReason(reason);
        return response;
    }
}
