package edu.vt.javadev.adventure.model;

import javafx.scene.control.TextInputDialog;

import java.util.Optional;

/**
 * Project 4 DialogCombinationInput class.
 * @author Felix Taylor
 */
public class DialogCombinationInput implements CombinationInputProvider {

    /**
     * Retrieve combination input from user.
     * @return input String
     */
    @Override
    public String getCombinationInput() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Input Combination");
        dialog.setHeaderText("Enter the combination:");
        dialog.setContentText("Combination:");
        Optional<String> result = dialog.showAndWait();
        return result.orElse("");
    }
}
