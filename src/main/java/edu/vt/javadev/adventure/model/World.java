package edu.vt.javadev.adventure.model;

import edu.vt.javadev.adventure.Message;

import java.util.HashMap;
import java.util.Map;

/**
 * Project 4 World class.
 * @author Felix Taylor
 */
public class World {

    private static boolean isHaunted;
    private static boolean hasPower;
    private static Light flashlight;

    // ----------------------------------------------------------
    // Global variables that hold strings to be used by the View
    // ----------------------------------------------------------

    /*
        These strings will be used to fill in the output. The roomName will go into the roomName label,
        the roomDescription will go into the outputArea web view, and the currentMessage will
        go into the outputMessageArea text flow.
     */

    public static String roomName = "";
    public static String roomDescription = "";
    public static String currentMessage = "";

    // ----------------------------------------------------------
    // World map (from unique names to game objects
    // ----------------------------------------------------------

    /* Game map - this is effectively the database the holds all game objects.
       They are placed into the map when they are initialized (during construction).
       They remain in the map until the game ends.
     */
    public static final Map<String, GameObject> map = new HashMap<>();

    // ----------------------------------------------------------
    // Game objects in the game tree
    // ----------------------------------------------------------

    /* The root node for all objects in the game tree.
       All rooms should be direct children of this object.
     */
    public static final Container root = new Container("root");

    /*
       The player object.
     */
    public static final Person player = new Person("player");

    /*
        Every object should be somewhere in the game tree. But sometimes
        you don't want to introduce an object into the game world until
        a particular point in the game. The nothing object is directly
        under the root object. It serves as a holder for objects that you
        do not yet want to be visible in your game world.
     */
    private static final Container nothing = new Container("nothing");

    // ------------------------------------------------------
    // Getters & setters
    // ------------------------------------------------------

    public static Light getFlashlight() {
        return flashlight;
    }

    public static void setFlashlight(Light flashlight) {
        World.flashlight = flashlight;
    }

    public static boolean hasPower() {
        return hasPower;
    }

    public static boolean isHaunted() {
        return isHaunted;
    }

    public static void setHaunted(boolean isHaunted) {
        World.isHaunted = isHaunted;
    }

    public static void setPower(boolean hasPower) {
        World.hasPower = hasPower;
        World.currentMessage += hasPower ? " " + Message.hasPower : " " + Message.noPower;
    }

    // ------------------------------------------------------
    // Static methods
    // ------------------------------------------------------

    public static String capitalize(String string) {
        return string.substring(0, 1).toUpperCase() + string.substring(1);
    }

    public static Room getCurrentRoom() {
        GameObject playerParent = player.getParent();
        if (!(playerParent instanceof Room)) {
            throw new IllegalStateException("The parent object of the player must be a room");
        }
        return (Room) playerParent;
    }

    // TODO We may want to evolve this constraint at some point
    // Currently, the player must be moved to a Room.
    // This ensures that the parent of player is always a room.
    // Advanced games may want to remove this constraint.
    public static void movePlayer(Room destination) {
        if (player.getParent() == null) { // this only occurs at the beginning of the game after the player is first constructed
            destination.addChild(player);
        }
        // otherwise, the parent *must* be a room (see getCurrentRoom method)
        Room currentRoom = getCurrentRoom();
        currentRoom.removeChild(player);
        destination.addChild(player);
        currentMessage = "";
    }

    public static void moveItemToPlayer(Item item) {
        if (item.getParent() instanceof Container container) {
            container.removeChild(item);
            World.player.addChild(item);
        } else {
            throw new IllegalStateException("Item not moved; original parent was not a container: " + item.getParent().getName());
        }
    }

    /**
     * Adds object to "nothing" container
     *
     * @param sacrifice GameObject
     */
    public static void feedToTheVoid(GameObject sacrifice) {
        nothing.addChild(sacrifice);
    }

    /**
     * Checks if object belongs to "nothing" container
     * @param go GameObject
     * @return boolean
     */
    public static boolean belongsToTheVoid(GameObject go) {
        return nothing.has(go);
    }

    /**
     * Resets the global state of the game world.
     * <p>
     * This method is particularly useful for unit testing. It clears the world map,
     * resets the player and game tree structure, and clears all global string fields.
     * <p>
     * It should be called before each test when running multiple scenarios within
     * the same test suite to ensure a clean and predictable environment.
     */
    public static void reset() {
        map.clear();
        root.children.clear();
        nothing.children.clear();

        // Reset player and re-add to map
        player.children.clear();
        player.setParent(null);
        map.put("player", player); // ensure player stays in map

        // Reset global variables
        roomName = "";
        roomDescription = "";
        currentMessage = "";
        flashlight = null;
    }

    /**
     * Convert a string into an associated room.
     *
     * @param noun String
     * @return associated Room
     */
    public static Room findRoom(String noun) {
        GameObject object = map.get(noun);
        return object instanceof Room r ? r : null;
    }

    public static boolean flashlightOn() {
        return flashlight != null && flashlight.isOn();
    }

    public static void exit() {
        if (!World.getCurrentRoom().equals(World.map.get("foyer"))) {
            currentMessage = Message.exitCant;
        } else {
            World.getCurrentRoom().goDir("south");
        }
    }
}
