package edu.vt.javadev.adventure.model;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * The World class holds global variables and methods that can be used by any object in the game.
 *
 * Global variables:
 * - map: the world map is a mapping from strings to game objects. The string are the unique names
 *        of the game objects and stored in the name field of the game object.
 * - player: the player character in the adventure game (the avatar of the person playing the game)
 * - root: the root game object in the game tree. This object does not represent any object in the game world.
 *          It is a container, and it directly contains all rooms in the game.
 * - startRoom: this is the room in which the player begins the game. [TODO Do we really need this ??]
 * - roomName: this is a string containing the room name to be printed after each turn. This will typically be
 *              the name of the room that the player resides in during that turn
 * - roomDescription: this is the description of the room that the player currently resides in. The room
 *                     description contains a short description of the room, and a (possibly recursive) list of
 *                     all pertinent game objects in that room.
 * - currentMessage: This is the current message that is given at the end of each turn based on the action
 *                   the player takes. The roomName, roomDescription, and currentMessage are printed in that order.
 *                   The roomName is printed in a Label object, the roomDescription is printed in a WebView,
 *                   and the currentMessage is printed in a Label object.
 */
public class World {

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

    /*
        The starting room. This will be declared here, but it will be initialized
        in the constructor of the MyGame class, which is the class that creates
        the entire game tree structure. [TODO Do we really need this as a global ??]
     */
    public static Room startRoom;

    // TODO Figure out how to give the player a proper name.
    public static final Person player = new Person("player");

    /*
        Every object should be somewhere in the game tree. But sometimes
        you don't want to introduce an object into the game world until
        a particular point in the game. The nothing object is directly
        under the root object. It serves as a holder for objects that you
        do not yet want to be visible in your game world. [TODO Do we really need this as a global ??]
     */
    public static Container nothing = new Container("nothing");

    // ------------------------------------------------------
    // Static methods
    // ------------------------------------------------------

    public static void getStartRoom(Room room) {
        startRoom = room;
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
        startRoom = null;
    }

}
