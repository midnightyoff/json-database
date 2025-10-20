package com.projects.server;


import com.google.gson.Gson;
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
    private final Gson gson = new Gson();

    private boolean isRunning;
    private final Database database;
    private ServerSocket serverSocket;

    public Server() {
        database = new Database();
        System.out.println("Server started!");
    }

    public void connectToLocalHost() {
        try (ExecutorService threadPool = Executors.newCachedThreadPool();
             ServerSocket sv = new ServerSocket(PORT, 50, InetAddress.getByName(ADDRESS))) {
            serverSocket = sv;
            isRunning = true;
            while (isRunning) {
                Socket socket = serverSocket.accept();
                threadPool.submit(() -> handleClient(socket));
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void handleClient(Socket socket) {
        try (DataInputStream in = new DataInputStream(socket.getInputStream());
             DataOutputStream out = new DataOutputStream(socket.getOutputStream())) {
            String requestJson = in.readUTF();
            System.out.println(Thread.currentThread().getName() + " : received: " + requestJson);

            Response response = processRequest(requestJson);

            String responseJson = gson.toJson(response);
            System.out.println(Thread.currentThread().getName() + " : sent: " + responseJson);
            out.writeUTF(responseJson);
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private Response stopServer() {
        isRunning = false;
        try {
            if (serverSocket != null && !serverSocket.isClosed()) {
                serverSocket.close();
            }
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }
        return Response.success();
    }

    private Response processRequest(String requestJson) {
        try {
            JsonObject reqObj = JsonParser.parseString(requestJson).getAsJsonObject();
            String type = reqObj.get("type").getAsString();

            return switch (type) {
                case "set" -> database.setCommand(reqObj.get("key"), reqObj.get("value"));
                case "get" -> database.getCommand(reqObj.get("key"));
                case "delete" -> database.deleteCommand(reqObj.get("key"));
                case "exit" -> stopServer();
                default -> Response.emptyResponse();
            };
        } catch (Exception e) {
            return Response.emptyResponse();
        }
    }
}

