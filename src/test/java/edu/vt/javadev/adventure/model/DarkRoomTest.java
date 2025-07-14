package edu.vt.javadev.adventure.model;

import edu.vt.javadev.adventure.Message;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class DarkRoomTest {

    private DarkRoom cellar;



    // Persons:
    // note: player already declared and initialized in World.player

    // Items:
    private Light flashlight;

    @BeforeEach
    public void setup() {
        // Reset global state before each test
        World.reset();

        // Initialize the game
        new MyGame();


        // Retrieve references to relevant objects
        cellar = (DarkRoom) World.map.get("cellar");



        flashlight = (Light) World.map.get("flashlight");
    }

    @Test
    public void testTooDark() {
        flashlight.setOn(true);
        assertFalse(cellar.isTooDark());
        flashlight.setOn(false);
        assertTrue(cellar.isTooDark());
    }

    @Test
    public void testDescription() {
        flashlight.setOn(false);
        assertEquals(Message.roomTooDark(cellar), cellar.getDescription());
        flashlight.setOn(true);
        assertFalse(cellar.getDescription().contains(Message.roomTooDark(cellar)));

    }

    @Test
    public void testGetContents() {
        flashlight.setOn(false);
        assertTrue(cellar.getContentsDescription().isEmpty());
        flashlight.setOn(true);
        String contents = cellar.getContentsDescription();
        assertTrue(contents.contains("crate"));
        assertTrue(contents.contains("breaker-box"));
    }

}
