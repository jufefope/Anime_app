module com.example.anime_app {
    // Requires the JavaFX controls module for UI components
    requires javafx.controls;
    // Requires the JavaFX FXML module for FXML support
    requires javafx.fxml;
    // Requires the java.net.http module for HTTP connections
    requires java.net.http;
    // Requires the org.json module for JSON processing
    requires org.json;

    // Opens the package to JavaFX FXML for reflection access
    opens com.example.anime_app to javafx.fxml;
    // Exports the package so it can be accessed by other modules
    exports com.example.anime_app;
}
