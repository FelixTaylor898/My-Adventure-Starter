package edu.vt.javadev.adventure.model;

import edu.vt.javadev.adventure.Message;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class RecordPlayerTest {

    private Room foyer;

    // Persons:
    // note: player already declared and initialized in World.player

    // Items:
    private Lever lever;

    private Record record;
    private RecordPlayer turntable;

    @BeforeEach
    public void setup() {
        // Reset global state before each test
        World.reset();

        // Initialize the game
        new MyGame();
        foyer = (Room) World.map.get("foyer");
        lever = (Lever) World.map.get("lever");
        record = (Record) World.map.get("record");
        turntable = (RecordPlayer) World.map.get("turntable");
    }

    @Test
    public void testPlayAlready() {
        record.take();
        foyer.goDir("east");
        record.place();
        lever.pull();
        turntable.play();
        assertTrue(turntable.isPlaying());
        turntable.turnOn();
        assertEquals(Message.turnOnAlready(turntable), World.currentMessage);
    }

    @Test
    public void testTurnOff() {
        turntable.turnOff();
        assertEquals(Message.turnOffAlready(turntable), World.currentMessage);
        record.take();
        foyer.goDir("east");
        record.place();
        lever.pull();
        turntable.play();
        assertTrue(turntable.isPlaying());
        turntable.turnOff();
        assertFalse(turntable.isPlaying());
        assertEquals(Message.turnOffSuccess(turntable), World.currentMessage);
    }

    @Test
    public void testExamine() {
        record.take();
        foyer.goDir("east");
        record.place();
        lever.pull();
        turntable.play();
        turntable.examine();
        assertTrue(turntable.isOn());
        assertEquals(turntable.getDescription() + " " + turntable.getStateDescription(), World.currentMessage);
        lever.pull();
        turntable.examine();
        assertFalse(turntable.isOn());
        assertEquals(turntable.getDescription() + " " + turntable.getStateDescription(), World.currentMessage);
    }

    @Test
    public void testStateNoRecord() {
        assertEquals("The turntable is off and has no record.", turntable.getStateDescription());
    }

    @Test
    public void testPlayNoRecord() {
        World.setPower(true);
        turntable.play();
        assertEquals(Message.playNoRecord(turntable), World.currentMessage);
    }

    @Test
    public void testNoDescription() {
        RecordPlayer rp = new RecordPlayer("rp");
        rp.examine();
        assertEquals(rp.getStateDescription(), World.currentMessage);
    }

    @Test
    public void testPlayNotHaunted() {
        record.take();
        foyer.goDir("east");
        record.place();
        World.setPower(true);
        World.setHaunted(false);
        turntable.play();
        assertEquals(Message.playSuccessNotHaunted(turntable, record), World.currentMessage);
    }

}
