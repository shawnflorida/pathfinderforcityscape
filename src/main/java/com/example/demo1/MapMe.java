package com.example.demo1;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import java.io.IOException;

public class MapMe extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MapMe.class.getResource("sampleFile.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1800, 900);
        stage.setTitle("Map Me v1.0");
        stage.getIcons().add(new Image("file:src/main/resources/assets/mapme.png"));
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}