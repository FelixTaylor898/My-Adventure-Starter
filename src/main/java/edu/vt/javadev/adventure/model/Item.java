package edu.vt.javadev.adventure.model;

import edu.vt.javadev.adventure.Message;

/**
 * Project 4 Item class.
 * @author Felix Taylor
 */
public class Item extends GameObject implements Takeable {

    // ==========================================================
    // Constructors
    // ==========================================================

    public Item(String name) {
        super(name);
    }

    // ==========================================================
    // Actions
    // ==========================================================

    public void take() {
        if (World.player.has(this)) {
            World.currentMessage = Message.takeAlready(this);
            return;
        }
        if (getParent() == null || World.belongsToTheVoid(this)) {
            World.currentMessage = Message.takeCant(this);
            return;
        }
        World.currentMessage = Message.takeOkay(this);
        World.moveItemToPlayer(this);
    }
}