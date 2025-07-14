package edu.vt.javadev.adventure.model;

import edu.vt.javadev.adventure.Message;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class RecordTest {

    private Room foyer;

    private Record record;
    private RecordPlayer turntable;

    @BeforeEach
    public void setup() {
        // Reset global state before each test
        World.reset();

        // Initialize the game
        new MyGame();
        record = (Record) World.map.get("record");
        turntable = (RecordPlayer) World.map.get("turntable");
        foyer = (Room) World.map.get("foyer");
    }

    @Test
    public void testInvalidArgs() {
        try {
            new Record("a", "", turntable);
        } catch (IllegalArgumentException e) {
            assertTrue(true);
        }
        try {
            new Record("b", null, turntable);
        } catch (IllegalArgumentException e) {
            assertTrue(true);
        }
        try {
            new Record("c", "song", null);
        } catch (IllegalArgumentException e) {
            assertTrue(true);
        }
    }

    @Test
    public void testPlaceNotInInventory() {
        record.place();
        assertEquals(Message.notInInventory(record), World.currentMessage);
    }

    @Test
    public void testIsPlaced() {
        assertFalse(record.isPlaced());
        record.take();
        assertFalse(record.isPlaced());
        foyer.goDir("east");
        record.place();
        assertTrue(record.isPlaced());

    }

    @Test
    public void testIsPlaying() {
        assertFalse(record.isPlaying());
        record.take();
        assertFalse(record.isPlaying());
        foyer.goDir("east");
        record.place();
        assertFalse(record.isPlaying());
        World.setPower(true);
        record.place();
        record.play();
        assertTrue(record.isPlaying());
    }

    @Test
    public void testPlayNotPlaced() {
        record.play();
        assertFalse(record.isPlaying());
        assertEquals(Message.playMustPlace(record), World.currentMessage);
    }

    @Test
    public void testExamine() {
        record.examine();
        assertEquals(record.getDescription(), World.currentMessage);
        record.take();
        foyer.goDir("east");
        record.place();
        record.examine();
        assertEquals(record.getDescription() + " The record is on the turntable.", World.currentMessage);
        World.setPower(true);
        turntable.play();
        record.examine();
        assertEquals(record.getDescription() + " Morning Dreams is playing on the turntable.", World.currentMessage);
    }

    @Test
    public void testExamineNothingSpecial() {
        Record r = new Record("rec", "song", turntable);
        r.examine();
        assertEquals(Message.examineNothingSpecial(r), World.currentMessage);
        foyer.addChild(r);
        r.take();
        foyer.goDir("east");
        r.place();
        r.examine();
        assertEquals("The rec is on the turntable.", World.currentMessage);
        r.take();
        assertEquals(Message.takeCant(r), World.currentMessage);
    }

}
