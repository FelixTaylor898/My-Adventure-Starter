package edu.vt.javadev.adventure.model;

import edu.vt.javadev.adventure.Message;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class PersonTest {
    private Person player;
    private Light flashlight;

    @BeforeEach
    public void setup() {
        // Reset global state before each test
        World.reset();

        // Initialize the game
        new MyGame();
        flashlight = (Light) World.map.get("flashlight");


        player = World.player;

    }

    @Test
    public void testInventory() {
        player.search();
        assertTrue(World.currentMessage.contains("a flashlight"));
        player.removeChild(flashlight);
        player.search();
        assertEquals(Message.inventoryEmpty, World.currentMessage);

    }

    @Test
    public void testExamine() {
        player.examine();
        assertEquals(player.getDescription() + " " + player.getContentsDescription(), World.currentMessage);
        player.setAlias("Bob");
        player.examine();
        assertEquals(Message.yourName(player.getAlias()) + " " + player.getDescription() + " " + player.getContentsDescription(), World.currentMessage);
        Person p = new Person("person");
        p.addChild(flashlight);
        p.examine();
        assertEquals(p.getContentsDescription(), World.currentMessage);
    }

    @Test
    public void testExamineMisc() {
        Person p = new Person("p");
        p.examine();
        assertEquals(Message.examineNothingSpecial(p), World.currentMessage);
    }
}