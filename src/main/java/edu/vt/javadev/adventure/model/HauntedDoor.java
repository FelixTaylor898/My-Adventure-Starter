package edu.vt.javadev.adventure.model;

import edu.vt.javadev.adventure.Message;

/**
 * Project 4 HauntedDoor class.
 *
 * @author Felix Taylor
 */
public class HauntedDoor extends Door {

    // ==========================================================
    // Constructors
    // ==========================================================

    public HauntedDoor(String name) {
        super(name);
    }

    // ==========================================================
    // Getters & setters
    // ==========================================================

    /**
     * Returns description, plus a warning if the house is still haunted.
     *
     * @return String
     */
    @Override
    public String getDescription() {
        String description = super.getDescription();
        if (!description.isEmpty()) description += " ";
        if (isHaunted()) description += Message.hauntedDoorMessage;
        return description;
    }

    // ==========================================================
    // Actions
    // ==========================================================

    /**
     * Opens the door only if the house isn't haunted.
     */
    @Override
    public void open() {
        if (World.isHaunted()) {
            World.currentMessage = Message.hauntedDoorMessage;
        } else super.open();
    }

    // ==========================================================
    // Helpers
    // ==========================================================

    private boolean isHaunted() {
        return World.isHaunted();
    }
}