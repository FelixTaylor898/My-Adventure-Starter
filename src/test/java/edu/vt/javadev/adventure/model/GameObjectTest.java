package edu.vt.javadev.adventure.model;

import edu.vt.javadev.adventure.Message;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class GameObjectTest {
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
    public void testGetParent() {
        assertEquals(foyer, player.getParent());
        assertEquals(bookshelf, diary.getParent());
        assertEquals(livingRoom, bookshelf.getParent());
        assertEquals(crate, record.getParent());
        assertEquals(breakerBox, lever.getParent());
        assertEquals(cellar, crate.getParent());
        assertEquals(jewelryBox, cellarKey.getParent());
        assertEquals(bedroom, jewelryBox.getParent());
        assertEquals(World.root, bedroom.getParent());
        assertEquals(World.root, livingRoom.getParent());
        assertEquals(World.root, cellar.getParent());
        assertEquals(World.root, foyer.getParent());
        assertEquals(World.root, outside.getParent());
        diary.take();
        jewelryBox.setOpen(true);
        cellarKey.take();
        flashlight.setOn(true);
        record.take();
        assertEquals(player, diary.getParent());
        assertEquals(player, record.getParent());
        assertEquals(player, flashlight.getParent());
        foyer.goDir("east");
    }

    @Test
    public void testStringHelpers() {
        assertEquals("the diary", diary.theName());
        assertEquals("the key", cellarKey.theName());
        assertEquals("the jewelry-box", jewelryBox.theName());
        assertEquals("the front-door", frontDoor.theName());
        assertEquals("The diary", diary.capTheName());
        assertEquals("The key", cellarKey.capTheName());
        assertEquals("The jewelry-box", jewelryBox.capTheName());
        assertEquals("The front-door", frontDoor.capTheName());
        GameObject owl = new GameObject("owl");
        GameObject apple = new GameObject("apple");
        assertEquals("a diary", diary.aName());
        assertEquals("a jewelry-box", jewelryBox.aName());
        assertEquals("a key", cellarKey.aName());
        assertEquals("an outside", outside.aName());
        assertEquals("an owl", owl.aName());
        assertEquals("an apple", apple.aName());
        GameObject cats = new GameObject("cats");
        assertEquals("cats", cats.aName());
        assertEquals("A diary", diary.capAName());
        assertEquals("A front-door", frontDoor.capAName());
        assertEquals("A key", cellarKey.capAName());
        assertEquals("An outside", outside.capAName());
        assertEquals("An owl", owl.capAName());
        assertEquals("An apple", apple.capAName());
        assertEquals("Cats", cats.capAName());
    }

    @Test
    public void testHasAncestor() {
        assertTrue(livingRoom.hasAncestor(World.root));
        assertFalse(livingRoom.hasAncestor(bookshelf));
        assertTrue(bookshelf.hasAncestor(livingRoom));
        assertTrue(diary.hasAncestor(livingRoom));
        assertTrue(diary.hasAncestor(World.root));
        assertTrue(flashlight.hasAncestor(player));
        assertTrue(flashlight.hasAncestor(foyer));
        assertTrue(flashlight.hasAncestor(World.root));
        assertFalse(diary.hasAncestor(jewelryBox));
        assertFalse(diary.hasAncestor(flashlight));
    }



    @Test
    public void miscTests() {
        GameObject object = new GameObject("object");
        object.examine();
        assertEquals(Message.examineNothingSpecial(object), World.currentMessage);
        object.search();
        assertEquals(Message.searchNothing(object), World.currentMessage);
        assertEquals("", object.getStateDescription());
    }


    @Test
    public void testConstructor() {
        try {
           new GameObject("");
        } catch (IllegalArgumentException e) {
                assertTrue(true);
        }

        try {
            new GameObject("Bedroom");
        } catch (IllegalArgumentException e) {
            assertTrue(true);
        }

        try {
            new GameObject("LivingRoom");
        } catch (IllegalArgumentException e) {
            assertTrue(true);
        }

        try {
            new GameObject("garage");
        } catch (IllegalArgumentException e) {
            assertTrue(true);
        }
    }

    @Test
    public void testNameExistsAlready() {
        try {
            new GameObject("record");
        } catch (IllegalArgumentException e) {
            assertEquals("A game object with the name 'record' already exists in the map.", e.getMessage());
        }
    }

    @Test
    public void testExamine() {
        GameObject object = new GameObject("a", "b");
        object.examine();
        assertEquals("b", World.currentMessage);
        Door door = new Door("door");
        door.examine();
        assertEquals(door.getStateDescription(), World.currentMessage);
        door.setDescription("a door.");
        door.examine();
        assertEquals("a door. " + door.getStateDescription(), World.currentMessage);
    }

    @Test
    public void testInvalidDescription() {
        try {
            new GameObject("object", null);
        } catch (IllegalArgumentException e) {
            assertTrue(true);
        }
    }

    @Test
    public void testInvalidName() {
        try {
            new GameObject(null);
        } catch (IllegalArgumentException e) {
            assertTrue(true);
        }
    }
}
