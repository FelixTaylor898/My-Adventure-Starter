package edu.vt.javadev.adventure.model;

import edu.vt.javadev.adventure.Message;

import java.util.ArrayList;
import java.util.List;

public class Person extends Container {

    // ==========================================================
    // Fields
    // ==========================================================

    private String alias;

    // ==========================================================
    // Constructors
    // ==========================================================

    public Person(String name) {
        super(name);
        contentsPrefix = "You are carrying";
        contentsSuffix = ".";
    }

    // ==========================================================
    // Getters & setters
    // ==========================================================

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    // ==========================================================
    // Actions
    // ==========================================================

    public void inventory() {
        if (isEmpty()) World.currentMessage = Message.inventoryEmpty;
        else World.currentMessage = getContentsDescription();
    }

    @Override
    public void search() {
        inventory();
    }

    @Override
    public void examine() {
        String yourName = alias != null ? Message.yourName(alias) : "";
        String desc = getDescription();
        String contents = getContentsDescription();
        List<String> parts = new ArrayList<>();
        if (!yourName.isEmpty()) parts.add(yourName);
        if (desc != null && !desc.isEmpty()) parts.add(desc);
        if (!contents.isEmpty()) parts.add(contents);
        String str = String.join(" ", parts);
        World.currentMessage = str.isEmpty() ? Message.examineNothingSpecial(this) : str;
    }
}