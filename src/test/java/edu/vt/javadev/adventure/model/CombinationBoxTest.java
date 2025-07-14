package edu.vt.javadev.adventure.model;

import edu.vt.javadev.adventure.Message;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class CombinationBoxTest {
    private DarkRoom cellar;
    // Containers:
    private Box breakerBox;
    private CombinationBox jewelryBox;


    // Persons:
    // note: player already declared and initialized in World.player

    // Items:
    private Light flashlight;

    @BeforeEach
    public void setup() {
        // Reset global state before each test
        World.reset();

        // Initialize the game
        new MyGame();

        cellar = (DarkRoom) World.map.get("cellar");


        breakerBox = (Box) World.map.get("breaker-box");
        jewelryBox = (CombinationBox) World.map.get("jewelry-box");


        flashlight = (Light) World.map.get("flashlight");

    }

    @Test
    public void testCombination() {
        try {
            new CombinationBox("b", null);
        } catch (IllegalArgumentException e) {
            assertTrue(true);
        }
        try {
            new CombinationBox("c", "");
        } catch (IllegalArgumentException e) {
            assertTrue(true);
        }

        CombinationBox cb = new CombinationBox("cb", "123");
        assertEquals("123", cb.getCombination());
    }

    @Test
    public void testLock() {
        jewelryBox.lock();
        assertEquals(Message.lockAlready(jewelryBox), World.currentMessage);
        jewelryBox.setLocked(false);
        jewelryBox.setOpen(true);
        jewelryBox.lock();
        assertEquals(Message.lockNotYetClosed(jewelryBox), World.currentMessage);
        jewelryBox.setOpen(false);
        jewelryBox.lock();
        assertEquals(Message.lockOkay(jewelryBox), World.currentMessage);
        assertTrue(jewelryBox.isLocked());
    }

    @Test
    public void testUnlock() {
        jewelryBox.setLocked(false);
        jewelryBox.unlock();
        assertEquals(Message.unlockAlready(jewelryBox), World.currentMessage);

        jewelryBox.setLocked(true);
        CombinationInputProvider mockInput = () -> "1234";
        jewelryBox.setInputProvider(mockInput);
        jewelryBox.unlock();
        assertTrue(jewelryBox.isLocked());
        assertEquals(Message.unlockIncorrectCombination("1234", jewelryBox), World.currentMessage);

        mockInput = () -> "";
        jewelryBox.setInputProvider(mockInput);
        jewelryBox.unlock();
        assertTrue(jewelryBox.isLocked());
        assertEquals(Message.unlockIncorrectCombination("", jewelryBox), World.currentMessage);

        mockInput = () -> jewelryBox.getCombination();
        jewelryBox.setInputProvider(mockInput);
        jewelryBox.unlock();
        assertFalse(jewelryBox.isLocked());
        assertEquals(Message.unlockCombinationOkay(jewelryBox), World.currentMessage);

        try {
            jewelryBox.setInputProvider(null);
        } catch (IllegalArgumentException e) {
            assertTrue(true);
        }
    }

    @Test
    public void testClose() {
        jewelryBox.setOpen(false);
        jewelryBox.setLocked(false);
        jewelryBox.close();
        assertEquals(Message.closeAlready(jewelryBox), World.currentMessage);
        jewelryBox.setOpen(true);
        jewelryBox.close();
        assertTrue(jewelryBox.isLocked());
        assertFalse(jewelryBox.isOpen());
        assertEquals(Message.closeAndLock(jewelryBox), World.currentMessage);
    }

    @Test
    public void stateDescription() {
        jewelryBox.setOpen(true);
        assertEquals("The jewelry-box is open.", jewelryBox.getStateDescription());
        jewelryBox.close();
        assertEquals("The jewelry-box is closed and locked.", jewelryBox.getStateDescription());
        jewelryBox.setLocked(false);
        assertEquals("The jewelry-box is closed but unlocked.", jewelryBox.getStateDescription());
    }
}
