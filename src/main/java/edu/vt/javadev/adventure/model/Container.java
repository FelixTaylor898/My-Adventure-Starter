package edu.vt.javadev.adventure.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Container extends GameObject {
    public List<GameObject> children;

    // Fields used in contentsDescription
    protected String contentsPrefix = "In " + theName() + " you see";
    protected String contentsSuffix = ".";

    // Constructor calls through to super
    public Container(String name) {
        super(name);
        children = new ArrayList<>();
    }

    public void setContentsPrefix(String contentsPrefix) {
        this.contentsPrefix = contentsPrefix;
    }

    public void setContentsSuffix(String contentsSuffix) {
        this.contentsSuffix = contentsSuffix;
    }

    public String getContentsDescription() {
        if (children == null || children.isEmpty()) {
            return "";
        }

        List<String> aList = children.stream()
                .filter(this::isListable)
                .map(GameObject::aName)
                .toList();
        if (aList.isEmpty()) {
            return "";
        }
        String contentsList = contentsPrefix + " " + commaSep(aList) + " " + contentsSuffix;
        StringBuilder sb = new StringBuilder(contentsList);

        for (GameObject go : children) {
            if (go instanceof Container c && !(go instanceof Person)) {
                sb.append(" ").append(c.getContentsDescription());
            }
        }
        return sb.toString();
    }

    private boolean isListable(GameObject gameObject) {
        return (!(gameObject instanceof Person) && (gameObject instanceof Item || gameObject instanceof Container));
    }

    public void addChild(GameObject gameObject) {
        if (this.hasAncestor(gameObject)) {
            throw new IllegalArgumentException("Cannot add a GameObject as a child of itself or its descendant");
        }
        gameObject.setParent(this);
        children.add(gameObject);
    }

    public void removeChild(GameObject gameObject) {
        gameObject.setParent(null);
        children.remove(gameObject);
        // we do not remove the game object from the map
        // when a game object is created, it is added to the map, and it's parent is null
        // when a game object is added to a container, it's parent becomes the container
        // when a game object is removed from a container, it's parent becomes null again,
        // but it stays in the world map
    }

    // From ChatGPT, modified
    public Map<String, GameObject> getSubtreeMap() {
        Map<String, GameObject> subtreeMap = new HashMap<>();
        populateSubtreeMap(this, subtreeMap);
        return subtreeMap;
    }

    // Recursive helper method to populate the subtree map
    private void populateSubtreeMap(GameObject component, Map<String, GameObject> map) {
        map.put(component.getName(), component);

        // I allowed IntelliJ to use a pattern variable here.
        if (component instanceof Container c) { // if the component is an instance of a container named "c"
            for (GameObject child : c.children) {
                populateSubtreeMap(child, map);
            }
        }
    }

    // ============================================================
    // Comma separated conjunction of words
    // ============================================================

    private String commaSep(List<String> words) {
        if (words == null || words.isEmpty()) {
            return "";
        }
        if (words.size() == 1) {
            return words.get(0);
        }
        if (words.size() == 2) {
            return words.get(0) + " and " + words.get(1);
        }
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < words.size() - 1; i++) {
            result.append(words.get(i)).append(", ");
        }
        // Append the last word with "and"
        result.append("and ").append(words.get(words.size() - 1));
        return result.toString();
    }

}

