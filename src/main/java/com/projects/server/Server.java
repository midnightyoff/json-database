package com.projects.server;


import java.util.Arrays;

public class Server {
    private boolean isRunning = false;
    private final String[] database = new String[1000];

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

    public void exitCommand() {
        isRunning = false;
    }

    public boolean isRunning() {
        return isRunning;
    }

    public void setRunning(boolean running) {
        isRunning = running;
    }

    public void executeCommand(String userInput) {
        String[] command = userInput.split(" ", 3);
        try {
            switch (command[0]) {
                case "set" -> System.out.println(setCommand(Integer.parseInt(command[1]), command[2]));
                case "delete" -> System.out.println(deleteCommand(Integer.parseInt(command[1])));
                case "get" -> System.out.println(getCommand(Integer.parseInt(command[1])));
                case "exit" -> exitCommand();
                default -> System.out.println("Non-existent command: " + Arrays.toString(command));
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}

