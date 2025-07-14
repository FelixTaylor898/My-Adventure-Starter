package edu.vt.javadev.adventure.model;

import edu.vt.javadev.adventure.Message;

import java.util.ArrayList;
import java.util.List;

/**
 * Project 4 DarkRoom class.
 *
 * @author Felix Taylor
 */
public class DarkRoom extends Room {

    // ==========================================================
    // Constructors
    // ==========================================================

    public DarkRoom(String name) {
        super(name);
    }

    // ==========================================================
    // Helpers
    // ==========================================================

    /**
     * Checks if the room is too dark (flashlight is off)
     *
     * @return True if room is too dark
     */
    public boolean isTooDark() {
        return !World.flashlightOn();
    }

    /**
     * Will only return description if room is lit.
     *
     * @return String
     */
    @Override
    public String getDescription() {
        if (isTooDark()) return Message.roomTooDark(this);
        return super.getDescription();
    }

    /**
     * Returns room's contents if room is lit
     * else only returns doors and current player.
     *
     * @return List
     */
    @Override
    public List<GameObject> visibleContents() {
        if (!isTooDark()) return children;
        return new ArrayList<>(children.stream()
                .filter(o -> o instanceof Person || o instanceof Door)
                .toList());
    }
}