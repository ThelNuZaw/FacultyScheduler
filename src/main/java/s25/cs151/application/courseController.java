package s25.cs151.application;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.*;

public class courseController extends Application {
    private TextField courseCode;
    private TextField courseName;
    private TextField sectionNumber;
    private TableView<Course> courseTable;

    @Override
    public void start(Stage stage) throws Exception, FileNotFoundException {

        Label header = new Label("Select the Course");
        header.setStyle("-fx-font-size: 25px; -fx-font-weight: bold; ");
        header.setAlignment(Pos.CENTER);

        courseCode = new TextField();
        Label courseCodeLabel = new Label("Enter Course Code");
        courseName = new TextField();
        Label courseNameLabel = new Label("Enter Course Name");
        sectionNumber = new TextField();
        Label sectionNumberLabel = new Label("Enter Section Number");




        Button submit = new Button("Submit");
        submit.setOnAction(e -> checkrequired());

        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10, 10, 10, 200));
        grid.setHgap(30);
        grid.setVgap(20);
        grid.add(courseCodeLabel, 0, 0);
        grid.add(courseCode, 1, 0);
        grid.add(courseNameLabel, 0, 1);
        grid.add(courseName, 1, 1);
        grid.add(sectionNumberLabel, 0, 2);
        grid.add(sectionNumber, 1, 2);
        grid.add(submit, 1, 3);

        VBox vb = new VBox(header, grid);
        //vb.setAlignment(Pos.CENTER);
        vb.setSpacing(10);
        vb.setStyle("-fx-alignment: center;" + "-fx-background-color: radial-gradient(center 50% 50%, radius 60%,  #fceabb, #f8b500);");

        stage.setScene(new Scene(vb, 700,500));
        stage.setTitle("Course");
        stage.show();


    }

    private void writecourseCSV(Course course) throws FileNotFoundException {
        File file = new File("course.csv");
        try (FileWriter fw = new FileWriter(file, true);
             BufferedWriter bw = new BufferedWriter(fw);
             PrintWriter pw = new PrintWriter(bw);){

            pw.println(course.getCourseCode() + ", " + course.getCourseName() +", " + course.getSectionNumber());


        }catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void checkrequired(){
        String code = courseCode.getText();
        String name = courseName.getText();
        String section = sectionNumber.getText();
        if(courseCode.getText().isEmpty() ){
            showAlert(Alert.AlertType.ERROR, "Course Code is required.");

        }
        if(courseName.getText().isEmpty() ){
            showAlert(Alert.AlertType.ERROR, "Course Name is required.");
        }
        if(sectionNumber.getText().isEmpty() ){
            showAlert(Alert.AlertType.ERROR, "Section Number is required.");
        }
        Course course = new Course(code, name, section);
        try {
            writecourseCSV(course);
            displaycourseTb();
        }catch (FileNotFoundException ex) {
            throw new RuntimeException(ex);
        }


    }

    private void displaycourseTb() {
        courseTable = createcourseTableView();
        courseTable.setItems(readcourseCSV());

        VBox container = new VBox(10, new Label("Faculty Course"), courseTable);
        container.setPadding(new javafx.geometry.Insets(15));
        Scene tableScene = new Scene(container, 700, 400);

        Stage tableStage = new Stage();
        tableStage.setTitle("Office Hours Table");
        tableStage.setScene(tableScene);
        tableStage.show();
    }

    private ObservableList<Course> readcourseCSV() {
        ObservableList<Course> list = FXCollections.observableArrayList();
        File file = new File("course.csv");
        if (!file.exists()) return list;

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)", -1);
                if (parts.length == 3) {
                    String code = parts[0];
                    String name = parts[1];
                    String section = parts[2].replaceAll("^\"|\"$", "");
                    list.add(new Course(code, name, section));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }

    private TableView<Course> createcourseTableView() {
        TableView<Course> table = new TableView<>();

        TableColumn<Course, String> CodeCol = new TableColumn<>("Course Code");
        CodeCol.setCellValueFactory(new PropertyValueFactory<>("courseCode"));

        TableColumn<Course, String> NameCol = new TableColumn<>("Course Name");
        NameCol.setCellValueFactory(new PropertyValueFactory<>("courseName"));

        TableColumn<Course, String> SectionCol = new TableColumn<>("Section Number");
        SectionCol.setCellValueFactory(new PropertyValueFactory<>("sectionNumber"));

        table.getColumns().addAll(CodeCol, NameCol, SectionCol);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        return table;
    }

    private void showAlert(Alert.AlertType alertType, String s) {
        Alert alert = new Alert(alertType);
        alert.setTitle("Error");
        alert.setHeaderText(s);
        alert.showAndWait();
    }


}
