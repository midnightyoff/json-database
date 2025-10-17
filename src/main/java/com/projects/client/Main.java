package com.projects.client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.InetAddress;
import java.net.Socket;


public class Main {
    private static final String ADDRESS = "127.0.0.1";
    private static final int PORT = 8080;

    public static void main(String[] args) {
        String sentStr = "Give me a record # 12";
        String msg = "";
        try (Socket socket = new Socket(InetAddress.getByName(ADDRESS), PORT);
             DataInputStream input = new DataInputStream(socket.getInputStream());
             DataOutputStream output = new DataOutputStream(socket.getOutputStream())) {
            System.out.println("Client connected to " + socket.getRemoteSocketAddress());
            output.writeUTF(sentStr);
            msg = input.readUTF();
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
        System.out.println("Sent: " + sentStr + "\n" +
                "Received: " + msg);
    }
}