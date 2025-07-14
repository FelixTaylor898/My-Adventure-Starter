package edu.vt.javadev.adventure.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class RoomTest {

    @BeforeEach
    public void setup() {
        // Reset global state before each test
        World.reset();

        // Initialize the game
        new MyGame();
    }

    @Test
    public void invalidRoomDirection() {
        Room room = new Room("room");
        try {
            room.setRoomDir(null, new Room("a"));
        } catch (IllegalArgumentException e) {
            assertTrue(true);
        }
        try {
            room.setRoomDir("", new Room("b"));
        } catch (IllegalArgumentException e) {
            assertTrue(true);
        }
        try {
            room.setRoomDir("east", null);
        } catch (IllegalArgumentException e) {
            assertTrue(true);
        }
    }

    @Test
    public void invalidDoorDirection() {
        Room room = new Room("room");
        try {
            room.setDoorDir(null, new Door("a"));
        } catch (IllegalArgumentException e) {
            assertTrue(true);
        }
        try {
            room.setDoorDir("", new Door ("b"));
        } catch (IllegalArgumentException e) {
            assertTrue(true);
        }
        try {
            room.setDoorDir("east", null);
        } catch (IllegalArgumentException e) {
            assertTrue(true);
        }
    }

    @Test
    public void testLook() {
        Room room = new Room("bed-room");
        room.setDescription("your room");
        room.look();
        assertEquals("Bed Room", World.roomName);
        assertTrue(World.roomDescription.contains("your room"));
    }
}
