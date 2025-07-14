package edu.vt.javadev.adventure.model;

import edu.vt.javadev.adventure.Message;

/**
 * Project 4 CombinationBox class
 *
 * @author Felix Taylor
 */
public class CombinationBox extends Box {

    // ==========================================================
    // Fields
    // ==========================================================

    private String combination;
    private CombinationInputProvider inputProvider;

    // ==========================================================
    // Constructors
    // ==========================================================

    public CombinationBox(String name, String combination) {
        super(name);
        setCombination(combination);
        setInputProvider(new DialogCombinationInput());
    }

    // ==========================================================
    // Getters & setters
    // ==========================================================

    public void setInputProvider(CombinationInputProvider inputProvider) {
        if (inputProvider == null) throw new IllegalArgumentException("CombinationInputProvider cannot be null");
        this.inputProvider = inputProvider;
    }

    public String getCombination() {
        return combination;
    }

    public void setCombination(String combination) {
        if (combination == null || combination.isEmpty()) {
            throw new IllegalArgumentException("Combination cannot be null/empty");
        }
        this.combination = combination;
    }

    // ==========================================================
    // Actions
    // ==========================================================

    @Override
    public void lock() {
        if (isLocked()) {
            World.currentMessage = Message.lockAlready(this);
            return;
        }
        if (isOpen()) {
            World.currentMessage = Message.lockNotYetClosed(this);
            return;
        }
        World.currentMessage = Message.lockOkay(this);
        setLocked(true);
    }

    @Override
    public void unlock() {
        if (!isLocked()) {
            World.currentMessage = Message.unlockAlready(this);
            return;
        }
        // Get combination from user
        String userCombination = inputProvider.getCombinationInput();
        if (!combination.equals(userCombination)) {
            World.currentMessage = Message.unlockIncorrectCombination(userCombination, this);
            return;
        }
        World.currentMessage = Message.unlockCombinationOkay(this);
        setLocked(false);
    }

    @Override
    public void close() {
        if (!isOpen()) {
            World.currentMessage = Message.closeAlready(this);
            return;
        }
        World.currentMessage = Message.closeAndLock(this);
        setOpen(false);
        setLocked(true); // automatically lock upon closing
    }

    // ==========================================================
    // Helpers
    // ==========================================================

    @Override
    public String getStateDescription() {
        if (isOpen()) {
            return capTheName() + " is open.";
        } else if (isLocked()) {
            return capTheName() + " is closed and locked.";
        } else {
            return capTheName() + " is closed but unlocked.";
        }
    }
}
