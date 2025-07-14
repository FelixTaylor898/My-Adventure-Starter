package edu.vt.javadev.adventure.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.vt.javadev.adventure.Message;

/**
 * Project 4 Container class.
 *
 * @author Felix Taylor
 */
public class Container extends GameObject {

    // ==========================================================
    // Fields
    // ==========================================================

    public List<GameObject> children;
    // Fields used in contentsDescription
    protected String contentsPrefix = "In " + theName() + " you see";
    protected String contentsSuffix = ".";

    // ==========================================================
    // Constructors
    // ==========================================================

    public Container(String name) {
        super(name);
        children = new ArrayList<>();
    }

    public Container(String name, String description) {
        super(name, description);
        children = new ArrayList<>();
    }

    // ==========================================================
    // Actions
    // ==========================================================

    @Override
    public void search() {
        World.currentMessage = children.isEmpty() ? Message.searchNothing(this) : getContentsDescription();
    }

    @Override
    public void examine() {
        String str = getDescription();
        if (!isEmpty()) {
            if (!str.isEmpty()) str += " ";
            str += getContentsDescription();
        }
        if (str.isEmpty()) str = Message.examineNothingSpecial(this);
        World.currentMessage = str;
    }

    // ==========================================================
    // Helpers
    // ==========================================================

    public String getContentsDescription() {
        List<GameObject> visible = visibleContents();
        if (visible.isEmpty()) {
            return "";
        }

        List<String> aList = visible.stream()
                .filter(this::isListable)
                .map(GameObject::aName)
                .toList();
        if (aList.isEmpty()) {
            return "";
        }
        String contentsList = contentsPrefix + " " + commaSep(aList) + (contentsSuffix.equals(".") ? "." : " " + contentsSuffix);
        StringBuilder sb = new StringBuilder(contentsList);

        for (GameObject go : visible) {
            if (go instanceof Person) continue;
            if (go instanceof Container c) {
                String d = c.getContentsDescription();
                if (!d.isEmpty()) sb.append(" ").append(d);
            }
        }
        return sb.toString();
    }

    private boolean isListable(GameObject gameObject) {
        return (!(gameObject instanceof Person) && (gameObject instanceof Item || gameObject instanceof Container || gameObject instanceof Switchable));
    }

    public void addChild(GameObject gameObject) {
        if (this.hasAncestor(gameObject)) {
            throw new IllegalArgumentException("Cannot add a GameObject as a child of itself or its descendant");
        }
        if (has(gameObject)) {
            throw new IllegalArgumentException(gameObject.getName() + " already exists in the container");
        }
        gameObject.setParent(this);
        children.add(gameObject);
    }

    public void removeChild(GameObject gameObject) {
        if (has(gameObject)) {
            gameObject.setParent(null);
            children.remove(gameObject);
        } else {
            throw new IllegalArgumentException(gameObject.getName() + " is not a child of " + getName());
        }
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
            for (GameObject child : c.visibleContents()) {
                populateSubtreeMap(child, map);
            }
        }
    }

    /**
     * Checks if container is empty.
     *
     * @return True if container is empty
     */
    public boolean isEmpty() {
        return children.isEmpty();
    }

    // ============================================================
    // Comma separated conjunction of words
    // ============================================================

    private String commaSep(List<String> words) {
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

    /**
     * Get list of all visible contents. Just gets all children in base Container.
     *
     * @return List of contents
     */
    public List<GameObject> visibleContents() {
        return children;
    }

    /**
     * Checks if container has a specific object.
     *
     * @param object GameObject
     * @return True if container has object
     */
    public boolean has(GameObject object) {
        return children.contains(object);
    }
}

