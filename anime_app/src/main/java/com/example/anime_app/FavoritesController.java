package com.example.anime_app;

import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.fxml.FXMLLoader;
import java.io.IOException;
import java.util.Map;
import org.json.JSONObject;

public class FavoritesController {

    @FXML
    private VBox root; // Root VBox container for the favorites view
    @FXML
    private GridPane favoritesGrid; // GridPane to display favorite anime items

    private Stage stage; // Reference to the current Stage
    private Map<String, JSONObject> favorites; // Map to store favorite animes

    // Set the stage for this controller
    public void setStage(Stage stage) {
        this.stage = stage;
    }

    // Set the favorites map for this controller and populate the grid
    public void setFavorites(Map<String, JSONObject> favorites) {
        this.favorites = favorites;
        populateFavoritesGrid();
    }

    // Populate the GridPane with favorite anime items
    private void populateFavoritesGrid() {
        favoritesGrid.getChildren().clear(); // Clear existing items
        int col = 0;
        int row = 0;
        for (JSONObject anime : favorites.values()) {
            VBox animeBox = createAnimeItem(anime); // Create a VBox for each anime
            favoritesGrid.add(animeBox, col, row); // Add the VBox to the GridPane
            col++;
            if (col == 3) { // Move to the next row after three columns
                col = 0;
                row++;
            }
        }
    }

    // Create a VBox for an individual anime item
    private VBox createAnimeItem(JSONObject animeData) {
        VBox itemBox = new VBox(5); // VBox with spacing between elements
        itemBox.setStyle("-fx-padding: 10; -fx-border-color: orange; -fx-border-width: 2; -fx-border-radius: 10; -fx-background-radius: 10;");
        itemBox.setAlignment(Pos.CENTER); // Center-align the contents

        String title = animeData.optString("Title", "N/A"); // Get the title
        String posterUrl = animeData.optString("Poster", "N/A"); // Get the poster URL

        ImageView imageView = new ImageView(new Image(posterUrl, true)); // Create ImageView for the poster
        imageView.setFitHeight(150); // Set height
        imageView.setFitWidth(100); // Set width
        imageView.setPreserveRatio(true); // Preserve aspect ratio

        Label titleLabel = new Label(title); // Create a label for the title
        titleLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: white;"); // Style the label

        itemBox.getChildren().addAll(imageView, titleLabel); // Add image and title to the VBox

        // Set an event handler for clicking on the item
        itemBox.setOnMouseClicked(event -> showAnimeDetails(animeData));

        return itemBox; // Return the created item box
    }

    // Show details of a selected anime
    private void showAnimeDetails(JSONObject animeDetails) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("animeDetails.fxml")); // Load the details view
            Scene scene = new Scene(loader.load());

            AnimeDetailsController controller = loader.getController(); // Get the details controller
            controller.loadAnimeDetails(animeDetails); // Load the details into the controller
            controller.setFavorites(favorites); // Pass favorites data to the controller
            controller.setStage(stage); // Pass the stage to the controller

            stage.setScene(scene); // Set the new scene
            stage.show(); // Show the stage
        } catch (IOException e) {
            e.printStackTrace(); // Print the stack trace if there's an error loading the FXML
        }
    }

    // Navigate back to the search view
    @FXML
    private void goBackToSearch() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Main.fxml")); // Load the main view
            Scene scene = new Scene(loader.load());

            MainController controller = loader.getController(); // Get the main controller
            controller.setFavorites(favorites); // Pass favorites data to the main controller

            stage.setScene(scene); // Set the new scene
            stage.show(); // Show the stage
        } catch (IOException e) {
            e.printStackTrace(); // Print the stack trace if there's an error loading the FXML
        }
    }

    @FXML
    public void initialize() {
        // Set up the background image
        Image fondo = new Image(getClass().getResourceAsStream("/com/example/anime_app/images/fondo.png"));
        BackgroundSize backgroundSize = new BackgroundSize(100, 100, true, true, true, false);
        BackgroundImage backgroundImage = new BackgroundImage(fondo,
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.CENTER,
                backgroundSize);
        root.setBackground(new Background(backgroundImage)); // Set the background for the root
    }
}
