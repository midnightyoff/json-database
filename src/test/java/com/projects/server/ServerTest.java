package com.projects.server;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ServerTest {

    private Server server;
    private Database db;

    @BeforeEach
    void setUp() {
        db = new Database();
        server = new Server(db);
        server.setRunning(true);
        server.executeCommand("set 0 Text0");
        server.executeCommand("set 1 Text1");
        server.executeCommand("set 2 Text2");
    }

    @Test
    void whenServerExitServerStatusRunningFalse() {
        server.executeCommand("exit");
        assertFalse(server.isRunning());
    }

    @Test
    void whenDeleteTextStringIsEmpty() {
        server.executeCommand("delete 2");
        Response response = db.getCommand("2");
        assertNull(response.getValue());
    }

    @Test
    void setNewString() {
        server.executeCommand("set 3 Text3");
        Response response = db.getCommand("3");
        assertEquals("Text3", response.getValue());
    }

    @Test
    void getString() {
        Response response = db.getCommand("0");
        assertEquals("Text0", response.getValue());
    }

    @Test
    void whenSetNewStringPrintOK() {
        assertEquals("OK", db.setCommand("3", "Text3").getResponse());
    }

    @Test
    void whenSetNewStringToExistingStringPrintOK() {
        assertEquals("OK", db.setCommand("3", "Text33333").getResponse());
    }

    @Test
    void whenDeleteExistingStringPrintOK() {
        assertEquals("OK", db.deleteCommand("2").getResponse());
    }

}