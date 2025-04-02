package edu.vt.javadev.adventure.model;

import java.util.HashMap;
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
        roomMap.put(direction, room);
    }
    public void setDoorDir(String direction, Door door) {
        doorMap.put(direction, door);
    }
    // ==========================================================
    // Actions
    // ==========================================================

    public void look() {
        World.roomName = toTitleCase(getName());
        World.roomDescription = roomDescription();
    }

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
        if (!roomMap.containsKey(direction) || roomMap.get(direction) == null) {
            World.currentMessage = "You can't go " + direction + " from here.";
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
}
