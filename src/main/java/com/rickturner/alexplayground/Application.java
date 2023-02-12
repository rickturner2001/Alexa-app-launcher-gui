package com.rickturner.alexplayground;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class Application extends javafx.application.Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("main.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 320, 240);
        stage.setTitle("Alexa Playground");
        stage.setScene(scene);
        stage.show();


        try {
            URI serverUri = new URI("");
            Client client = new Client (serverUri);
            client.connect();
        } catch (URISyntaxException ex) {
            System.out.println("An error occurred: " + ex.getMessage());
        }
    }

    public static void main(String[] args) {
        launch();
    }
}