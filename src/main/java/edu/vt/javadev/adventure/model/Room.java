package edu.vt.javadev.adventure.model;

import edu.vt.javadev.adventure.Message;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Room extends Container {

    private final Map<String, Room> roomMap;
    private final Map<String, Door> doorMap;

    public Room(String name) {
        super(name);

        roomMap = new HashMap<>();
        doorMap = new HashMap<>();

        contentsPrefix = "You see";
        contentsSuffix = "here.";
    }

    // ==========================================================
    // Getters & Setters
    // ==========================================================

    public void setRoomDir(String direction, Room room) {
        if (direction == null || direction.isEmpty() || room == null) throw new IllegalArgumentException();
        roomMap.put(direction, room);
    }

    public void setDoorDir(String direction, Door door) {
        if (direction == null || direction.isEmpty() || door == null) throw new IllegalArgumentException();
        doorMap.put(direction, door);
        addChild(door);
    }

    // ==========================================================
    // Actions
    // ==========================================================

    public void look() {
        World.roomName = toTitleCase(getName());
        World.roomDescription = roomDescription();
    }

    public void enter(String noun) {
        Room destination = World.findRoom(noun);
        if (destination == null) {
            World.currentMessage = Message.notARoom(noun);
        } else if (destination.equals(World.getCurrentRoom())) {
            World.currentMessage = Message.alreadyInRoom(destination);
        }  else {
            String direction = World.getCurrentRoom().findDirection(destination);
            if (direction == null) {
                World.currentMessage = Message.cantGoFromHere(destination);
            } else {
                World.getCurrentRoom().goDir(direction);
            }
        }
    }

    // ==========================================================
    // Helpers
    // ==========================================================

    public String roomDescription() {
        return "<p>" +
                getDescription() +
                "</p>" +
                "<p>" +
                getContentsDescription() +
                "</p>";
    }


    /*
     * Assumes that the specified string is in kebab case, meaning that the letters
     * are all lower case and the words are separated by dashes.
     * Kebob case is typically used for slugs. A slug is a short name for a project or title
     * that is often used in a URL.
     * Slugs are meant to be easily readable by humans, search engines, and web browsers.
     */
    private String toTitleCase(String slug) {
        String[] words = slug.split("-");
        StringBuilder sb = new StringBuilder();

        for (String word : words) {
            sb.append(Character.toUpperCase(word.charAt(0)))   // Capitalize first letter
                    .append(word.substring(1))       // Append the rest of the word
                    .append(" ");                              // Add a space after each word
        }
        return sb.toString().trim(); // Remove the space after the last word
    }

    public void goDir(String direction) {
        if (!roomMap.containsKey(direction)) {
            World.currentMessage = Message.cantGoDirection(direction);
            return;
        }
        Room destRoom = roomMap.get(direction);
        Door destDoor = doorMap.get(direction);
        if (destDoor != null && !destDoor.isOpen()) {
            World.currentMessage = destDoor.capTheName() + " is closed.";
            return;
        }
        World.movePlayer(destRoom);
    }

    private String findDirection(Room room) {
        for (Map.Entry<String, Room> entry : roomMap.entrySet()) {
            if (entry.getValue().equals(room)) {
                return entry.getKey();
            }
        }
        return null;
    }

    public boolean equals(Room other) {
        return this.getName().equals(other.getName());
    }
}
