package edu.vt.javadev.adventure.model;

import edu.vt.javadev.adventure.Message;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class LeverTest {


    // Items:
    private Lever lever;

    @BeforeEach
    public void setup() {
        // Reset global state before each test
        World.reset();
        // Initialize the game
        new MyGame();
        lever = (Lever) World.map.get("lever");
    }

    @Test
    public void testTurnOff() {
        lever.setOn(false);
        lever.turnOff();
        assertEquals(Message.turnOffAlready(lever), World.currentMessage);
        lever.setOn(true);
        lever.turnOff();
        assertEquals(Message.turnOffSuccess(lever) + " " + Message.noPower, World.currentMessage);
    }

    @Test
    public void testExamineLever() {
        lever.examine();
        assertEquals("The lever is off. " + Message.noPower, World.currentMessage);
        lever.setOn(true);
        lever.examine();
        assertEquals("The lever is on. " + Message.hasPower, World.currentMessage);

    }

}
