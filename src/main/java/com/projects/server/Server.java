package com.projects.server;


import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {
    private static final String ADDRESS = "127.0.0.1";
    private static final int PORT = 8080;

    private boolean isRunning;
    private final Database database;
    private ServerSocket serverSocket;

    public Server() {
        database = new Database();
        System.out.println("Server started!");
    }

    public void connectToLocalHost() {
        try (ExecutorService threadPool = Executors.newCachedThreadPool()) {
            serverSocket = new ServerSocket(PORT, 50, InetAddress.getByName(ADDRESS));
            isRunning = true;
            while (isRunning) {
                Socket socket = serverSocket.accept();
                threadPool.submit(() -> {
                    try (DataInputStream in = new DataInputStream(socket.getInputStream());
                         DataOutputStream out = new DataOutputStream(socket.getOutputStream())) {
                        String requestJson = in.readUTF();
                        System.out.println(Thread.currentThread().getName() + " : received: " + requestJson);

                        JsonObject reqObj = JsonParser.parseString(requestJson).getAsJsonObject();
                        Response response = executeCommand(reqObj);

                        String responseJson = new Gson().toJson(response);
                        System.out.println(Thread.currentThread().getName() + " : sent: " + responseJson);
                        out.writeUTF(responseJson);
                    } catch (IOException e) {
                        System.out.println("Error: " + e.getMessage());
                    }
                });
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }


    public Response exitCommand() {
        isRunning = false;
        try {
            serverSocket.close();
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }
        return Response.createSuccessResponse();
    }

    public Response executeCommand(JsonObject reqObj) {
        String type = reqObj.get("type").getAsString();
        try {
            switch (type) {
                case "set" -> {
                    JsonElement key = reqObj.get("key");
                    JsonElement value = reqObj.get("value");
                    return database.setCommand(key, value);
                }
                case "delete" -> {
                    JsonElement key = reqObj.get("key");
                    return database.deleteCommand(key);
                }
                case "get" -> {
                    JsonElement key = reqObj.get("key");
                    return database.getCommand(key);
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

