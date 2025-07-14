package edu.vt.javadev.adventure.model;

import edu.vt.javadev.adventure.Message;

public class Switch extends GameObject implements Switchable {

    // ==========================================================
    // Fields
    // ==========================================================

    private boolean isOn;

    // ==========================================================
    // Constructors
    // ==========================================================

    public Switch(String name) {
        super(name);
        isOn = false;
    }

    // ==========================================================
    // Getters & setters
    // ==========================================================

    public void setOn(boolean isOn) {
        this.isOn = isOn;
    }

    @Override
    public boolean isOn() {
        return isOn;
    }

    // ==========================================================
    // Actions
    // ==========================================================

    @Override
    public void turnOn() {
        if (isOn()) World.currentMessage = Message.turnOnAlready(this);
        else {
            isOn = true;
            World.currentMessage = Message.turnOnSuccess(this);
        }
    }

    @Override
    public void turnOff() {
        if (!isOn()) World.currentMessage = Message.turnOffAlready(this);
        else {
            isOn = false;
            World.currentMessage = Message.turnOffSuccess(this);
        }
    }

    // ==========================================================
    // Helpers
    // ==========================================================

    @Override
    public String getStateDescription() {
        return isOn() ? capTheName() + " is on." : capTheName() + " is off.";
    }
}
