package com.projects.server;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ServerTest {

    private Server server;

    @BeforeEach
    void setUp() {
        server = new Server();
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
        assertEquals("Error: Non-existent string", server.getCommand(2));
    }

    @Test
    void setNewString() {
        server.executeCommand("set 3 Text3");
        assertEquals("Text3", server.getCommand(3));
    }

    @Test
    void getString() {
        assertEquals("Text0", server.getCommand(0));
    }

    @Test
    void whenGetOnNoneExistingStringPrintError() {
        assertEquals("Error: Index 1001 out of bounds for length 1000", server.getCommand(1001));
    }

    @Test
    void whenSetNewStringPrintOK() {
        assertEquals("OK", server.setCommand(3, "Text3"));
    }

    @Test
    void whenSetNewStringToExistingStringPrintOK() {
        assertEquals("OK", server.setCommand(3, "Text33333"));
    }

    @Test
    void whenDeleteExistingStringPrintOK() {
        assertEquals("OK", server.deleteCommand(2));
    }

}