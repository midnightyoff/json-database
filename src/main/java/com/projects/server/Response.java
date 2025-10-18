package com.projects.server;

public class Response {
    private String response;
    private String value;
    private String reason;

    public void setResponse(String response) {
        this.response = response;
    }

    public void setValue(String value) {
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

    public String getValue() {
        return value;
    }

    public String getReason() {
        return reason;
    }
}
