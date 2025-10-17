package com.projects.server;

import java.io.DataInputStream;
import java.io.DataOutputStream;

public class Main {
    public static void main(String[] args) {
        Server server = new Server();
        server.setRunning(true);
        server.connectToLocalHost();
        String msg = "A record # 12 was sent";
        try {
            while (server.isRunning()) {
                DataOutputStream dataOutputStream = server.getDataOutputStream();
                DataInputStream dataInputStream = server.getDataInputStream();
                String userResponse = dataInputStream.readUTF();
                System.out.println("Received: " + userResponse);
                dataOutputStream.writeUTF(msg);
                System.out.println("Sent: " + msg);
                server.exitCommand();
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }

    }
}
