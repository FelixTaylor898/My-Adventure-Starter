package edu.vt.javadev.adventure.model;

import edu.vt.javadev.adventure.Message;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ItemTest {

    private Person player;

    @BeforeEach
    public void setup() {
        // Reset global state before each test
        World.reset();
        // Initialize the game
        new MyGame();
        player = World.player;
    }

    @Test
    public void testCantTake() {
        Item item = new Item("item");
        item.take();
        assertEquals(Message.takeCant(item), World.currentMessage);
        assertFalse(player.has(item));
        World.feedToTheVoid(item);
        item.take();
        assertFalse(player.has(item));
        assertEquals(Message.takeCant(item), World.currentMessage);
    }

}
