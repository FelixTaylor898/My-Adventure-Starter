package edu.vt.javadev.adventure.model;

import java.util.Random;

public class MyGame {

    // declare game objects

    // Rooms:

    private final Room bedroom;
    private final Room foyer;
    private final DarkRoom cellar;
    private final Room livingRoom;
    private final Room outside;

    // Doors:
    private final Door cellarDoor;
    private final Door bedroomDoor;
    private final HauntedDoor frontDoor;

    // Containers:
    private final Box breakerBox;
    private final CombinationBox jewelryBox;
    private final Container crate;
    private final Container bookshelf;

    // Persons:
    // note: player already declared and initialized in World.player

    // Items:
    private final Lever lever;
    private final Light flashlight;
    private final Book diary;
    private final Item cellarKey;
    private final Record record;
    private final RecordPlayer turntable;

    public MyGame() {
        // initializeObjects;
        World.setHaunted(true);

        //rooms
        bedroom = new Room("bedroom");
        bedroom.setDescription("You are in your great aunt Eleanor's bedroom. Faded floral sheets complements the rot creeping through the floorboards. You see a jewelry box with a ballerina on top, her porcelain face chipped and smiling. The door to the foyer is to the south.");
        foyer = new Room("foyer");
        foyer.setDescription("You are in the foyer of your great aunt Eleanor's abandoned house. The curtains move ominously, and shadows shuffle on the walls. The door to her bedroom is to your north. The door to the cellar is to your west. The living room is to the east. The front door is to the south.");
        livingRoom = new Room("living-room");
        livingRoom.setDescription("You are in the living room. The wallpaper has peeled like dead skin, and a moldy armchair still faces the enshadowed fireplace, as if waiting for someone long buried. A crooked bookshelf leans against the wall, while an old turntable sits in the shadows. The foyer is to the west.");
        cellar = new DarkRoom("cellar");
        cellar.setDescription("You are in the cellar. It's choked with cobwebs thick as cotton, every corner crawling with spiders that skitter away from your shoes. The door to the foyer is to the east. You see an old rusted breaker box on the wall and a crate of dusty vinyl records in the corner.");
        outside = new Room("outside");
        outside.setDescription("You are outside, relieved to know that you have freed your great aunt's spirit. Congratulations! You have solved the \"haunted house\" mystery!");

        // player
        World.player.setDescription("The ghost of your great aunt, Eleanor, haunts her old abandoned house. In order to free her, you must play the record she was given shortly before her death. Only then can you exit the house.");

        String combination = generateRandomCombination();
        System.out.println(combination);

        // containers
        breakerBox = new Box("breaker-box");
        breakerBox.setDescription("The breaker-box lurks on the wall like a sealed metal coffin, its surface marred with rust and grime.");
        breakerBox.setOpen(false);
        breakerBox.setLocked(false);
        jewelryBox = new CombinationBox("jewelry-box", combination);
        jewelryBox.setDescription("The jewelry-box is decorated with chipped porcelain and flaking gold paint, and the ballerina on top spins endlessly to a distorted lullaby. The jewelry-box has a combination lock.");
        jewelryBox.setLocked(true);
        jewelryBox.setOpen(false);
        crate = new Container("crate");
        crate.setDescription("The dusty crate sits in the corner, packed with warped vinyl records whose faded labels whisper songs of sorrow.");
        bookshelf = new Container("bookshelf");
        bookshelf.setDescription("The bookshelf is crooked and sagging, its dark wood splintered and heavy with forgotten books.");
        turntable = new RecordPlayer("turntable");
        turntable.setDescription("The turntable is aged and scratched, its wooden frame darkened by years of neglect.");

        // items
        String readText = "You open the diary to the marked page. \"Dear diary, today I was gifted a record called 'Morning Dreams.' I cannot wait to listen to it!\" Seconds before your flashlight flickers off, you spot a combination written in blood: " + combination;
        diary = new Book("diary", readText);
        diary.setDescription("The diary's pages are yellowed and curling at the edges. It has a piece of frayed lace marking your great aunt's last entry prior to her death.");
        cellarKey = new Item("key");
        cellarKey.setDescription("The cellar key's teeth are uneven and rust-bitten. It's cold to the touch.");
        record = new Record("record", "Morning Dreams", turntable);
        record.setDescription("The label for Morning Dreams is streaked with dried blood and smeared fingerprints.");

        // doors
        cellarDoor = new Door("cellar-door", cellarKey);
        cellarDoor.setDescription("The cellar-door's wood is splintered and dark with old stains");
        cellarDoor.setLocked(true);
        cellarDoor.setOpen(false);
        bedroomDoor = new Door("bedroom-door");
        bedroomDoor.setDescription("The bedroom-door is scuffed and bowed, its paint peeling near the knob, which rattles faintly even when no one is on the other side.");
        bedroomDoor.setOpen(false);
        frontDoor = new HauntedDoor("front-door");
        frontDoor.setDescription("The front-door looms tall and warped, its peephole dark and foreboding.");
        frontDoor.setLocked(false);
        frontDoor.setOpen(false);

        // switches
        lever = new Lever("lever");
        flashlight = new Light("flashlight");
        flashlight.setDescription("The flashlight is battered and dented, its once-bright casing now dull.");
        flashlight.setOn(false);

        World.setFlashlight(flashlight);

        constructGameTree();
    }

    private void constructGameTree() {
        // rooms
        addRoom(outside);
        addRoom(bedroom);
        addRoom(cellar);
        addRoom(livingRoom);
        addRoom(foyer);

        // directions
        foyer.setRoomDir("north", bedroom);
        foyer.setRoomDir("west", cellar);
        foyer.setRoomDir("south", outside);
        foyer.setRoomDir("east", livingRoom);

        cellar.setRoomDir("east", foyer);

        bedroom.setRoomDir("south", foyer);

        livingRoom.setRoomDir("west", foyer);


        // doors
        foyer.setDoorDir("north", bedroomDoor);
        foyer.setDoorDir("west", cellarDoor);
        foyer.setDoorDir("south", frontDoor);

        cellar.setDoorDir("east", cellarDoor);

        bedroom.setDoorDir("south", bedroomDoor);

        // player
        World.movePlayer(foyer);

        // containers
        cellar.addChild(breakerBox);
        cellar.addChild(crate);
        livingRoom.addChild(turntable);
        bedroom.addChild(jewelryBox);
        livingRoom.addChild(bookshelf);

        // Add items to containers
        breakerBox.addChild(lever);
        jewelryBox.addChild(cellarKey);
        crate.addChild(record);
        bookshelf.addChild(diary);

        World.player.addChild(flashlight);

    }

    // ==========================================================
    // Private helper methods
    // ==========================================================

    private void addRoom(Room room) {
        World.root.addChild(room);
    }

    private static String generateRandomCombination() {
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 3; i++) {
            sb.append(random.nextInt(10));
            sb.append(random.nextInt(10));
            if (i < 2) {
                sb.append("-");
            }
        }
        return sb.toString();
    }
}
