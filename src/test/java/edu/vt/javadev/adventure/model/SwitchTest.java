package edu.vt.javadev.adventure.model;

import edu.vt.javadev.adventure.Message;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class SwitchTest {


    private Light flashlight;


    @BeforeEach
    public void setup() {
        // Reset global state before each test
        World.reset();

        // Initialize the game
        new MyGame();

        flashlight = (Light) World.map.get("flashlight");

    }

    @Test
    public void testState() {
        flashlight.setOn(false);
        assertEquals("The flashlight is off.", flashlight.getStateDescription());
        flashlight.setOn(true);
        assertEquals("The flashlight is on.", flashlight.getStateDescription());
    }

    @Test
    public void testOnAlready() {
        flashlight.setOn(true);
        flashlight.turnOn();
        assertEquals(Message.turnOnAlready(flashlight), World.currentMessage);
    }
}
