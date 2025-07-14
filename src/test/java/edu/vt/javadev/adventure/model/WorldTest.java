package edu.vt.javadev.adventure.model;

import edu.vt.javadev.adventure.Message;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class WorldTest {

    private Room bedroom;
    private Room foyer;
    private DarkRoom cellar;
    private Room livingRoom;
    private Room outside;

    // Doors:
    private Door cellarDoor;
    private Door bedroomDoor;
    private HauntedDoor frontDoor;

    // Containers:
    private Box breakerBox;
    private CombinationBox jewelryBox;
    private Container crate;
    private Container bookshelf;

    // Persons:
    // note: player already declared and initialized in World.player

    // Items:
    private Lever lever;
    private Light flashlight;
    private Book diary;
    private Item cellarKey;
    private Record record;
    private RecordPlayer turntable;

    private Person player;

    @BeforeEach
    public void setup() {
        // Reset global state before each test
        World.reset();

        // Initialize the game
        new MyGame();

        player = World.player;

        // Retrieve references to relevant objects
        bedroom = (Room) World.map.get("bedroom");
        outside = (Room) World.map.get("outside");
        cellar = (DarkRoom) World.map.get("cellar");
        livingRoom = (Room) World.map.get("living-room");
        foyer = (Room) World.map.get("foyer");

        cellarDoor = (Door) World.map.get("cellar-door");
        bedroomDoor = (Door) World.map.get("bedroom-door");
        frontDoor = (HauntedDoor) World.map.get("front-door");

        breakerBox = (Box) World.map.get("breaker-box");
        jewelryBox = (CombinationBox) World.map.get("jewelry-box");

        crate = (Container) World.map.get("crate");
        bookshelf = (Container) World.map.get("bookshelf");

        lever = (Lever) World.map.get("lever");
        flashlight = (Light) World.map.get("flashlight");
        diary = (Book) World.map.get("diary");
        cellarKey = (Item) World.map.get("key");
        record = (Record) World.map.get("record");
        turntable = (RecordPlayer) World.map.get("turntable");
    }

    @Test
    public void testCapitalize() {
        assertEquals("Cat", World.capitalize("cat"));
        assertEquals("Zoo", World.capitalize("zoo"));
        assertEquals("Dog", World.capitalize("Dog"));
        assertEquals("Hello", World.capitalize("hello"));
        assertEquals("123", World.capitalize("123"));
    }

    @Test
    public void testFindRoom() {
        assertNull(World.findRoom(""));
        assertNull(World.findRoom(null));
        assertNull(World.findRoom("screwdriver"));
        assertNull(World.findRoom("backyard"));
        assertEquals(livingRoom, World.findRoom("living-room"));
        assertEquals(foyer, World.findRoom("foyer"));
    }

    @Test
    public void testTakeMisc() {
        Item object = new Item("item");
        GameObject object2 = new GameObject("object");
        object.setParent(object2);
        try {
            World.moveItemToPlayer(object);
        } catch(IllegalStateException e) {
            assertTrue(true);
        }
    }

    @Test
    public void testCantExit() {
        foyer.goDir("east");
        World.exit();
        assertEquals(Message.exitCant, World.currentMessage);
    }

    @Test
    public void testGetCurrentRoomFail() {
        bookshelf.addChild(player);
        try {
            World.getCurrentRoom();
        } catch (IllegalStateException e) {
            assertTrue(true);
        }
    }

    @Test
    public void testFindRoomNotARoom() {
        World.root.addChild(new Item("item"));
        assertNull(World.findRoom("item"));
    }

    @Test
    public void testFlashlightOnFail() {
        World.setFlashlight(null);
        assertFalse(World.flashlightOn());
    }
}
