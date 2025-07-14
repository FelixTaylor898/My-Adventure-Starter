package edu.vt.javadev.adventure;

import edu.vt.javadev.adventure.model.*;
import edu.vt.javadev.adventure.model.Readable;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.scene.text.Font;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class AdventureController {

    // ============================================
    // Fields
    // ============================================

    @FXML
    private WebView webView;

    @FXML
    private TextField inputField;

    @SuppressWarnings("unused")
    @FXML
    private Button moveButton;

    private WebEngine engine;

    private final List<String> validTwoWordCommands = new ArrayList<>();

    // ============================================
    // Initialization
    // ============================================

    @FXML
    private void initialize() {
        engine = webView.getEngine();
        engine.setUserStyleSheetLocation(
                Objects.requireNonNull(getClass().getResource("style.css")).toExternalForm()
        );
        inputField.setFont(Font.font("Georgia", 18));
        inputField.requestFocus();
        World.currentMessage = "What is your name?";
        updateView();
        validTwoWordCommands.add("search");
        validTwoWordCommands.add("examine");
        validTwoWordCommands.add("x");
        validTwoWordCommands.add("take");
        validTwoWordCommands.add("open");
        validTwoWordCommands.add("close");
        validTwoWordCommands.add("lock");
        validTwoWordCommands.add("unlock");
        validTwoWordCommands.add("read");
        validTwoWordCommands.add("place");
        validTwoWordCommands.add("play");
        validTwoWordCommands.add("pull");
        validTwoWordCommands.add("turn-on");
        validTwoWordCommands.add("turn-off");
    }

    // ============================================
    // Process inputs
    // ============================================

    @FXML
    private void processInput(String inputText) {
        String[] words = inputText.trim().split("\\s+");

        if (World.player.getAlias() == null) {
            World.player.setAlias(words[0]);
            World.player.examine();
            updateView();
            return;
        }

        World.getCurrentRoom().look();
        switch (words.length) {
            case 0 -> World.currentMessage = "";
            case 1 -> processOneWordInput(words[0]);
            case 2 -> processTwoWordInput(words[0], words[1]);
            default -> World.currentMessage = Message.parserTooManyWords;
        }
        updateView();
    }

    private void processOneWordInput(String verb) {
        Room room = World.getCurrentRoom();
        switch (verb) {
            case "l", "look" -> {
                room.look();
                World.currentMessage = "";
            }
            case "n", "north" -> room.goDir("north");
            case "s", "south" -> room.goDir("south");
            case "e", "east" -> room.goDir("east");
            case "w", "west" -> room.goDir("west");
            case "i", "inventory" -> World.player.inventory();
            case "exit" -> World.exit();
            default -> World.currentMessage = Message.unknownCommand(verb);
        }
    }

    private void processTwoWordInput(String verb, String noun) {
        if (verb.equals("go")) {
            processOneWordInput(noun);
            return;
        }

        if (verb.equals("enter")) {
            World.getCurrentRoom().enter(noun);
            return;
        }

        if (!validTwoWordCommands.contains(verb)) {
            World.currentMessage = Message.unknownCommand(verb, noun);
            return;
        }

        Map<String, GameObject> scope = World.getCurrentRoom().getSubtreeMap();

        GameObject gameObject;
        if (noun.equals(World.player.getAlias())) {
            gameObject = World.player;
        } else {
            gameObject = resolveFromScope(noun, scope);
            if (gameObject == null) {
                World.currentMessage = Message.scopeNotFound(noun);
                return;
            }
        }

        switch (verb) {
            case "x", "examine" -> gameObject.examine();
            case "search" -> gameObject.search();
            case "read" -> {
                if (gameObject instanceof Readable b) {
                    b.read();
                } else {
                    World.currentMessage = Message.readCant(gameObject);
                }
            }
            case "place" -> {
                if (gameObject instanceof Placeable p) {
                    p.place();
                } else {
                    World.currentMessage = Message.placeCant(gameObject);
                }
            }
            case "play" -> {
                if (gameObject instanceof Playable p) {
                    p.play();
                } else {
                    World.currentMessage = Message.playCant(gameObject);
                }
            }
            case "pull" -> {
                if (gameObject instanceof Pullable p) {
                    p.pull();
                } else {
                    World.currentMessage = Message.pullCant(gameObject);
                }
            }
            case "take" -> {
                if (World.player.has(gameObject)) {
                    World.currentMessage = Message.takeAlready(gameObject);
                }
                else if (gameObject instanceof Takeable t) {
                    t.take();
                } else {
                    World.currentMessage = Message.takeCant(gameObject);
                }
            }
            case "open" -> {
                if (gameObject instanceof Openable openable) {
                    openable.open();
                } else {
                    World.currentMessage = Message.openCant(gameObject);
                }
            }
            case "close" -> {
                if (gameObject instanceof Openable openable) {
                    openable.close();
                } else {
                    World.currentMessage = Message.closeCant(gameObject);
                }
            }
            case "lock" -> {
                if (gameObject instanceof Lockable lockable) {
                    lockable.lock();
                } else {
                    World.currentMessage = Message.lockCant(gameObject);
                }
            }
            case "unlock" -> {
                if (gameObject instanceof Lockable lockable) {
                    lockable.unlock();
                } else {
                    World.currentMessage = Message.unlockCant(gameObject);
                }
            }
            case "turn-on" -> {
                if (gameObject instanceof Switchable s) {
                    s.turnOn();
                } else {
                    World.currentMessage = Message.turnOnCant(gameObject);
                }
            }
            case "turn-off" -> {
                if (gameObject instanceof Switchable s) {
                    s.turnOff();
                } else {
                    World.currentMessage = Message.turnOffCant(gameObject);
                }
            }
            default -> World.currentMessage = Message.unknownCommand(verb, noun);
        }
    }

    // ============================================
    // Helpers
    // ============================================

    @FXML
    private void onSubmit() {
        String inputText = inputField.getText();
        if (inputText != null && !inputText.trim().isEmpty()) {
            processInput(inputText);
            inputField.clear();
        } else if (World.player.getAlias() != null) {
            World.currentMessage = "";
            updateView();
        }
    }

    private void updateView() {
        World.getCurrentRoom().look();

        StringBuilder content = new StringBuilder();
        content.append("<html><head></head><body>");
        content.append(String.format("<div><b>%s</b></div>", World.roomName));
        content.append(String.format("<div>%s</div>", World.roomDescription));
        if (World.isHaunted()) {
            content.append(String.format("<div>%s</div>", Message.randomGhostMessage()));
            content.append("<div>&nbsp;</div>");
        }
        if (!World.currentMessage.isEmpty()) {
            content.append(String.format("<div>%s</div>", World.currentMessage));
        }

        content.append("</body></html>");
        engine.loadContent(content.toString());
    }

    private static GameObject resolveFromScope(String noun, Map<String, GameObject> scope) {
        // 1. Exact match
        if (scope.containsKey(noun)) {
            return scope.get(noun);
        }

        // 2. Suffix match (e.g., "door" matches "kitchen-door")
        List<GameObject> suffixMatches = scope.entrySet().stream()
                .filter(entry -> entry.getKey().endsWith("-" + noun))
                .map(Map.Entry::getValue)
                .toList();
        if (suffixMatches.size() == 1) {
            return suffixMatches.getFirst();
        }

        // 3. Word match (e.g., "pin" matches "safety-pin")
        List<GameObject> substringMatches = scope.entrySet().stream()
                .filter(entry -> List.of(entry.getKey().split("-")).contains(noun))
                .map(Map.Entry::getValue)
                .toList();
        if (substringMatches.size() == 1) {
            return substringMatches.getFirst();
        }

        // 4. No match or ambiguous
        return null;
    }
}
