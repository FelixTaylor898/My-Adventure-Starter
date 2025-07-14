package edu.vt.javadev.adventure.model;
import edu.vt.javadev.adventure.Message;

/**
 * Project 4 Book class.
 * @author Felix Taylor
 */
public class Book extends Item implements Readable {

    // ==========================================================
    // Fields
    // ==========================================================

    private String readText; // text when you read the book

    // ==========================================================
    // Constructors
    // ==========================================================

    public Book(String name, String readText) {
        super(name);
        setReadText(readText);
    }

    // ==========================================================
    // Getters & setters
    // ==========================================================

    public String getReadText() {
        return readText;
    }

    public void setReadText(String readText) {
        if (readText == null || readText.isEmpty()) throw new IllegalArgumentException("readText cannot be null/empty");
        this.readText = readText;
    }

    // ==========================================================
    // Actions
    // ==========================================================

    @Override
    public void read() {
        if (!World.flashlightOn()) { // light must be on
            World.currentMessage = Message.readNoLight(this);
            return;
        }
        // reading will flick the flashlight off
        // so cellar will be dark
        // upon initial entry
        World.getFlashlight().turnOff();
        World.currentMessage = readText;
    }

    @Override
    public void search() {
        read();
    }
}
