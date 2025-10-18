package com.projects.server;


import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;

public class Server {
    private static final String ADDRESS = "127.0.0.1";
    private static final int PORT = 8080;

    private boolean isRunning = false;
    private final String[] database = new String[1000];

    private Socket socket;
    private DataInputStream in;
    private DataOutputStream out;


    public String setCommand(int index, String text) {
        try {
            database[index] = text;
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
        return "OK";
    }

    public String getCommand(int index) {
        try {
            if (database[index] != null) {
                return database[index];
            } else throw new Exception("Non-existent string");
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }

    public String deleteCommand(int index) {
        try {
            database[index] = null;
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
        return "OK";
    }

    public String exitCommand() {
        isRunning = false;
        try {
            socket.close();
            return "OK";
        } catch (Exception e) {
           return ("Error: " + e.getMessage());
        }
    }

    public boolean isRunning() {
        return isRunning;
    }

    public void setRunning(boolean running) {
        isRunning = running;
    }

    public String executeCommand(String userInput) {
        String[] command = userInput.split(" ", 3);
        try {
            switch (command[0]) {
                case "set" -> {
                    return setCommand(Integer.parseInt(command[1]), command[2]);
                }
                case "delete" -> {
                    return deleteCommand(Integer.parseInt(command[1]));
                }
                case "get" -> {
                    return getCommand(Integer.parseInt(command[1]));
                }
                case "exit" -> {
                    return exitCommand();
                }
                default -> {
                    return "Non-existent command: " + Arrays.toString(command);
                }
            }
        } catch (Exception e) {
            return ("Error: " + e.getMessage());
        }
    }

    public void connectToLocalHost() {
        try (ServerSocket server = new ServerSocket(PORT, 50, InetAddress.getByName(ADDRESS))) {
            socket = server.accept();
            in = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());
            System.out.println("Server connected to " + socket.getInetAddress().getHostAddress());
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public DataInputStream getDataInputStream() {
        return in;
    }
    public DataOutputStream getDataOutputStream() {
        return out;
    }
}

