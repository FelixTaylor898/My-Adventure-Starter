package edu.vt.javadev.adventure;

import edu.vt.javadev.adventure.model.*;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.scene.text.Font;

import java.util.Objects;

public class AdventureController {

    @FXML
    private WebView webView;

    @FXML
    private TextField inputField;

    @SuppressWarnings("unused")
    @FXML
    private Button moveButton;

    private WebEngine engine;

    @FXML
    private void initialize() {
        engine = webView.getEngine();
        engine.setUserStyleSheetLocation(
                Objects.requireNonNull(getClass().getResource("style.css")).toExternalForm()
        );
        inputField.setFont(Font.font("Georgia", 18));
        inputField.requestFocus();
    }

    @FXML
    private void processInput(String inputText) {
        String[] words = inputText.trim().split("\\s+");

        World.getCurrentRoom().look();
        switch (words.length) {
            case 0 -> World.currentMessage = "";
            case 1 -> processOneWordInput(words[0]);
            case 2 -> processTwoWordInput(words[0], words[1]);
            default -> World.currentMessage = Message.parserTooManyWords();
        }
        updateView();
    }

    private void processOneWordInput(String verb) {
        Room room = World.getCurrentRoom();
        switch (verb) {
            case "l", "look" -> room.look();
            case "n", "north" -> room.goDir("north");
            case "s", "south" -> room.goDir("south");
            case "e", "east" -> room.goDir("east");
            case "w", "west" -> room.goDir("west");
            case "i", "inventory" -> World.player.inventory();
            default -> World.currentMessage = Message.unknownCommand(verb);
        }
    }

    private void processTwoWordInput(String verb, String noun) {
        if (verb.equals("go")) {
            processOneWordInput(noun);
            return;
        }

        if (!World.map.containsKey(noun)) {
            World.currentMessage = Message.parserObjectNotFound(noun);
            return;
        }

        GameObject gameObject = World.map.get(noun);
        switch (verb) {
            case "x", "examine" -> gameObject.examine();
            case "search" -> gameObject.search();
            case "take" -> {
                if (gameObject instanceof Item i) {
                    i.take();
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
            default -> World.currentMessage = Message.unknownCommand(verb, noun);
        }
    }

    @FXML
    private void onSubmit() {
        String inputText = inputField.getText();
        if (!inputText.isEmpty()) {
            processInput(inputText);
            inputField.clear();
        } else {
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

        if (!World.currentMessage.isEmpty()) {
            content.append(String.format("<div>%s</div>", World.currentMessage));
        }

        content.append("</body></html>");
        engine.loadContent(content.toString());
    }
}
