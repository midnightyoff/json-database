package com.projects.client;

import com.beust.jcommander.JCommander;
import com.google.gson.Gson;

import java.io.DataInputStream;
import java.io.DataOutputStream;
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

        String sentMsg = new Gson().toJson(request);
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