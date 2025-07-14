package edu.vt.javadev.adventure.model;

import edu.vt.javadev.adventure.Message;

public class Lever extends Switch implements Pullable {

    // ==========================================================
    // Constructors
    // ==========================================================

    public Lever(String name) {
        super(name);
    }

    // ==========================================================
    // Actions
    // ==========================================================

    @Override
    public void turnOn() {
        boolean oState = isOn();
        super.turnOn();
        // updates world's power
        powerChange(oState, isOn());
    }

    @Override
    public void turnOff() {
        boolean oState = isOn();
        super.turnOff();
        // updates world's power
        powerChange(oState, isOn());
    }

    @Override
    public void pull() {
        if (isOn()) turnOff();
        else turnOn();
    }

    // ==========================================================
    // Helpers
    // ==========================================================

    @Override
    public String getStateDescription() {
        return super.getStateDescription() + " " + (isOn() ? Message.hasPower : Message.noPower);
    }

    // Updates the world's power state.
    private void powerChange(boolean oState, boolean nState) {
        if (oState != nState) World.setPower(nState);
    }
}
