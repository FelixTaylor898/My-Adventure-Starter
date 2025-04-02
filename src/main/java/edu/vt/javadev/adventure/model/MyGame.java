package edu.vt.javadev.adventure.model;

public class MyGame {

    // declare game objects

    // Rooms:
    private final Room livingRoom;
    private final Room kitchen;

    // Doors:

    private final Door kitchenDoor;

    // Containers:

    // Persons:
    // note: player already declared and initialized in World.player

    // Items:
    private final Item safetyPin;
    private final Item peanutButter;
    private final Item pinStripedSuit;
    private final Item silverKey;

    public MyGame() {
        // initializeObjects;

        //rooms
        kitchen = new Room("kitchen");
        kitchen.setDescription("You are in a small kitchen. You can return to the living room by going south.");
        livingRoom = new Room("living-room");
        livingRoom.setDescription("You are in a living room with a small fireplace. There are several exits," +
                " but the only one you are interested in now is north to the kitchen.");

        // doors

        // player
        World.player.setDescription("You're just an average fearless adventurer.");

        // containers

        // items

        safetyPin = new Item("safety-pin");
        peanutButter = new Item("peanut-butter");
        peanutButter.setArticle("some");
        silverKey = new Item("silver-key");
        kitchenDoor = new Door("kitchen-door", silverKey);
        pinStripedSuit = new Item("pin-striped-suit");

        constructGameTree();
    }

    public void constructGameTree() {

        // rooms
        addRoom(kitchen);
        addRoom(livingRoom);

        // directions
        kitchen.setRoomDir("south", livingRoom);
        livingRoom.setRoomDir("north", kitchen);

        // doors

        livingRoom.setDoorDir("north", kitchenDoor);
        kitchen.setDoorDir("south", kitchenDoor);
        kitchenDoor.setOpen(false);
        kitchenDoor.setLocked(true);
        // player
        World.movePlayer(livingRoom);

        // containers

        // Add items to containers

        livingRoom.addChild(safetyPin);
        livingRoom.addChild(peanutButter);
        livingRoom.addChild(kitchenDoor);
        livingRoom.addChild(silverKey);
        kitchen.addChild(pinStripedSuit);

    }

    // ==========================================================
    // Private helper methods
    // ==========================================================

    private void addRoom(Room room) {
        World.root.addChild(room);
    }
}
