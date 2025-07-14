package edu.vt.javadev.adventure.model;

import edu.vt.javadev.adventure.Message;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class HauntedDoorTest {
    private HauntedDoor frontDoor;

    @BeforeEach
    public void setup() {
        World.reset();

        // Initialize the game
        new MyGame();

        frontDoor = (HauntedDoor) World.map.get("front-door");
    }

    @Test
    public void testOpen() {
        assertTrue(World.isHaunted());
        frontDoor.open();
        assertFalse(frontDoor.isOpen());
        assertEquals(Message.hauntedDoorMessage, World.currentMessage);
        assertTrue(frontDoor.getDescription().contains(Message.hauntedDoorMessage));
        World.setHaunted(false);
        frontDoor.open();
        assertTrue(frontDoor.isOpen());
        assertEquals(Message.openOkay(frontDoor), World.currentMessage);
        assertFalse(frontDoor.getDescription().contains(Message.hauntedDoorMessage));
    }

    @Test
    public void testNoDescription() {
        HauntedDoor door = new HauntedDoor("door");
        assertEquals(Message.hauntedDoorMessage, door.getDescription());
    }
}