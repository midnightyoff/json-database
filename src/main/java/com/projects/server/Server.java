package com.projects.server;


import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    private static final String ADDRESS = "127.0.0.1";
    private static final int PORT = 8080;

    private boolean isRunning;
    private final Database database;

    public Server(Database database) {
        this.database = database;
        this.isRunning = false;
    }

    public void connectToLocalHost() {
        try (ServerSocket server = new ServerSocket(PORT, 50, InetAddress.getByName(ADDRESS))) {
            while (isRunning) {
                try (Socket socket = server.accept();
                DataInputStream in = new DataInputStream(socket.getInputStream());
                DataOutputStream out = new DataOutputStream(socket.getOutputStream())) {
                    System.out.println("Server connected to " + socket.getInetAddress().getHostAddress());

                    String requestJson = in.readUTF();
                    JsonObject reqObj  = JsonParser.parseString(requestJson).getAsJsonObject();

                    String request = reqObj.get("type").getAsString()
                            + (reqObj.has("key") ? " " + reqObj.get("key").getAsString() : "")
                            + (reqObj.has("value") ? " " + reqObj.get("value").getAsString() : "");

                    Response response = executeCommand(request);
                    out.writeUTF(new Gson().toJson(response));
                }
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }


    public Response exitCommand() {
        isRunning = false;
        Response response = new Response();
        response.setResponse("OK");
        return response;
    }

    public Response executeCommand(String userInput) {
        String[] command = userInput.split(" ", 3);
        try {
            switch (command[0]) {
                case "set" -> {
                    return database.setCommand(command[1], command[2]);
                }
                case "delete" -> {
                    return database.deleteCommand(command[1]);
                }
                case "get" -> {
                    return database.getCommand(command[1]);
                }
                case "exit" -> {
                    return exitCommand();
                }
                default -> {
                    return Response.emptyResponse();
                }
            }
        } catch (Exception e) {
            return Response.emptyResponse();
        }
    }

    public void setRunning(boolean running) {
        isRunning = running;
    }

    public boolean isRunning() {
        return isRunning;
    }
}

