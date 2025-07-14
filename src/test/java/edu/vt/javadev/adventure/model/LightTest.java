package edu.vt.javadev.adventure.model;

import edu.vt.javadev.adventure.Message;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class LightTest {
    private Room outside;
    private Room driveway;
    private Room garage;
    private Room kitchen;
    private Room house;
    private Door frontDoor;
    private Door sideDoor;
    private Container flowerPot;
    private Container shelves;
    private Container tools;
    private Box shoebox;
    private Item key;
    private Item screwdriver;
    private Item lightBulb;
    private Person player;

    @BeforeEach
    public void setup() {
        // Reset global state before each test
        World.reset();

        // Initialize the game
        new MyGame();

        player = World.player;

        // Retrieve references to relevant objects
        outside = (Room) World.map.get("outside-house");
        kitchen = (Room) World.map.get("kitchen");
        driveway = (Room) World.map.get("driveway");
        garage = (Room) World.map.get("garage");
        house = (Room) World.map.get("house");

        frontDoor = (Door) World.map.get("front-door");
        sideDoor = (Door) World.map.get("side-door");
        flowerPot = (Container) World.map.get("flower-pot");
        shelves = (Container) World.map.get("shelves");
        tools = (Container) World.map.get("tools");
        shoebox = (Box) World.map.get("shoebox");
        key = (Item) World.map.get("key");
        screwdriver = (Item) World.map.get("screwdriver");
        lightBulb = (Item) World.map.get("light-bulb");
    }



    @Test
    public void testTurnOn() {

    }

    @Test
    public void testTurnOff() {

    }

}
