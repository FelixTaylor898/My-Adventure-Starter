package edu.vt.javadev.adventure.model;

public class OpenableDelegate {
    private boolean isOpen = false;
    private final GameObject owner;
    private final Lockable lockable;

    public OpenableDelegate(GameObject owner, Lockable lockable) {
        this.owner = owner;
        this.lockable = lockable; // may be null if the door is openable but not lockable
    }

    public void setOpen(boolean open) {
        isOpen = open;
    }

    public void open() {
        if (isOpen) {
            World.currentMessage = owner.capTheName() + " is already open.";
        } else {
            isOpen = true;
            World.currentMessage = "You open " + owner.theName() + ".";
        }
    }

    public void close() {
        if (!isOpen) {
            World.currentMessage = owner.capTheName() + " is already closed.";
        } else {
            isOpen = false;
            World.currentMessage = "You close " + owner.theName() + ".";
        }
    }

    public boolean isOpen() {
        return isOpen;
    }
}

