package edu.vt.javadev.adventure.model;

import edu.vt.javadev.adventure.Message;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class DoorTest {
    private Room foyer;
    private DarkRoom cellar;


    // Doors:
    private Door cellarDoor;
    private Door bedroomDoor;
    private HauntedDoor frontDoor;

    // Containers:


    // Persons:
    // note: player already declared and initialized in World.player

    // Items:
    private Light flashlight;
    private Item cellarKey;


    private Person player;

    @BeforeEach
    public void setup() {
        // Reset global state before each test
        World.reset();

        // Initialize the game
        new MyGame();

        player = World.player;

        // Retrieve references to relevant objects
        cellar = (DarkRoom) World.map.get("cellar");
        foyer = (Room) World.map.get("foyer");

        cellarDoor = (Door) World.map.get("cellar-door");
        bedroomDoor = (Door) World.map.get("bedroom-door");
        frontDoor = (HauntedDoor) World.map.get("front-door");

        flashlight = (Light) World.map.get("flashlight");
        cellarKey = (Item) World.map.get("key");
    }

    @Test
    public void testOpen() {
        assertFalse(cellarDoor.isOpen());
        assertEquals("The cellar-door is closed and locked.", cellarDoor.getStateDescription());
        cellarDoor.open();
        assertEquals(Message.openNotYetUnlocked(cellarDoor), World.currentMessage);
        assertFalse(cellarDoor.isOpen());
        cellarDoor.setLocked(false);
        assertEquals("The cellar-door is closed but unlocked.", cellarDoor.getStateDescription());
        cellarDoor.open();
        assertTrue(cellarDoor.isOpen());
        assertEquals(Message.openOkay(cellarDoor), World.currentMessage);
        assertEquals("The cellar-door is open.", cellarDoor.getStateDescription());
        cellarDoor.open();
        assertEquals(Message.openAlready(cellarDoor), World.currentMessage);
    }

    @Test
    public void testClose() {
        assertFalse(bedroomDoor.isOpen());
        bedroomDoor.close();
        assertFalse(bedroomDoor.isOpen());
        assertEquals(Message.closeAlready(bedroomDoor), World.currentMessage);
        assertEquals("The bedroom-door is closed.", bedroomDoor.getStateDescription());
        bedroomDoor.open();
        assertEquals("The bedroom-door is open.", bedroomDoor.getStateDescription());
        assertTrue(bedroomDoor.isOpen());
        bedroomDoor.close();
        assertEquals("The bedroom-door is closed.", bedroomDoor.getStateDescription());
        assertFalse(bedroomDoor.isOpen());
        assertEquals(Message.closeOkay(bedroomDoor), World.currentMessage);
    }

    @Test
    public void testLock() {
        assertTrue(cellarDoor.isLocked());
        cellarDoor.lock();
        assertEquals(Message.lockAlready(cellarDoor), World.currentMessage);
        assertEquals("The cellar-door is closed and locked.", cellarDoor.getStateDescription());
        cellarDoor.setLocked(false);
        cellarDoor.setOpen(true);
        cellarDoor.lock();
        assertFalse(cellarDoor.isLocked());
        assertEquals(Message.lockNotYetClosed(cellarDoor), World.currentMessage);
        assertEquals("The cellar-door is open.", cellarDoor.getStateDescription());
        cellarDoor.setOpen(false);
        cellarDoor.lock();
        assertFalse(cellarDoor.isLocked());
        assertEquals(Message.lockNotYetKey(cellarDoor, cellarKey), World.currentMessage);
        assertEquals("The cellar-door is closed but unlocked.", cellarDoor.getStateDescription());
        player.addChild(cellarKey);
        cellarDoor.lock();
        assertEquals(Message.lockOkay(cellarDoor), World.currentMessage);
        assertTrue(cellarDoor.isLocked());
        assertEquals("The cellar-door is closed and locked.", cellarDoor.getStateDescription());
    }

    @Test
    public void testUnlock() {
        cellarDoor.unlock();
        assertTrue(cellarDoor.isLocked());
        assertEquals("The cellar-door is closed and locked.", cellarDoor.getStateDescription());
        assertEquals(Message.unlockNotYetKey(cellarDoor, cellarKey), World.currentMessage);
        player.addChild(cellarKey);
        cellarDoor.unlock();
        assertFalse(cellarDoor.isLocked());
        assertEquals("The cellar-door is closed but unlocked.", cellarDoor.getStateDescription());
        assertEquals(Message.unlockOkay(cellarDoor), World.currentMessage);
        cellarDoor.unlock();
        assertEquals(Message.unlockAlready(cellarDoor), World.currentMessage);
    }

    @Test
    public void testDoorNoKey() {
        bedroomDoor.setOpen(false);
        bedroomDoor.setLocked(false);
        bedroomDoor.lock();
        assertEquals(Message.lockCant(bedroomDoor), World.currentMessage);
        bedroomDoor.unlock();
        assertEquals(Message.unlockAlready(bedroomDoor), World.currentMessage);
    }

    @Test
    public void testWithKeyConstructor() {
        Item item = new Item("item");
        Door door = new Door("door", item);
        assertEquals(item, door.getWithKey());
    }



    @Test
    public void testNullKey() {
        try {
            new Door("door", null);
        } catch (IllegalArgumentException e) {
            assertTrue(true);
        }
    }

    @Test
    public void testSearch() {
        assertEquals(foyer, World.getCurrentRoom());
        bedroomDoor.search();
        assertEquals(Message.searchNothing(bedroomDoor), World.currentMessage);
        cellarDoor.search();
        assertEquals(Message.searchNothing(cellarDoor), World.currentMessage);
        frontDoor.search();
        assertEquals(Message.searchNothing(frontDoor), World.currentMessage);
    }

    @Test
    public void testExamine() {
        assertEquals(foyer, World.getCurrentRoom());
        bedroomDoor.examine();
        assertEquals(bedroomDoor.getDescription() + " " + bedroomDoor.getStateDescription(), World.currentMessage);
        bedroomDoor.setDescription("");
        bedroomDoor.examine();
        assertEquals(bedroomDoor.getStateDescription(), World.currentMessage);
    }



}
