package com.example.anime_app;

import javafx.fxml.FXML;
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

public class AnimeDetailsController {

    @FXML
    private ImageView posterImageView; // ImageView for displaying the anime poster
    @FXML
    private Label titleLabel; // Label for displaying the anime title
    @FXML
    private Label yearLabel; // Label for displaying the release year of the anime
    @FXML
    private Label ratedLabel; // Label for displaying the anime's rating
    @FXML
    private Label releasedLabel; // Label for displaying the release date of the anime
    @FXML
    private Label genreLabel; // Label for displaying the anime's genre
    @FXML
    private Label writerLabel; // Label for displaying the anime's writer
    @FXML
    private Label actorsLabel; // Label for displaying the actors in the anime
    @FXML
    private Label plotLabel; // Label for displaying the plot of the anime
    @FXML
    private Label languageLabel; // Label for displaying the language of the anime
    @FXML
    private Label imdbRatingLabel; // Label for displaying the IMDB rating of the anime
    @FXML
    private VBox root; // VBox layout container for the details view

    private Stage stage; // Reference to the current Stage
    private Map<String, JSONObject> favorites; // Map to store favorite animes
    private JSONObject currentAnime; // JSONObject holding the details of the current anime

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
        root.setBackground(new Background(backgroundImage));

        // Adjust image view for poster
        posterImageView.setPreserveRatio(false); // Allow full stretching
        posterImageView.setFitWidth(200); // Set width of the poster image
        posterImageView.setFitHeight(300); // Set height of the poster image
    }

    // Set the stage for this controller
    public void setStage(Stage stage) {
        this.stage = stage;
    }

    // Set the favorites map for this controller
    public void setFavorites(Map<String, JSONObject> favorites) {
        this.favorites = favorites;
    }

    // Load anime details into the view
    public void loadAnimeDetails(JSONObject animeDetails) {
        currentAnime = animeDetails;
        // Set text for each label with data from the JSONObject
        titleLabel.setText("Title: " + animeDetails.optString("Title", "N/A"));
        yearLabel.setText("Year: " + animeDetails.optString("Year", "N/A"));
        ratedLabel.setText("Rated: " + animeDetails.optString("Rated", "N/A"));
        releasedLabel.setText("Released: " + animeDetails.optString("Released", "N/A"));
        genreLabel.setText("Genre: " + animeDetails.optString("Genre", "N/A"));
        writerLabel.setText("Writer: " + animeDetails.optString("Writer", "N/A"));
        actorsLabel.setText("Actors: " + animeDetails.optString("Actors", "N/A"));
        plotLabel.setText("Plot: " + animeDetails.optString("Plot", "N/A"));
        languageLabel.setText("Language: " + animeDetails.optString("Language", "N/A"));
        imdbRatingLabel.setText("IMDB Rating: " + animeDetails.optString("imdbRating", "N/A"));

        // Load poster image if URL is available
        String posterUrl = animeDetails.optString("Poster", "N/A");
        if (!posterUrl.equals("N/A")) {
            Image posterImage = new Image(posterUrl, true);  // Load asynchronously
            posterImageView.setImage(posterImage);
        } else {
            posterImageView.setImage(null); // Clear the image view if no poster is available
        }
    }

    // Toggle the current anime as a favorite
    @FXML
    private void toggleFavorite() {
        String animeID = currentAnime.optString("imdbID", "N/A");
        if (favorites.containsKey(animeID)) {
            favorites.remove(animeID); // Remove from favorites if already added
        } else {
            favorites.put(animeID, currentAnime); // Add to favorites if not already added
        }
    }

    // Navigate back to the search view
    @FXML
    private void goBackToSearch() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Main.fxml"));
            Scene scene = new Scene(loader.load());

            // Pass favorites data back to the main controller
            MainController controller = loader.getController();
            controller.setFavorites(favorites);

            stage.setScene(scene); // Set the new scene for the stage
            stage.show(); // Display the stage
        } catch (IOException e) {
            e.printStackTrace(); // Print the stack trace if there's an error loading the FXML
        }
    }
}
