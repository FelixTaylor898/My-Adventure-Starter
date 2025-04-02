package edu.vt.javadev.adventure.model;

public class Person extends Container {

    public Person(String name) {
        super(name);
        contentsPrefix = "You are carrying";
        contentsSuffix = ".";
    }

    public void inventory() {
        World.currentMessage = getContentsDescription();
    }
}

