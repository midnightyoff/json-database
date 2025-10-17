package com.projects.server;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Server server = new Server();
        server.setRunning(true);

        Scanner scanner = new Scanner(System.in);
        while(server.isRunning()){
            String command = scanner.nextLine();
            server.executeCommand(command);
        }
    }
}
