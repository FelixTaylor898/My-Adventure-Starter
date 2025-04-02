package edu.vt.javadev.adventure.model;

import edu.vt.javadev.adventure.Message;

public class LockableDelegate {
    private boolean isLocked = false;
    private Item withKey;
    private final GameObject owner;
    private final Openable openable; // this can be null if not openable

    public LockableDelegate(GameObject owner, Item key, Openable openable) {
        this.owner = owner;
        this.withKey = key;
        this.openable = openable;
    }

    public void setLocked(boolean locked) {
        isLocked = locked;
    }

    public void lock() {
        if (isLocked) {
            World.currentMessage = Message.lockAlready(owner);
            return;
        }

        if (openable.isOpen()) {
            World.currentMessage = Message.lockNotYetClosed(owner);
        }

        if (!World.player.children.contains(getKey())) {
                World.currentMessage = Message.lockNotYetClosed(owner);
                return;
            }
            // if we make it here, this object is unlocked and the player has the key to lock it.
            isLocked = true;
            World.currentMessage = "You lock " + owner.theName() + ".";
    }

    public void unlock() {
        if (!isLocked) {
            World.currentMessage = owner.capTheName() + " is already unlocked.";
        } else {
            isLocked = false;
            World.currentMessage = "You unlock " + owner.theName() + ".";
        }
    }

    public boolean isLocked() {
        return isLocked;
    }

    public boolean requiresKey() {
        return withKey != null;
    }

    public Item getKey() {
        return withKey;
    }

    public void setWithKey(Item key) {
        this.withKey = key;
    }
}
