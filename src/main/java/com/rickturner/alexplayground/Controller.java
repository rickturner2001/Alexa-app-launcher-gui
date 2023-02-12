package com.rickturner.alexplayground;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;

import java.io.File;
import java.util.Iterator;

import redis.clients.jedis.Jedis;

public class Controller {

    private Jedis jedis = new Jedis();

    @FXML
    private TextField filenameInput;
    @FXML
    private Button confirmLabelNameBtn;
    @FXML
    private Button choseFileBtn;
    @FXML
    private VBox pathContainer;

    @FXML
    public void initialize() {
        populateContainer();
    }


    public void populateContainer() {
        pathContainer.getChildren().clear();
        pathContainer.setPadding(new Insets(20, 0, 0, 0));
        Iterator<String> labels = jedis.smembers("label").iterator();
        Iterator<String> paths = jedis.smembers("path").iterator();

        while (labels.hasNext() && paths.hasNext()) {

            String labelVal = labels.next();
            String pathVal = paths.next();

            GridPane hbox = new GridPane();
            hbox.setStyle("-fx-padding: 8px; -fx-background-color: #4338ca; -fx-border-radius: 10px");
            hbox.setMinWidth(Region.USE_COMPUTED_SIZE);

            ColumnConstraints labelColumn = new ColumnConstraints();
            labelColumn.setHalignment(HPos.LEFT);
            labelColumn.setPercentWidth(70);

            ColumnConstraints buttonColumn = new ColumnConstraints();
            buttonColumn.setHalignment(HPos.RIGHT);
            buttonColumn.setPercentWidth(30);

            hbox.getColumnConstraints().addAll(labelColumn, buttonColumn);

            Label label = new Label(labelVal);
            label.setStyle("-fx-padding: 4px; -fx-text-fill: white");
            GridPane.setHalignment(label, HPos.LEFT);


            Button button = new Button("Delete");
            button.setId(pathVal);
            button.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    jedis.srem("label", labelVal);
                    jedis.srem("path", pathVal);
                    populateContainer();
                }
            });
            button.setStyle("-fx-background-color: #ef4444; -fx-text-fill: white; -fx-border-radius: 10px; -fx-padding: 8px; -fx-border-radius: 10px");
            GridPane.setHalignment(button, HPos.RIGHT);

            hbox.add(label, 0, 0);
            hbox.add(button, 1, 0);

            pathContainer.getChildren().add(hbox);
        }
    }

    public void onFileChooseButtonClick(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();

        File selectedFile = fileChooser.showOpenDialog(null);
        if (selectedFile != null) {
            choseFileBtn.setText(selectedFile.getName());

        }
    }

    public void onConfirmButtonClick(ActionEvent actionEvent) {
        jedis.sadd("label", filenameInput.getText());
        jedis.sadd("path", choseFileBtn.getText());
        populateContainer();

        choseFileBtn.setText("Choose a file");
        filenameInput.setText("");
    }
}
