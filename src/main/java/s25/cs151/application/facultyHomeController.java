package s25.cs151.application;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class facultyHomeController {
    public void display(Stage primaryStage) throws IOException {


        Label header = new Label("Welcome to Faculty Home");
        header.setStyle("-fx-font-size: 25px; -fx-font-weight: bold;");

        HBox hb = new HBox();
        hb.setAlignment(Pos.CENTER);
        hb.setSpacing(20);
        Button btn1 = new Button("Define semester's office");
        Button btn2 = new Button("Time Slot");
        Button btn3 = new Button("Course");
        Button btn4 = new Button("Office hours schedule");
        hb.getChildren().addAll(btn1, btn2, btn3, btn4);
        VBox vb = new VBox(header, hb);
        vb.setAlignment(Pos.CENTER);
        vb.setSpacing(20);
        vb.setStyle("-fx-background-color: radial-gradient(center 50% 50%, radius 60%,  #fceabb, #f8b500);");

        btn1.setOnAction(e -> {
            OfficeHoursController officeHoursApp = new OfficeHoursController();
            try {
                Stage newStage = new Stage();
                officeHoursApp.start(newStage);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
        btn2.setOnAction(e -> {
            timeslotController timeslotApp = new timeslotController();

            try {
                Stage newStage = new Stage();
              timeslotApp.start(newStage);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
       btn3.setOnAction(e->{
           courseController courseApp = new courseController();
           try {
               Stage newStage = new Stage();
               courseApp.start(newStage);
           } catch (Exception ex) {
               ex.printStackTrace();
           }
       });




        primaryStage.setScene(new Scene(vb, 700, 500));
        primaryStage.setTitle("Faculty Home");
        primaryStage.show();
    }

}