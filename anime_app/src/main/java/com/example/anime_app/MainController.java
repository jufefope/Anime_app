package com.example.anime_app;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.Parent;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class MainController {

    @FXML
    private VBox root; // Root VBox container for the main view

    @FXML
    private TextField searchField; // TextField for entering search queries

    @FXML
    private GridPane resultsGrid; // GridPane to display search results

    private final String API_KEY = "175b73c"; // API key for accessing the OMDB API
    private Map<String, JSONObject> favorites = new HashMap<>(); // Map to store favorite animes

    @FXML
    public void initialize() {
        // Configure the background image
        Image fondo = new Image(getClass().getResourceAsStream("/com/example/anime_app/images/fondo.png"));
        BackgroundSize backgroundSize = new BackgroundSize(100, 100, true, true, true, false);
        BackgroundImage backgroundImage = new BackgroundImage(fondo,
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.CENTER,
                backgroundSize);
        root.setBackground(new Background(backgroundImage)); // Set the background for the root
    }

    // Handle the search operation
    @FXML
    private void handleSearch() {
        String query = searchField.getText(); // Get the search query
        if (!query.isEmpty()) { // If the query is not empty
            String urlString = "https://www.omdbapi.com/?s=" + query + "&apikey=" + API_KEY; // Construct the API URL
            try {
                URL url = new URL(urlString);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection(); // Open the connection
                conn.setRequestMethod("GET"); // Set the request method to GET
                conn.connect();

                int responseCode = conn.getResponseCode(); // Get the response code
                if (responseCode != 200) {
                    throw new RuntimeException("HttpResponseCode: " + responseCode); // Throw an exception if not successful
                } else {
                    StringBuilder inline = new StringBuilder();
                    Scanner scanner = new Scanner(url.openStream()); // Read the response
                    while (scanner.hasNext()) {
                        inline.append(scanner.nextLine());
                    }
                    scanner.close();

                    JSONObject jsonResponse = new JSONObject(inline.toString()); // Parse the response
                    JSONArray searchResults = jsonResponse.getJSONArray("Search"); // Extract the search results

                    resultsGrid.getChildren().clear(); // Clear existing results
                    for (int i = 0; i < searchResults.length(); i++) {
                        JSONObject anime = searchResults.getJSONObject(i); // Get each anime result
                        String title = anime.getString("Title"); // Get the title
                        String poster = anime.getString("Poster"); // Get the poster URL

                        VBox animeBox = createAnimeItem(title, poster, anime); // Create a VBox for each anime
                        resultsGrid.add(animeBox, i % 3, i / 3); // Place in a 3x3 grid
                    }
                }
            } catch (IOException e) {
                e.printStackTrace(); // Print the stack trace if there's an error
            }
        }
    }

    // Create a VBox for an individual anime item
    private VBox createAnimeItem(String title, String posterUrl, JSONObject animeData) {
        VBox itemBox = new VBox(5); // VBox with spacing between elements
        itemBox.setStyle("-fx-padding: 10; -fx-border-color: orange; -fx-border-width: 2; -fx-border-radius: 10; -fx-background-radius: 10;");
        itemBox.setAlignment(Pos.CENTER); // Center-align the contents

        ImageView imageView = new ImageView(new Image(posterUrl)); // Create ImageView for the poster
        imageView.setFitHeight(150); // Set height
        imageView.setFitWidth(100); // Set width
        imageView.setPreserveRatio(true); // Preserve aspect ratio

        Label titleLabel = new Label(title); // Create a label for the title
        titleLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: white;"); // Style the label

        itemBox.getChildren().addAll(imageView, titleLabel); // Add image and title to the VBox

        // Set an event handler for clicking on the item
        itemBox.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            String imdbID = animeData.optString("imdbID", "N/A"); // Get the IMDB ID
            showAnimeDetails(imdbID); // Show details for the selected anime
        });

        return itemBox; // Return the created item box
    }

    // Show details of a selected anime
    private void showAnimeDetails(String imdbID) {
        String urlString = "https://www.omdbapi.com/?i=" + imdbID + "&apikey=" + API_KEY; // Construct the API URL
        try {
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection(); // Open the connection
            conn.setRequestMethod("GET"); // Set the request method to GET
            conn.connect();

            int responseCode = conn.getResponseCode(); // Get the response code
            if (responseCode != 200) {
                throw new RuntimeException("HttpResponseCode: " + responseCode); // Throw an exception if not successful
            } else {
                StringBuilder inline = new StringBuilder();
                Scanner scanner = new Scanner(url.openStream()); // Read the response
                while (scanner.hasNext()) {
                    inline.append(scanner.nextLine());
                }
                scanner.close();

                JSONObject animeDetails = new JSONObject(inline.toString()); // Parse the response

                // Load the details view
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/anime_app/animeDetails.fxml"));
                Parent root = loader.load();
                AnimeDetailsController controller = loader.getController(); // Get the details controller
                controller.loadAnimeDetails(animeDetails); // Load the details into the controller
                controller.setFavorites(favorites); // Pass favorites data to the controller

                Stage stage = (Stage) this.root.getScene().getWindow(); // Get the current stage
                controller.setStage(stage); // Pass the stage to the controller
                stage.setScene(new Scene(root)); // Set the new scene
                stage.show(); // Show the stage
            }
        } catch (IOException e) {
            e.printStackTrace(); // Print the stack trace if there's an error
        }
    }

    // View the list of favorite animes
    @FXML
    private void viewFavorites() {
        try {
            // Load the favorites view
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/anime_app/favorites.fxml"));
            Parent root = loader.load();
            FavoritesController controller = loader.getController(); // Get the favorites controller
            controller.setFavorites(favorites); // Pass favorites data to the controller

            Stage stage = (Stage) resultsGrid.getScene().getWindow(); // Get the current stage
            controller.setStage(stage); // Pass the stage to the controller
            stage.setScene(new Scene(root)); // Set the new scene
            stage.show(); // Show the stage
        } catch (IOException e) {
            e.printStackTrace(); // Print the stack trace if there's an error
        }
    }

    // Set the favorites map for this controller
    public void setFavorites(Map<String, JSONObject> favorites) {
        this.favorites = favorites;
    }
}
