package edu.vt.javadev.adventure.model;

import edu.vt.javadev.adventure.Message;

public class RecordPlayer extends Container implements Playable, Switchable {

    // ==========================================================
    // Fields
    // ==========================================================

    private boolean isOn;

    // ==========================================================
    // Constructors
    // ==========================================================

    public RecordPlayer(String name) {
        super(name);
        contentsPrefix = "On " + theName() + " you see";
    }

    // ==========================================================
    // Getters & setters
    // ==========================================================

    public void setOn(boolean on) {
        isOn = on;
    }

    @Override
    public boolean isPlaying() {
        return isOn();
    }

    @Override
    public boolean isOn() {
        if (!World.hasPower()) setOn(false);
        return isOn;
    }

    // ==========================================================
    // Actions
    // ==========================================================

    @Override
    public void examine() {
        if (!World.hasPower()) setOn(false);
        String str = getDescription();
        if (!str.isEmpty()) str += " ";
        str += getStateDescription();
        World.currentMessage = str;
    }

    @Override
    public void play() {
        if (!World.hasPower()) {
            setOn(false);
            World.currentMessage = Message.noPower;
            return;
        }
        Record record = findRecord();
        if (record == null) {
            World.currentMessage = Message.playNoRecord(this);
        } else if (isPlaying()) {
            World.currentMessage = Message.turnOnAlready(this);
        } else {
            World.currentMessage = World.isHaunted() ? Message.playSuccessHaunted(this, record) : Message.playSuccessNotHaunted(this, record);
            setOn(true);
            World.setHaunted(false);
        }
    }

    @Override
    public void turnOn() {
        play();
    }

    @Override
    public void turnOff() {
        if (!isPlaying()) {
            World.currentMessage = Message.turnOffAlready(this);
        } else {
            setOn(false);
            World.currentMessage = Message.turnOffSuccess(this);
        }
    }

    // ==========================================================
    // Helpers
    // ==========================================================

    private Record findRecord() {
        return children.stream()
                .filter(child -> child instanceof Record)
                .map(child -> (Record) child)
                .findFirst()
                .orElse(null);
    }

    @Override
    public String getStateDescription() {
        Record record = findRecord();
        if (record == null) {
            return capTheName() + " is off and has no record.";
        }
        if (isPlaying()) {
            return capTheName() + " is on and playing " + record.getSongName() + ".";
        } else {
            return capTheName() + " is off, but it has a record.";
        }
    }
}
