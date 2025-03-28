package s25.cs151.application;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;


import javax.swing.*;
import java.io.FileNotFoundException;
import java.io.IOException;

public class Main extends Application {

    @Override
    public void start(Stage primarystage) throws IOException {
        facultyHomeController hp = new facultyHomeController();
        hp.display(primarystage);
    }

    public static void main(String[] args) throws FileNotFoundException {
        launch();
    }
}