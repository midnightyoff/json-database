package com.projects.server;

import java.io.DataInputStream;
import java.io.DataOutputStream;

public class Main {
    public static void main(String[] args) {
        Server server = new Server();
        server.setRunning(true);

        try {
            while (true) {
                server.connectToLocalHost();
                DataOutputStream dataOutputStream = server.getDataOutputStream();
                DataInputStream dataInputStream = server.getDataInputStream();
                String sentMsg = server.executeCommand(dataInputStream.readUTF());
                dataOutputStream.writeUTF(sentMsg);
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
