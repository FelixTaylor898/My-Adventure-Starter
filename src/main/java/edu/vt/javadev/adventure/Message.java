package edu.vt.javadev.adventure;

import edu.vt.javadev.adventure.model.*;
import edu.vt.javadev.adventure.model.Record;

import java.util.Random;

public class Message {

    // ============================================
    // GENERAL SYSTEM MESSAGES
    // ============================================

    public static String scopeNotFound(String noun) {
        return "You don't see any " + noun + " here.";
    }

    public static String yourName(String name) {
        return "Your name is " + name + ".";
    }

    public static String parserTooManyWords = "The game parser supports a maximum of two words.";

    public static String unknownCommand(String verb) {
        return "Unknown command: " + verb;
    }

    public static String unknownCommand(String verb, String noun) {
        return "Unknown command: " + verb + " " + noun;
    }

    // ============================================
    // ROOM & DIRECTION MESSAGES
    // ============================================

    public static String exitCant = "You can't exit the house from here.";

    public static String notARoom(String gameObject) {
        return gameObject + " is not a location.";
    }

    public static String cantGoFromHere(Room gameObject) {
        return "You can't go to " + gameObject.theName() + " from here.";
    }

    public static String alreadyInRoom(Room gameObject) {
        return "You are already in " + gameObject.theName() + ".";
    }

    public static String cantGoDirection(String direction) {
        return "You can't go " + direction + " from here.";
    }

    // ============================================
    // INVENTORY MESSAGES
    // ============================================

    public static String inventoryEmpty = "You are not carrying anything.";

    public static String notInInventory(Item item) {
        return item.capTheName() + " is not in your inventory.";
    }


    // ============================================
    // POWER MESSAGES
    // ============================================

    public static String noPower = "The house has no power.";
    public static String hasPower = "The house now has power.";

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
        return "You open " + object.theName() + ".";
    }

    public static String unlockIncorrectCombination(String combination, CombinationBox combinationBox) {
        return combination + " is not the correct combination for " + combinationBox.theName() + ".";
    }

    // ============================================
    // CLOSE MESSAGES
    // ============================================

    public static String closeAndLock(GameObject object) {
        return object.capTheName() + " has been closed and locked.";
    }

    public static String closeCant(GameObject object) {
        return object.capTheName() + " is not something you can close.";
    }

    public static String closeAlready(GameObject object) {
        return object.capTheName() + " is already closed.";
    }

    public static String closeOkay(GameObject object) {
        return "You close " + object.theName() + ".";
    }

    public static String doorClosed(Door door) {
        return door.capTheName() + " is closed.";
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
        return "You lock " + object.theName() + ".";
    }

    // ============================================
    // UNLOCK MESSAGES
    // ============================================

    public static String unlockCombinationOkay(CombinationBox combinationBox) {
        return "You have successfully entered the combination. " + unlockOkay(combinationBox);
    }

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
        return "You unlock " + object.theName() + ".";
    }

    // ============================================
    // TURN ON MESSAGES
    // ============================================

    public static String turnOnCant(GameObject gameObject) {
        return gameObject.capTheName() + " is not something you can turn on.";
    }

    public static String turnOnAlready(GameObject object) { return object.capTheName() + " is already on."; }

    public static String turnOnSuccess(GameObject object) {
        return "You turn on " + object.theName() + ".";
    }

    // ============================================
    // TURN OFF MESSAGES
    // ============================================

    public static String turnOffCant(GameObject gameObject) {
        return gameObject.capTheName() + " is not something you can turn off.";
    }

    public static String turnOffAlready(GameObject light) { return light.capTheName() + " is already off."; }

    public static String turnOffSuccess(GameObject light) {
        return "You turn off " + light.theName() + "."; }


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

    public static String searchCantLocked(Box box) {
        return "You can't search " + box.theName() + " while it is locked.";
    }

    // ============================================
    // PLAY MESSAGES
    // ============================================

    public static String playCant(GameObject gameObject) {
        return gameObject.capTheName() + " is not something you can play.";
    }

    public static String playMustPlace(Record record) {
        return "You must place " + record.theName() + " on a player before you can play it.";
    }

    public static String playNoRecord(RecordPlayer player) {
        return "You cannot play " + player.theName() + ", it has no record.";
    }

    public static String playSuccessHaunted(RecordPlayer player, Record record) {
        return "You turn on " + player.theName() + ". " + record.getSongName() + " plays through the old speakers, filling the house with a much-needed calmness. The air becomes warmer, and dawn emerges through the windows. Eleanor sings along to the gentle lyrics. As the song comes to a close, her spirit passes on.";
    }

    public static String playSuccessNotHaunted(RecordPlayer player, Record record) {
        return player.capTheName() + " is now playing " + record.getSongName() + ".";
    }

    // ============================================
    // DARKNESS MESSAGES
    // ============================================

    public static String roomTooDark(Room room) {
        return "It's too dark to see anything in " + room.theName() + ".";
    }

    // ============================================
    // READ MESSAGES
    // ============================================

    public static String readNoLight(Book book) {
        return "There isn't enough light to read " + book.theName() + ".";
    }

    public static String readCant(GameObject gameObject) {
        return gameObject.capTheName() + " is not something you can read.";
    }

    // ============================================
    // PLACE MESSAGES
    // ============================================

    public static String placeCant(GameObject gameObject) {
        return gameObject.capTheName() + " is not something you can place.";
    }

    public static String placeTooFar(GameObject object) {
        return "There is nowhere to place " + object.theName();
    }

    public static String placeSuccess(GameObject record, GameObject player) {
        return "You place " + record.theName() + " on " + player.theName() + ".";
    }

    // ============================================
    // PULL MESSAGES
    // ============================================

    public static String pullCant(GameObject gameObject) {
        return gameObject.capTheName() + " is not something you can pull.";
    }

    // ============================================
    // GHOST MESSAGES
    // ============================================

    public static String hauntedDoorMessage = "You must free the ghost of Eleanor before you can leave.";

    public static String randomGhostMessage() {
        Random rand = new Random();
        int index = rand.nextInt(ghostMessages.length);
        return ghostMessages[index];
    }

    private static final String[] ghostMessages = new String[]{
            "The ghost whispers, \"Go away.\"",
            "You hear a distant scream.",
            "The ghost of Eleanor sobs.",
            "You hear tapping on the windows.",
            "The ghost of Eleanor whispers, \"Leave.\"",
            "You hear slow, labored breathing.",
            "You hear scratching on the walls.",
            "You hear a soft hum.",
            "You hear Eleanor cry, \"No!\"",
            "Eleanor whispers your name.",
            "The curtains move ominously.",
            "Eleanor murmurs, \"Please.\"",
            "Lightning strikes outside.",
            "You hear a distant wail.",
            "You hear a quiet laugh.",
            "The ghost cries, \"Help me.\"",
            "The ghost says, \"It's so cold.\"",
            "You hear a sigh.",
            "You hear footsteps behind you.",
            "You feel a hand on your shoulder.",
            "You hear a cry of pain.",
            "The furniture shakes.",
            "A shadow moves across the wall.",
            "You hear faint crying.",
            "The temperature drops suddenly.",
            "You feel someone watching you.",
            "You hear whispers in the dark.",
            "You hear faint scratching under the floorboards.",
            "The wind carries a voice calling your name.",
            "You feel an icy breeze.",
            "The walls groan as if in pain.",
            "You hear chains dragging across the floor.",
            "You feel cold fingers brush your neck.",
            "Eleanor's laughter echoes.",
            "A cat yowls in the distance.",
            "You hear a distant groan.",
            "You feel cold fingers brush your neck.",
            "The floorboards creak as if someone is walking.",
            "You hear breathing in the dark, but no one is there.",
            "The walls seem to close in around you.",
            "You feel a cold hand brushing your face.",
            "The wind howls through the cracks in the walls.",
            "You hear the faint sound of a lullaby.",
            "A nearby door rattles on its hinges.",
            "You feel someone standing just behind you.",
            "A chair moves by itself, scraping across the floor.",
            "You hear faint whispers.",
            "The curtains sway as if pushed by unseen hands.",
            "The curtains move ominously.",
            "For a moment, you see blood trickle down the walls.",
            "You hear something shatter.",
            "You hear a voice say, \"It's not safe here.\"",
            "A figure watches you from a window.",
            "You hear pipes rattle loudly.",
            "You hear a demonic growl.",
            "A bird shrieks in the distance.",
            "A spider scuttles across the floor.",
            "A rat sprints across the floor.",
            "Something suddenly falls off a wall."
    };
}