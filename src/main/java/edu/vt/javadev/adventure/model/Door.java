package edu.vt.javadev.adventure.model;

import edu.vt.javadev.adventure.Message;

public class Door extends GameObject implements Openable, Lockable {

    private boolean isOpen;
    private boolean isLocked;
    /*
     * This is the key that the player must have
     * in their inventory in order to unlock the door.
     */
    private Item withKey;

    public Door(String name) {
        super(name);
    }

    public Door(String name, Item key) {
        super(name);
        withKey = key;
    }

    // Getters for isOpen and isLocked

    @Override
    public boolean isOpen() {
        return isOpen;
    }

    @Override
    public boolean isLocked() {
        return isLocked;
    }

    // Setters for isOpen and isLocked that do not require checks
    // To be used during game initialization

    public void setOpen(boolean open) {
        isOpen = open;
    }

    public void setLocked(boolean locked) {
        isLocked = locked;
    }

    // Setter for withKey

    public void setWithKey(Item key) {
        withKey = key;
    }

    // Open and Close methods

    @Override
    public void open() {
        if (isOpen) {
            World.currentMessage = Message.openAlready(this);
            return;
        }

        if (isLocked) {
            World.currentMessage = Message.openNotYetUnlocked(this);
            return;
        }

        // All check have passed; the door may be opened.
        World.currentMessage = Message.openOkay(this);
        isOpen = true;
    }

    @Override
    public void close() {

        if (!isOpen) {
            World.currentMessage = Message.closeAlready(this);
            return;
        }

        World.currentMessage = Message.closeOkay(this);
        isOpen = false;

    }

    // Lock and Unlock methods

    @Override
    public void lock() {
        if (isLocked) {
            World.currentMessage = Message.lockAlready(this);
            return;
        }

        if (isOpen) {
            World.currentMessage = Message.lockNotYetClosed(this);
            return;
        }

        if (!World.player.children.contains(withKey)) {
            World.currentMessage = Message.lockNotYetKey(this, withKey);
            return;
        }

        // All checks have passed, you may lock the door.
        World.currentMessage = Message.lockOkay(this);
        isLocked = true;
    }

    @Override
    public void unlock() {

        if (!isLocked) {
            World.currentMessage = Message.unlockAlready(this);
            return;
        }

        if (!World.player.children.contains(withKey)) {
            World.currentMessage = Message.unlockNotYetKey(this, withKey);
            return;
        }

        // All checks have passed, you may unlock the door.
        World.currentMessage = Message.unlockOkay(this);
        isLocked = false;
    }

}
