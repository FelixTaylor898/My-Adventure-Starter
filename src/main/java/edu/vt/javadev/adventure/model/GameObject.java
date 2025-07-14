package edu.vt.javadev.adventure.model;

import edu.vt.javadev.adventure.Message;

/**
 * Project 4 GameObject class.
 * @author Felix Taylor
 */
public class GameObject {

    // ==========================================================
    // Fields
    // ==========================================================

    private String name;
    private String description;
    private GameObject parent;

    // ==========================================================
    // Constructors
    // ==========================================================

    // Single argument constructor
    public GameObject(String name) {
        this(name, "");
    }

    public GameObject(String name, String description) {
        setName(name);
        setDescription(description);
        World.map.put(name, this); // Automatically add the object to the map
    }

    // ==========================================================
    // Getters & setters
    // ==========================================================

    public void setDescription(String description) {
        if (description == null) throw new IllegalArgumentException("description cannot be null");
        this.description = description;
    }

    private void setName(String name) {
        if (World.map.containsKey(name)) {
            throw new IllegalArgumentException("A game object with the name '" + name + "' already exists in the map.");
        }
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Name cannot be null or empty.");
        }
        if (!name.matches("^[a-z]+(-[a-z]+)*$")) {
            throw new IllegalArgumentException("Name must be in kebab-case (lowercase letters and dashes separating words).");
        }
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public GameObject getParent() {
        return parent;
    }

    public void setParent(GameObject parent) {
        this.parent = parent;
    }

    // ==========================================================
    // Simple accessors for name
    // ==========================================================

    public String theName() {
        return "the " + name;
    }

    public String capTheName() {
        return World.capitalize(theName());
    }

    public String aName() {

        if (name.endsWith("s")) {
            return name;
        }

        char firstChar = Character.toLowerCase(name.charAt(0));
        if ("aeiou".indexOf(firstChar) >= 0) {
            return "an " + name;
        }

        return "a " + name;
    }

    public String capAName() {
        return World.capitalize(aName());
    }

    // ==========================================================
    // Check if one game object is the ancestor of another in the object tree
    // ==========================================================

    public boolean hasAncestor(GameObject gameObject) {
        GameObject current = this;
        while (current != null) {
            if (current == gameObject) {
                return true;
            }
            current = current.getParent();
        }
        return false;
    }

    // ==========================================================
    // Action methods
    // ==========================================================

    public void examine() {
        boolean emptyDescription = description.isEmpty();
        boolean emptyStateDescription = getStateDescription().isEmpty();
        if (!emptyDescription && !emptyStateDescription) {
            World.currentMessage = getDescription() + " " + getStateDescription();
        } else if (emptyDescription && !emptyStateDescription) {
            World.currentMessage = getStateDescription();
        }
        else if (emptyDescription) {
            World.currentMessage = Message.examineNothingSpecial(this);
        } else {
            World.currentMessage = getDescription();
        }
    }

    public void search() {
        World.currentMessage = Message.searchNothing(this);
    }

    public String getStateDescription() {
        return "";
    }
}


