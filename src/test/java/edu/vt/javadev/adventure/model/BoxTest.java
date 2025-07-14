package edu.vt.javadev.adventure.model;

import edu.vt.javadev.adventure.Message;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class BoxTest {

    Box box;
    Item goldRing;
    Item boxKey;
    Box shoebox;

    // Containers:
    private Box breakerBox;

    @BeforeEach
    public void setup() {
        // Reset global state before each test
        World.reset();

        // Initialize the game
        new MyGame();


        breakerBox = (Box) World.map.get("breaker-box");

        
        box = new Box("box");
        box.setDescription("A small, ornate jewelry box.");
        box.setOpen(true);  // open and unlocked
        box.setLocked(false);

        goldRing = new Item("gold-ring");
        box.addChild(goldRing);

        boxKey = new Item("box-key");
        box.setWithKey(boxKey);

        shoebox = new Box("shoebox");
        shoebox.setOpen(false);
        shoebox.setLocked(false);
    }

    @Test
    public void testExamineWithAllDescriptions() {
        box.examine();
        String msg = World.currentMessage;

        assertTrue(msg.contains(box.getDescription()));
        assertTrue(msg.contains(box.getStateDescription()));
        assertTrue(msg.contains(box.getContentsDescription()));
    }

    @Test
    public void testExamineWithOnlyStateDescription() {
        box.setDescription("");
        box.examine();
        String msg = World.currentMessage;

        assertFalse(msg.contains("A small, ornate jewelry box."));
        assertTrue(msg.contains(box.getStateDescription()));
        assertTrue(msg.contains(box.getContentsDescription()));
    }

    @Test
    public void testExamineClosedSuppressesContents() {
        box.setOpen(false);
        box.examine();
        String msg = World.currentMessage;

        assertTrue(msg.contains(box.getDescription()));
        assertTrue(msg.contains(box.getStateDescription()));
        assertFalse(msg.contains("gold-ring"));
    }

    @Test
    public void testOpenMessageIncludesContents() {
        box.setOpen(false);  // Must be closed to open
        box.setLocked(false);
        box.open();
        String msg = World.currentMessage;

        assertTrue(msg.contains(Message.openOkay(box)));
        assertTrue(msg.contains(box.getContentsDescription()));
    }

    @Test
    public void testOpenAlreadyMessage() {
        box.setOpen(true);
        box.open();
        assertEquals(Message.openAlready(box), World.currentMessage);
    }

    @Test
    public void testLockWhileOpen() {
        World.player.addChild(boxKey);
        box.setOpen(true);
        box.setLocked(false);
        box.lock();
        assertEquals(Message.lockNotYetClosed(box), World.currentMessage);
    }



    @Test
    public void testSearchMessageShowsContents() {
        box.setOpen(true);
        box.search();
        assertEquals(box.getContentsDescription(), World.currentMessage);
    }

    @Test
    public void testLock() {
        box.setLocked(true);
        box.lock();
        assertEquals(Message.lockAlready(box), World.currentMessage);
        box.setLocked(false);
        box.lock();
        assertFalse(box.isLocked());
        assertEquals(Message.lockNotYetClosed(box), World.currentMessage);
        box.setOpen(false);
        box.lock();
        assertFalse(box.isLocked());
        assertEquals(Message.lockNotYetKey(box, boxKey), World.currentMessage);
        World.player.addChild(boxKey);
        box.lock();
        assertTrue(box.isLocked());
        assertEquals(Message.lockOkay(box), World.currentMessage);
        shoebox.lock();
        assertFalse(shoebox.isLocked());
        assertEquals(Message.lockCant(shoebox), World.currentMessage);
    }

    @Test
    public void testUnlock() {
        shoebox.unlock();
        assertEquals(Message.unlockCant(shoebox), World.currentMessage);
        box.setLocked(false);
        box.unlock();
        assertEquals(Message.unlockAlready(box), World.currentMessage);
        box.setLocked(true);
        box.unlock();
        assertTrue(box.isLocked());
        assertEquals(Message.unlockNotYetKey(box, boxKey), World.currentMessage);
        World.player.addChild(boxKey);
        box.unlock();
        assertFalse(box.isLocked());
        assertEquals(Message.unlockOkay(box), World.currentMessage);
    }

    @Test
    public void testOpen() {
        box.open();
        assertEquals(Message.openAlready(box), World.currentMessage);
        box.close();
        box.setLocked(true);
        box.open();
        assertFalse(box.isOpen());
        assertEquals(Message.openNotYetUnlocked(box), World.currentMessage);
        box.setLocked(false);
        box.open();
        assertTrue(box.isOpen());
        assertEquals(Message.openOkay(box) + " " + box.getContentsDescription(), World.currentMessage);
    }

    @Test
    public void testSearchEmptyBox() {
        shoebox.setOpen(true);
        shoebox.search();
        assertEquals(Message.searchNothing(shoebox), World.currentMessage);
    }

    @Test
    public void testSearchLocked() {
        box.setLocked(true);
        box.search();
        assertEquals(Message.searchCantLocked(box), World.currentMessage);
    }

    @Test
    public void testStateDescription() {
        assertEquals("The box is open.", box.getStateDescription());
        box.close();
        assertEquals("The box is closed but unlocked.", box.getStateDescription());
        box.setLocked(true);
        assertEquals("The box is closed and locked.", box.getStateDescription());
        assertEquals("The shoebox is closed.", shoebox.getStateDescription());
    }

    @Test
    public void testWithKeyConstructor() {
        Box box2 = new Box("box-two", boxKey);
        assertEquals(boxKey, box.getWithKey());
    }

    @Test
    public void testOpenNoContents() {
        shoebox.open();
        shoebox.examine();
        assertEquals(shoebox.getStateDescription(), World.currentMessage);
        shoebox.setDescription("a shoebox");
        shoebox.examine();
        assertEquals(shoebox.getDescription() + " " + shoebox.getStateDescription(), World.currentMessage);
    }

    @Test
    public void testClosedAlready() {
        box.setOpen(false);
        box.close();
        assertEquals(Message.closeAlready(box), World.currentMessage);
    }

    @Test
    public void testClosedUnlockedState() {
        box.setOpen(false);
        box.setLocked(false);
        assertEquals("The box is closed but unlocked.", box.getStateDescription());
    }
}