package edu.vt.javadev.adventure.model;

import edu.vt.javadev.adventure.Message;

public class Item extends GameObject {

    public Item(String name) {
        super(name);
    }

    public void take() {
        if (World.player.children.contains(this)) {
            World.currentMessage = Message.takeAlready(this);
            return;
        }
        World.currentMessage = Message.takeOkay(this);
        World.moveItemToPlayer(this);
    }
}

