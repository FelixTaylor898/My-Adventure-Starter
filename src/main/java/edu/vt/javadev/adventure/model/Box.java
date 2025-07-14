package edu.vt.javadev.adventure.model;

import edu.vt.javadev.adventure.Message;

import java.util.ArrayList;
import java.util.List;

/**
 * Project 4 Box class.
 * @author Felix Taylor
 */

public class Box extends Container implements Lockable, Openable {

    // ==========================================================
    // Fields
    // ==========================================================

    private boolean isOpen;
    private boolean isLocked;
    private Item withKey;

    // ==========================================================
    // Constructors
    // ==========================================================

    public Box(String name) {
        super(name);
    }

    public Box(String name, Item key) {
        super(name);
        withKey = key;
    }

    // ==========================================================
    // Getters & setters
    // ==========================================================

    public void setWithKey(Item withKey) {
        this.withKey = withKey;
    }

    @Override
    public boolean isLocked() {
        return isLocked;
    }

    public void setOpen(boolean setOpen) {
        this.isOpen = setOpen;
    }

    public void setLocked(boolean setLocked) {
        this.isLocked = setLocked;
    }

    @Override
    public boolean isOpen() {
        return isOpen;
    }

    public Item getWithKey() {
        return withKey;
    }

    // ==========================================================
    // Actions
    // ==========================================================

    @Override
    public void lock() {
        if (isLocked) {
            World.currentMessage = Message.lockAlready(this);
            return;
        }
        if (noKey()) {
            World.currentMessage = Message.lockCant(this);
            return;
        }
        if (isOpen) {
            World.currentMessage = Message.lockNotYetClosed(this);
            return;
        }
        if (!World.player.has(withKey)) {
            World.currentMessage = Message.lockNotYetKey(this, withKey);
            return;
        }
        World.currentMessage = Message.lockOkay(this);
        isLocked = true;
    }

    @Override
    public void unlock() {
        if (noKey()) {
            World.currentMessage = Message.unlockCant(this);
            return;
        }

        if (!isLocked) {
            World.currentMessage = Message.unlockAlready(this);
            return;
        }

        if (!World.player.has(withKey)) {
            World.currentMessage = Message.unlockNotYetKey(this, withKey);
            return;
        }

        // All checks have passed, you may unlock the door.
        World.currentMessage = Message.unlockOkay(this);
        isLocked = false;
    }

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
        isOpen = true;
        World.currentMessage = Message.openOkay(this) + (isEmpty() ? "" : " " + getContentsDescription());
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

    @Override
    public void examine() {
        String str = getDescription();
        if (!str.isEmpty()) {
            str += " ";
        }
        str += getStateDescription();
        if (isOpen() && !isEmpty()) {
            str += " " + getContentsDescription();
        }
        World.currentMessage = str;
    }

    @Override
    public void search() {
        if (isLocked()) {
            World.currentMessage = Message.searchCantLocked(this);
            return;
        }
        open();
        super.search();
    }

    // ==========================================================
    // Helpers
    // ==========================================================

    private boolean noKey() {
        return withKey == null;
    }

    @Override
    public String getStateDescription() {
        if (isOpen()) {
            return capTheName() + " is open.";
        } if (noKey()) {
            return capTheName() + " is closed.";
        } else if (isLocked()) {
            return capTheName() + " is closed and locked.";
        } else return capTheName() + " is closed but unlocked.";
    }

    /**
     * Return box's contents only if open.
     * @return List of box's contents
     */
    @Override
    public List<GameObject> visibleContents() {
        return isOpen() ? children : new ArrayList<>();
    }
}