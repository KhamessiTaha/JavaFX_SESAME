package org.tahakhamessi;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.tahakhamessi.util.DatabaseManager;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        DatabaseManager.initialize();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Login.fxml"));
        Scene scene = new Scene(loader.load(), 600, 400);
        stage.setTitle("Car Rental Management");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
