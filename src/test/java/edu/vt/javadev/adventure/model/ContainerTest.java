package edu.vt.javadev.adventure.model;

import edu.vt.javadev.adventure.Message;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class ContainerTest {
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
    public void testHas() {
        assertTrue(jewelryBox.has(cellarKey));
        assertTrue(breakerBox.has(lever));
        assertTrue(bookshelf.has(diary));
        assertTrue(crate.has(record));
        assertFalse(foyer.has(turntable));
        assertFalse(foyer.has(bedroom));
    }

    @Test
    public void testExamine() {
        // no description, empty box
        Container c = new Container("c");
        c.examine();
        assertEquals(Message.examineNothingSpecial(c), World.currentMessage);

        // no description, has contents
        c.addChild(cellarKey);
        c.examine();
        assertTrue(c.getContentsDescription().contains("key"));
        assertEquals(c.getContentsDescription(), World.currentMessage);

        // no contents, description present
        c.removeChild(cellarKey);
        c.setDescription("container");
        c.examine();
        assertEquals(c.getDescription(), World.currentMessage);

        // has contents and description
        c.addChild(cellarKey);
        c.examine();
        assertEquals(c.getDescription() + " " + c.getContentsDescription(), World.currentMessage);
    }

    @Test
    public void testSearch() {
        bookshelf.search();
        assertTrue(World.currentMessage.contains("diary"));
        assertEquals(bookshelf.getContentsDescription(), World.currentMessage);
        bookshelf.removeChild(diary);
        bookshelf.search();
        assertEquals(Message.searchNothing(bookshelf), World.currentMessage);
    }

    @Test
    public void testSubtreeMap() {
        bedroomDoor.open();
        foyer.goDir("north");
        Map<String, GameObject> subtreeMap = bedroom.getSubtreeMap();
        assertTrue(subtreeMap.containsValue(bedroomDoor));
        assertTrue(subtreeMap.containsValue(player));
        assertTrue(subtreeMap.containsValue(jewelryBox));
        assertFalse(subtreeMap.containsValue(cellarKey));
        assertTrue(subtreeMap.containsValue(bedroom));
        assertTrue(subtreeMap.containsValue(flashlight));
        assertEquals(5, subtreeMap.size());

        jewelryBox.setOpen(true);
        subtreeMap = bedroom.getSubtreeMap();
        assertTrue(subtreeMap.containsValue(cellarKey));

        cellarKey.take();
        bedroom.goDir("south");
        subtreeMap = bedroom.getSubtreeMap();
        assertTrue(subtreeMap.containsValue(bedroomDoor));
        assertFalse(subtreeMap.containsValue(player));
        assertTrue(subtreeMap.containsValue(jewelryBox));
        assertFalse(subtreeMap.containsValue(cellarKey));
        assertTrue(subtreeMap.containsValue(bedroom));
        assertFalse(subtreeMap.containsValue(flashlight));
        assertEquals(3, subtreeMap.size());
    }

    @Test
    public void testCommaSep() {
        assertEquals("", foyer.getContentsDescription());
        String inventory = player.getContentsDescription();
        assertTrue(inventory.contains("a flashlight"));
        assertFalse(inventory.contains(","));
        assertFalse(inventory.contains(" and "));
        diary.take();
        inventory = player.getContentsDescription();
        assertTrue(inventory.contains("a flashlight"));
        assertTrue(inventory.contains("a diary"));
        assertFalse(inventory.contains(","));
        assertTrue(inventory.contains(" and "));
        jewelryBox.setOpen(true);
        cellarKey.take();
        diary.take();
        inventory = player.getContentsDescription();
        assertTrue(inventory.contains("a flashlight"));
        assertTrue(inventory.contains("a diary"));
        assertTrue(inventory.contains("a key"));
        assertTrue(inventory.contains(", and"));
    }

    @Test
    public void testRemoveChild() {
        assertTrue(livingRoom.has(turntable));
        assertTrue(livingRoom.has(bookshelf));
        livingRoom.removeChild(bookshelf);
        livingRoom.removeChild(turntable);
        assertFalse(livingRoom.has(turntable));
        assertFalse(livingRoom.has(bookshelf));
        assertNull(turntable.getParent());
        assertNull(bookshelf.getParent());
        try {
            livingRoom.removeChild(bookshelf);
        } catch (IllegalArgumentException e) {
            assertTrue(true);
        }
        try {
            livingRoom.removeChild(crate);
        } catch (IllegalArgumentException e) {
            assertTrue(true);
        }
    }

    @Test
    public void testAddChild() {
        try {
            bookshelf.addChild(livingRoom);
        } catch (IllegalArgumentException e) {
            assertEquals("Cannot add a GameObject as a child of itself or its descendant", e.getMessage());
        }
        try {
            bookshelf.addChild(bookshelf);
        } catch (IllegalArgumentException e) {
            assertEquals("Cannot add a GameObject as a child of itself or its descendant", e.getMessage());
        }
        try {
            bookshelf.addChild(diary);
        } catch (IllegalArgumentException e) {
            assertEquals("diary already exists in the container", e.getMessage());
        }
        try {
            bookshelf.addChild(frontDoor);
        } catch (IllegalArgumentException e) {
            assertEquals("A Door can only be added to a Room", e.getMessage());
        }
        bookshelf.addChild(record);
        assertTrue(bookshelf.has(record));
        assertEquals(bookshelf, record.getParent());
    }

    @Test
    public void testSubtreeDarkRoom() {
        cellarDoor.setOpen(true);
        foyer.goDir("west");
        Map<String, GameObject> map = cellar.getSubtreeMap();
        assertTrue(map.containsValue(player));
        assertTrue(map.containsValue(flashlight));
        assertTrue(map.containsValue(cellarDoor));
        assertTrue(map.containsValue(cellar));
        assertEquals(4, map.size());
    }

    @Test
    public void testContentsDescription() {
        assertEquals("", foyer.getContentsDescription()); // no listable items
        assertEquals("You see a turntable and a bookshelf here. In the bookshelf you see a diary.", livingRoom.getContentsDescription());
        assertEquals("In the bookshelf you see a diary.", bookshelf.getContentsDescription());
        diary.take();
        assertEquals("You are carrying a flashlight and a diary.", player.getContentsDescription());
        assertEquals("You see a jewelry-box here.", bedroom.getContentsDescription());
        jewelryBox.setOpen(true);
        assertEquals("You see a jewelry-box here. In the jewelry-box you see a key.", bedroom.getContentsDescription());
        cellarKey.take();
        assertEquals("You see a jewelry-box here.", bedroom.getContentsDescription());
        assertEquals("", jewelryBox.getContentsDescription()); // empty container
    }

    @Test
    public void testOtherConstructor() {
        Container c = new Container("c", "desc");
        assertEquals("c", c.getName());
        assertEquals("desc", c.getDescription());
        assertNotNull(c.children);
    }

    @Test
    public void testContainerWithPlayer() {
        foyer.addChild(diary);
        String contents = foyer.getContentsDescription();
        assertFalse(contents.contains("a player"));
        assertFalse(contents.contains("a flashlight"));
    }
}
