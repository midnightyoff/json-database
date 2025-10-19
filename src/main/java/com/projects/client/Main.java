package com.projects.client;

import com.beust.jcommander.JCommander;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileReader;
import java.net.InetAddress;
import java.net.Socket;


public class Main {
    private static final String ADDRESS = "127.0.0.1";
    private static final int PORT = 8080;

    public static void main(String[] args) {

        Request request = new Request();
        JCommander.newBuilder()
                .addObject(request)
                .build()
                .parse(args);

        Object object = request;
        if (request.getFileName() != null) {
            String path = System.getProperty("user.dir") + "/src/main/java/com/projects/client/data/" + request.getFileName();
            try (BufferedReader br = new BufferedReader(new FileReader(path))) {
                object = new Gson().fromJson(br, Object.class);
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }

        String sentMsg = new Gson().toJson(object);
        String receivedMsg = "";

        try (Socket socket = new Socket(InetAddress.getByName(ADDRESS), PORT);
             DataInputStream input = new DataInputStream(socket.getInputStream());
             DataOutputStream output = new DataOutputStream(socket.getOutputStream())) {
            System.out.println("Client connected to " + socket.getRemoteSocketAddress());
            output.writeUTF(sentMsg);
            receivedMsg = input.readUTF();
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
        System.out.println("Sent: " + sentMsg + "\n" +
                "Received: " + receivedMsg);
    }
}