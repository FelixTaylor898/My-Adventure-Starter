package edu.vt.javadev.adventure.model;

import edu.vt.javadev.adventure.Message;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class MyGameTest {

    private Room livingRoom;
    private Room kitchen;
    private Door kitchenDoor;
    private Item silverKey;

    @BeforeEach
    public void setup() {
        // Reset global state before each test
        World.reset();

        // Initialize the game
        new MyGame();

        // Retrieve references to relevant objects
        livingRoom = (Room) World.map.get("living-room");
        kitchen = (Room) World.map.get("kitchen");
        kitchenDoor = (Door) World.map.get("kitchen-door");
        silverKey = (Item) World.map.get("silver-key");
    }

    @Test
    public void testKitchenDoorPuzzleStepByStep() {
        // Start: player is in the living room
        assertEquals(livingRoom, World.getCurrentRoom());

        // Try to go north -> door is closed
        livingRoom.goDir("north");
        assertEquals(kitchenDoor.capTheName() + " is closed.", World.currentMessage);
        assertEquals(livingRoom, World.getCurrentRoom());

        // Try to open door -> it's locked
        kitchenDoor.open();
        assertEquals(Message.openNotYetUnlocked(kitchenDoor), World.currentMessage);
        assertFalse(kitchenDoor.isOpen());

        // Try to unlock door -> you don't have the key
        kitchenDoor.unlock();
        assertEquals(Message.unlockNotYetKey(kitchenDoor, silverKey), World.currentMessage);
        assertTrue(kitchenDoor.isLocked());

        // Take the silver key
        silverKey.take();
        assertTrue(World.player.children.contains(silverKey));
        assertEquals(Message.takeOkay(silverKey), World.currentMessage);

        // Unlock the door
        kitchenDoor.unlock();
        assertFalse(kitchenDoor.isLocked());
        assertEquals(Message.unlockOkay(kitchenDoor), World.currentMessage);

        // Open the door
        kitchenDoor.open();
        assertTrue(kitchenDoor.isOpen());
        assertEquals(Message.openOkay(kitchenDoor), World.currentMessage);

        // Go north to the kitchen
        livingRoom.goDir("north");
        assertEquals(kitchen, World.getCurrentRoom());
        assertEquals("", World.currentMessage); // Message cleared by movePlayer
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

}