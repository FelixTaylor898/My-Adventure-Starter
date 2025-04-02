package edu.vt.javadev.adventure;

import edu.vt.javadev.adventure.model.MyGame;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;

public class AdventureApp extends Application {

    @Override
    public void start(Stage primaryStage) throws IOException {
        // Initialize the game world
        new MyGame();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("adventure-view.fxml"));
        VBox root = fxmlLoader.load();
        Scene scene = new Scene(root, 800, 600);

        primaryStage.setTitle("AdventureFX");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
