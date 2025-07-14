package edu.vt.javadev.adventure.model;

import edu.vt.javadev.adventure.Message;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class BookTest {

    // Items:
    private Light flashlight;
    private Book diary;

    @BeforeEach
    public void setup() {
        // Reset global state before each test
        World.reset();

        // Initialize the game
        new MyGame();



        flashlight = (Light) World.map.get("flashlight");
        diary = (Book) World.map.get("diary");
    }

    @Test
    public void testReadText() {
        try {
            new Book("book1", null);
        } catch (IllegalArgumentException e) {
            assertTrue(true);
        }
        try {
            new Book("book2", "");
        } catch (IllegalArgumentException e) {
            assertTrue(true);
        }
        Book book = new Book("book", "read text");
        assertEquals("read text", book.getReadText());
    }

    @Test
    public void testRead() {
        // too dark to read
        diary.read();
        assertEquals(Message.readNoLight(diary), World.currentMessage);

        // enough light to read
        flashlight.setOn(true);
        diary.read();
        assertEquals(diary.getReadText(), World.currentMessage);
    }

    @Test
    public void testSearch() {
        // too dark to read
        diary.search();
        assertEquals(Message.readNoLight(diary), World.currentMessage);

        // enough light to read
        flashlight.setOn(true);
        diary.search();
        assertEquals(diary.getReadText(), World.currentMessage);
    }

    @Test
    public void testInvalidText() {
        try {
            new Book("a", null);
        } catch (IllegalArgumentException e) {
            assertTrue(true);
        }
        try {
            new Book("b", "");
        } catch (IllegalArgumentException e) {
            assertTrue(true);
        }
    }

}
