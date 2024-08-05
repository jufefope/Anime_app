package com.example.anime_app;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        // Load the FXML file for the main interface
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/anime_app/Main.fxml"));
        Parent root = loader.load(); // Load the root node from the FXML file

        // Create a new scene with the loaded root node, setting its width and height
        Scene scene = new Scene(root, 650, 380);

        // Set the title of the primary stage (window)
        primaryStage.setTitle("Anime Search App");

        // Load and set the icon for the application window
        Image icon = new Image(getClass().getResourceAsStream("/com/example/anime_app/images/icon.png"));
        primaryStage.getIcons().add(icon);

        // Set the scene for the primary stage and display the window
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        // Launch the JavaFX application
        launch(args);
    }
}
