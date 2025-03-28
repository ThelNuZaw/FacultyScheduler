package s25.cs151.application;

import javafx.application.Application;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.*;

public class timeslotController extends Application {
    private ComboBox<String> startTime;
    private ComboBox<String> endTime;

    @Override
    public void start(Stage stage) throws Exception {
        Label header = new Label("Time Slot");
        header.setStyle("-fx-font-size: 25px; -fx-font-weight: bold;");

        startTime = new ComboBox<>();
        endTime = new ComboBox<>();
        //startTime.getItems().addAll(generateTime());
        //endTime.getItems().addAll(generateTime());
        Button submit = new Button("Submit");
        submit.setOnAction(e -> {});


    }
}