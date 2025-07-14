package edu.vt.javadev.adventure.model;

import edu.vt.javadev.adventure.Message;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class MyGameTest {

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

        lever = (Lever) World.map.get("lever");
        flashlight = (Light) World.map.get("flashlight");
        diary = (Book) World.map.get("diary");
        cellarKey = (Item) World.map.get("key");
        record = (Record) World.map.get("record");
        turntable = (RecordPlayer) World.map.get("turntable");
    }

    @Test
    public void cantGoDirection() {
        assertEquals(foyer, World.getCurrentRoom());
        foyer.goDir("south");
        assertEquals(Message.doorClosed(frontDoor), World.currentMessage);
        assertEquals(foyer, World.getCurrentRoom());

        foyer.goDir("west");
        assertEquals(Message.doorClosed(cellarDoor), World.currentMessage);
        assertEquals(foyer, World.getCurrentRoom());

        foyer.goDir("north");
        assertEquals(Message.doorClosed(bedroomDoor), World.currentMessage);
        assertEquals(foyer, World.getCurrentRoom());

        foyer.goDir("east");
        assertEquals("", World.currentMessage);
        assertEquals(livingRoom, World.getCurrentRoom());

        livingRoom.goDir("south");
        assertEquals(Message.cantGoDirection("south"), World.currentMessage);
        assertEquals(livingRoom, World.getCurrentRoom());
        livingRoom.goDir("north");
        assertEquals(Message.cantGoDirection("north"), World.currentMessage);
        assertEquals(livingRoom, World.getCurrentRoom());
        livingRoom.goDir("east");
        assertEquals(Message.cantGoDirection("east"), World.currentMessage);
        assertEquals(livingRoom, World.getCurrentRoom());
        livingRoom.goDir("up");
        assertEquals(Message.cantGoDirection("up"), World.currentMessage);
        assertEquals(livingRoom, World.getCurrentRoom());
    }

    @Test
    public void testEnter() {
        // item that's not in the game
        foyer.enter("cat");
        assertEquals(World.getCurrentRoom(), foyer);
        assertEquals(Message.notARoom("cat"), World.currentMessage);
        // item that isn't a room
        foyer.enter("turntable");
        assertEquals(foyer, World.getCurrentRoom());
        assertEquals(Message.notARoom("turntable"), World.currentMessage);
        // a door that's closed
        foyer.enter("bedroom");
        assertEquals(foyer, World.getCurrentRoom());
        assertEquals(Message.doorClosed(bedroomDoor), World.currentMessage);
        // a room you're already in
        foyer.enter("foyer");
        assertEquals(foyer, World.getCurrentRoom());
        assertEquals(Message.alreadyInRoom(foyer), World.currentMessage);
        // success
        foyer.enter("living-room");
        assertEquals("", World.currentMessage);
        assertEquals(livingRoom, World.getCurrentRoom());
        // room that's too far away
        livingRoom.enter("bedroom");
        assertEquals(livingRoom, World.getCurrentRoom());
        assertEquals(Message.cantGoFromHere(bedroom), World.currentMessage);
    }

    @Test
    public void testInventory() {
        assertEquals(1, player.children.size());
        assertTrue(player.has(flashlight));
        player.inventory();
        assertTrue(World.currentMessage.contains("a flashlight"));

        flashlight.setOn(true);
        record.take();
        assertTrue(player.has(record));
        assertEquals(2, player.children.size());
        player.inventory();
        assertTrue(World.currentMessage.contains("a record"));

        diary.take();
        assertTrue(player.has(diary));
        assertEquals(3, player.children.size());
        player.inventory();
        assertTrue(World.currentMessage.contains("a diary"));

        jewelryBox.setOpen(true);

        cellarKey.take();
        assertTrue(player.has(cellarKey));
        assertEquals(4, player.children.size());
        player.inventory();
        assertTrue(World.currentMessage.contains("a key"));

        player.removeChild(flashlight);
        player.removeChild(cellarKey);
        player.removeChild(diary);
        player.removeChild(record);

        assertTrue(player.isEmpty());
        player.inventory();
        assertEquals(Message.inventoryEmpty, World.currentMessage);
    }



    @Test
    public void testPuzzleStepByStep() {
        // Start: player is in the foyer
        assertEquals(foyer, World.getCurrentRoom());
        assertFalse(flashlight.isOn());
        assertFalse(World.hasPower());
        assertFalse(World.flashlightOn());
        assertTrue(World.isHaunted());

        // try to open the front door
        frontDoor.open();
        assertFalse(frontDoor.isOpen());
        assertEquals(Message.hauntedDoorMessage, World.currentMessage);

        // try to open the cellar door
        cellarDoor.open();
        assertFalse(cellarDoor.isOpen());
        assertEquals(Message.openNotYetUnlocked(cellarDoor), World.currentMessage);

        // Go to the livingroom and read the diary
        foyer.goDir("east");
        assertEquals("", World.currentMessage);
        assertEquals(livingRoom, World.getCurrentRoom());

        // Try to read the diary - flashlight is off
        diary.read();
        assertEquals(Message.readNoLight(diary), World.currentMessage);

        // Turn on the flashlight
        flashlight.turnOn();
        assertTrue(flashlight.isOn());
        assertEquals(Message.turnOnSuccess(flashlight), World.currentMessage);

        // Read the diary
        diary.read();
        assertEquals(diary.getReadText(), World.currentMessage);
        assertTrue(World.currentMessage.contains(jewelryBox.getCombination()));

        // return to the foyer
        livingRoom.goDir("west");
        assertEquals("", World.currentMessage);
        assertEquals(foyer, World.getCurrentRoom());

        // try to go to the bedroom
        foyer.goDir("north");
        assertEquals(Message.doorClosed(bedroomDoor), World.currentMessage);
        assertEquals(foyer, World.getCurrentRoom());

        // open bedroom door
        bedroomDoor.open();
        assertTrue(bedroomDoor.isOpen());
        assertEquals(Message.openOkay(bedroomDoor), World.currentMessage);

        // go to the bedroom
        foyer.goDir("north");
        assertEquals("", World.currentMessage);
        assertEquals(bedroom, World.getCurrentRoom());

        // try to open the jewelry box
        jewelryBox.open();
        assertFalse(jewelryBox.isOpen());
        assertEquals(Message.openNotYetUnlocked(jewelryBox), World.currentMessage);

        // simulate unlocking the jewelry box
        jewelryBox.setLocked(false);

        // open jewelry box
        jewelryBox.open();
        assertTrue(jewelryBox.isOpen());
        assertEquals(Message.openOkay(jewelryBox) + " " + jewelryBox.getContentsDescription(), World.currentMessage);

        // take the key
        cellarKey.take();
        assertTrue(player.has(cellarKey));
        assertEquals(Message.takeOkay(cellarKey), World.currentMessage);

        flashlight.turnOff();

        // return to foyer
        bedroom.goDir("south");
        assertEquals("", World.currentMessage);
        assertEquals(foyer, World.getCurrentRoom());

        // unlock cellar door
        cellarDoor.unlock();
        assertFalse(cellarDoor.isLocked());
        assertEquals(Message.unlockOkay(cellarDoor), World.currentMessage);

        // open cellar door
        cellarDoor.open();
        assertTrue(cellarDoor.isOpen());
        assertEquals(Message.openOkay(cellarDoor), World.currentMessage);

        // go to the cellar
        foyer.goDir("west");
        assertEquals("", World.currentMessage);
        assertEquals(cellar, World.getCurrentRoom());


        flashlight.turnOn();

        record.take();
        assertTrue(player.has(record));
        assertEquals(Message.takeOkay(record), World.currentMessage);

        cellar.goDir("east");

        // try to place the turntable when there's no turntable nearby
        record.place();
        assertEquals(Message.placeTooFar(record), World.currentMessage);

        foyer.goDir("east");

        // place the record on the turntable
        record.place();
        assertEquals(turntable, record.getParent());
        assertEquals(Message.placeSuccess(record, turntable), World.currentMessage);

        // try to play the record
        turntable.play();
        assertFalse(turntable.isPlaying());
        assertEquals(Message.noPower, World.currentMessage);

        livingRoom.goDir("west");
        foyer.goDir("west");

        // open the breaker box
        breakerBox.open();
        assertTrue(breakerBox.isOpen());
        assertEquals(Message.openOkay(breakerBox) + " " + breakerBox.getContentsDescription(), World.currentMessage);

        // pull the lever
        lever.pull();
        assertTrue(lever.isOn());
        assertTrue(World.hasPower());

        cellar.goDir("east");
        foyer.goDir("east");

        // play the record
        turntable.play();
        assertTrue(turntable.isPlaying());
        assertEquals(Message.playSuccessHaunted(turntable, record), World.currentMessage);
        assertFalse(World.isHaunted());

        livingRoom.goDir("west");

        // successfully open the front door
        frontDoor.open();
        assertTrue(frontDoor.isOpen());
        World.exit();
        assertEquals(outside, World.getCurrentRoom());

    }

    // This test method is illustrative of how to create a test that has
    // nothing to do with the scenario created in the MyGame class.
    @Test
    public void testLoopingThroughFourRooms() {
        World.reset();

        // Create rooms
        Room roomA = new Room("room-a");
        roomA.setDescription("You are in room A.");

        Room roomB = new Room("room-b");
        roomB.setDescription("You are in room B.");

        Room roomC = new Room("room-c");
        roomC.setDescription("You are in room C.");

        Room roomD = new Room("room-d");
        roomD.setDescription("You are in room D.");

        // Connect rooms (A → south → B → west → C → north → D → east → A)
        roomA.setRoomDir("south", roomB);
        roomB.setRoomDir("west", roomC);
        roomC.setRoomDir("north", roomD);
        roomD.setRoomDir("east", roomA);

        // Add all rooms to the world tree
        World.root.addChild(roomA);
        World.root.addChild(roomB);
        World.root.addChild(roomC);
        World.root.addChild(roomD);

        // Start the player in room A
        World.movePlayer(roomA);

        // Move south to B
        roomA.goDir("south");
        assertSame(roomB, World.getCurrentRoom());

        // Move west to C
        roomB.goDir("west");
        assertSame(roomC, World.getCurrentRoom());

        // Move north to D
        roomC.goDir("north");
        assertSame(roomD, World.getCurrentRoom());

        // Move east back to A
        roomD.goDir("east");
        assertSame(roomA, World.getCurrentRoom());
    }

    @Test
    public void testRandomGhostMessage() {
        assertFalse(Message.randomGhostMessage().isEmpty());
        assertFalse(Message.randomGhostMessage().isEmpty());
        assertFalse(Message.randomGhostMessage().isEmpty());
        assertFalse(Message.randomGhostMessage().isEmpty());
        assertFalse(Message.randomGhostMessage().isEmpty());
        assertFalse(Message.randomGhostMessage().isEmpty());
    }

    @Test
    public void testMessagesMisc() {
        assertEquals("You don't see any cat here.", Message.scopeNotFound("cat"));
        assertEquals("The game parser supports a maximum of two words.", Message.parserTooManyWords);
        assertEquals("Unknown command: pet", Message.unknownCommand("pet"));
        assertEquals("Unknown command: pet cat", Message.unknownCommand("pet", "cat"));
        assertEquals("The lever is not something you can open.", Message.openCant(lever));
        assertEquals("The lever is not something you can close.", Message.closeCant(lever));
        assertEquals("The lever is not something you can turn on.", Message.turnOnCant(lever));
        assertEquals("The lever is not something you can turn off.", Message.turnOffCant(lever));
        assertEquals("The lever is not something you can play.", Message.playCant(lever));
        assertEquals("The lever is not something you can read.", Message.readCant(lever));
        assertEquals("The lever is not something you can place.", Message.placeCant(lever));
        assertEquals("The diary is not something you can pull.", Message.pullCant(diary));
    }
}