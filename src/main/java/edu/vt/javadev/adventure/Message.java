package edu.vt.javadev.adventure;

import edu.vt.javadev.adventure.model.*;

public class Message {

    // ============================================
    // GENERAL SYSTEM MESSAGES
    // ============================================

    public static String parserObjectNotFound(String name) {
        return "I don't recognize the game object with the name: " + name;
    }

    public static String parserTooManyWords() {
        return "The game parser supports a maximum of two words.";
    }

    public static String unknownCommand(String verb) {
        return "Unknown command: " + verb;
    }

    public static String unknownCommand(String verb, String noun) {
        return "Unknown command: " + verb + " " + noun;
    }

    public static String objectNotInScope(GameObject object) {
        return "You don't see " + object.theName() + " here.";
    }

    // ============================================
    // INVENTORY MESSAGES
    // ============================================

    public static String inventoryEmpty() {
        return "You are not carrying anything.";
    }

    // ============================================
    // OPEN MESSAGES
    // ============================================

    public static String openCant(GameObject object) {
        return object.capTheName() + " is not something you can open.";
    }

    public static String openAlready(GameObject object) {
        return object.capTheName() + " is already open.";
    }

    public static String openNotYetUnlocked(GameObject object) {
        return "You can't open " + object.theName() + " because it is currently locked.";
    }

    public static String openOkay(GameObject object) {
        return object.capTheName() + " is now open.";
    }

    // ============================================
    // CLOSE MESSAGES
    // ============================================

    public static String closeCant(GameObject object) {
        return object.capTheName() + " is not something you can close.";
    }

    public static String closeAlready(GameObject object) {
        return object.capTheName() + " is already closed.";
    }

    public static String closeOkay(GameObject object) {
        return object.capTheName() + " is now closed.";
    }

    // ============================================
    // LOCK MESSAGES
    // ============================================

    public static String lockCant(GameObject object) {
        return object.capTheName() + " is not something you can lock.";
    }

    public static String lockAlready(GameObject object) {
        return object.capTheName() + " is already locked.";
    }

    public static String lockNotYetClosed(GameObject object) {
        return "You can't lock " + object.theName() + " while it's still open.";
    }

    public static String lockNotYetKey(GameObject object, Item key) {
        return "You need to be carrying " + key.theName() +
                " before you can lock " + object.theName() + ".";
    }

    public static String lockOkay(GameObject object) {
        return object.capTheName() + " is now locked.";
    }

    // ============================================
    // UNLOCK MESSAGES
    // ============================================

    public static String unlockCant(GameObject object) {
        return object.capTheName() + " is not something you can unlock.";
    }

    public static String unlockAlready(GameObject object) {
        return object.capTheName() + " is already unlocked.";
    }

    public static String unlockNotYetKey(GameObject object, Item key) {
        return "You need to be carrying " + key.theName() +
                " before you can unlock " + object.theName() + ".";
    }

    public static String unlockOkay(GameObject object) {
        return object.capTheName() + " is now unlocked.";
    }

    // ============================================
    // TAKE MESSAGES
    // ============================================

    public static String takeCant(GameObject object) {
        return object.capTheName() + " is not something you can take.";
    }

    public static String takeAlready(GameObject object) {
        return "You already have " + object.theName() + ".";
    }

    public static String takeOkay(GameObject object) {
        return object.capTheName() + " is now in your inventory.";
    }

// ============================================
// EXAMINE MESSAGES
// ============================================

    public static String examineNothingSpecial(GameObject object) {
        return "You see nothing unusual about " + object.theName() + ".";
    }

// ============================================
// SEARCH MESSAGES
// ============================================

    public static String searchNothing(GameObject object) {
        return "You search " + object.theName() + ", but you don't find anything interesting.";
    }

}
