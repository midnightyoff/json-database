package com.projects.server;

import javax.xml.crypto.Data;
import java.io.DataInputStream;
import java.io.DataOutputStream;

public class Main {
    public static void main(String[] args) {
        Database db = new Database();
        Server server = new Server(db);
        server.setRunning(true);
        server.connectToLocalHost();
    }
}
