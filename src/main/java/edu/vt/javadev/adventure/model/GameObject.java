package edu.vt.javadev.adventure.model;

public class GameObject {
    public final String name;
    public String description;
    private GameObject parent;
    private String article = "a";

    // Single argument constructor
    public GameObject(String name) {
        this(name, "");
    }

    public GameObject(String name, String description) {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Name cannot be null or empty.");
        }
        if (!name.matches("^[a-z]+(-[a-z]+)*$")) {
            throw new IllegalArgumentException("Name must be in kebab-case (lowercase letters and dashes separating words).");
        }
        if (description == null) {
            throw new IllegalArgumentException("Description cannot be null.");
        }
        if (World.map.containsKey(name)) {
            throw new IllegalArgumentException("A game object with the name '" + name + "' already exists in the map.");
        }

        this.name = name;
        this.description = description;
        World.map.put(name, this); // Automatically add the object to the map
    }

    // ==========================================================
    // Getters and setters
    // ==========================================================

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public GameObject getParent() {
        return parent;
    }

    public void setParent(GameObject parent) {
        this.parent = parent;
    }

    public String getArticle() {
        return article;
    }

    public void setArticle(String article) {
        this.article = article;
    }

    // ==========================================================
    // Simple accessors for name
    // ==========================================================

    public String theName() {
        return "the " + name;
    }

    public String capTheName() {
        return capitalize(theName());
    }

    public String aName() {
        return article + " " + name;
    }

    public String capAName() {
        return capitalize(aName());
    }

    public String capitalize(String string) {
        return string.substring(0, 1).toUpperCase() + string.substring(1);
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
        if (description == null || description.isEmpty()) {
            World.currentMessage = "You see nothing unusual about " + theName() + ".";
        } else {
            World.currentMessage = getDescription();
        }
    }

    public void search() {
        World.currentMessage = "You search " + theName() + ", but you don't find anything interesting.";
    }
}


