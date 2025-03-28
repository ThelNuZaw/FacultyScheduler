package s25.cs151.application;

import javafx.application.Application;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.*;

public class OfficeHoursController extends Application {

    private ComboBox<String> semesterDropdown;
    private TextField yearInput;
    private CheckBox mondayCheckbox, tuesdayCheckbox, wednesdayCheckbox, thursdayCheckbox, fridayCheckbox;
    private TableView<OfficeHour> table;


    public void start(Stage primarystage){
        Label header = new Label("Office Hour Information");
        header.setStyle("-fx-font-size: 25px; -fx-font-weight: bold; ");

        yearInput = new TextField();
        yearInput.setPromptText("Enter Year");
        yearInput.textProperty().addListener((obs, oldText, newText) -> {
            if (!newText.matches("\\d*")) {
                yearInput.setText(newText.replaceAll("[^\\d]", ""));
            }
        });

        semesterDropdown = new ComboBox<>();
        semesterDropdown.getItems().addAll("Spring", "Summer", "Fall", "Winter");
        semesterDropdown.setValue("Spring");

        mondayCheckbox = new CheckBox("Monday");
        tuesdayCheckbox = new CheckBox("Tuesday");
        wednesdayCheckbox = new CheckBox("Wednesday");
        thursdayCheckbox = new CheckBox("Thursday");
        fridayCheckbox = new CheckBox("Friday");

        HBox daysBox = new HBox(15, mondayCheckbox, tuesdayCheckbox, wednesdayCheckbox, thursdayCheckbox, fridayCheckbox);
        daysBox.setStyle("-fx-alignment: center;");

        Button submitButton = new Button("Submit");
        submitButton.setOnAction(e -> onSubmitButtonClick());

        VBox root = new VBox(10,
                header,
                new Label("Input Year:"), yearInput,
                new Label("Select Semester:"), semesterDropdown,
                new Label("Select Days:"), daysBox,
                submitButton
        );
        root.setPadding(new javafx.geometry.Insets(10, 20, 10, 20));
        root.setStyle("-fx-alignment: center;" +  "-fx-background-color: radial-gradient(center 50% 50%, radius 60%,  #fceabb, #f8b500);");


        Scene scene = new Scene(root, 700, 500);
        primarystage.setScene(scene);
        primarystage.setTitle("Define Office Hours");
       primarystage.show();
    }

    private void onSubmitButtonClick() {
        if (semesterDropdown.getValue() == null || yearInput.getText().isEmpty() || !yearInput.getText().matches("\\d{4}") ||
                !(mondayCheckbox.isSelected() || tuesdayCheckbox.isSelected() ||
                        wednesdayCheckbox.isSelected() || thursdayCheckbox.isSelected() || fridayCheckbox.isSelected())) {
            showAlert(Alert.AlertType.ERROR, "Input Error", "Missing Required Fields", "Please select a semester and enter a valid year.");
            return;
        }

        String year = yearInput.getText();
        String semester = semesterDropdown.getSelectionModel().getSelectedItem();

        StringBuilder selectedDays = new StringBuilder();
        if (mondayCheckbox.isSelected()) selectedDays.append("Monday, ");
        if (tuesdayCheckbox.isSelected()) selectedDays.append("Tuesday, ");
        if (wednesdayCheckbox.isSelected()) selectedDays.append("Wednesday, ");
        if (thursdayCheckbox.isSelected()) selectedDays.append("Thursday, ");
        if (fridayCheckbox.isSelected()) selectedDays.append("Friday, ");
        if (selectedDays.length() > 0) selectedDays.setLength(selectedDays.length() - 2);
        String days = selectedDays.toString();

        showAlert(Alert.AlertType.INFORMATION, "Office Hours Set", "Receipt of Submission",
                "Year: " + year + "\nSemester: " + semester + "\n" + days);
        OfficeHour fh = new OfficeHour(year, semester, days);

        try {
            writeToCSVFile(fh);
            displayTableView();
        } catch (FileNotFoundException ex) {
            throw new RuntimeException(ex);
        }
    }
    private void displayTableView() {
        table = createTableView();
        table.setItems(loadFromCSV());

        VBox container = new VBox(10, new Label("Faculty Office Hours Table:"), table);
        container.setPadding(new javafx.geometry.Insets(15));
        Scene tableScene = new Scene(container, 700, 400);

        Stage tableStage = new Stage();
        tableStage.setTitle("Office Hours Table");
        tableStage.setScene(tableScene);
        tableStage.show();
    }


    private void showAlert(Alert.AlertType type, String title, String header, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private void writeToCSVFile(OfficeHour fh) throws FileNotFoundException {
        File csvFile = new File("officeHours.csv");
        try (PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(csvFile, true)))) {
            out.println(fh.getYear() + "," + fh.getSemester()+ ",\"" + fh.getSelectedDays() + "\"");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private ObservableList<OfficeHour> loadFromCSV() {
        ObservableList<OfficeHour> list = FXCollections.observableArrayList();
        File file = new File("officeHours.csv");
        if (!file.exists()) return list;

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)", -1);
                if (parts.length == 3) {
                    String year = parts[0];
                    String semester = parts[1];
                    String days = parts[2].replaceAll("^\"|\"$", "");
                    list.add(new OfficeHour(year, semester, days));
                }
            }

            // 🔽 Sort by descending year, then descending semester
            list.sort((o1, o2) -> {
                int y1 = Integer.parseInt(o1.getYear());
                int y2 = Integer.parseInt(o2.getYear());
                if (y1 != y2) return Integer.compare(y2, y1); // Descending year
                return Integer.compare(TrackSemester(o2.getSemester()), TrackSemester(o1.getSemester())); // Descending semester
            });

        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }


    private int TrackSemester(String semester) {
        return switch (semester.trim()) {
            case "Spring" -> 4;
            case "Summer" -> 3;
            case "Fall"   -> 2;
            case "Winter" -> 1;
            default -> 0;
        };
    }

    private TableView<OfficeHour> createTableView() {
        TableView<OfficeHour> table = new TableView<>();

        TableColumn<OfficeHour, String> yearCol = new TableColumn<>("Year");
        yearCol.setCellValueFactory(new PropertyValueFactory<>("year"));

        TableColumn<OfficeHour, String> semesterCol = new TableColumn<>("Semester");
        semesterCol.setCellValueFactory(new PropertyValueFactory<>("semester"));

        TableColumn<OfficeHour, String> daysCol = new TableColumn<>("Selected Days");
        daysCol.setCellValueFactory(new PropertyValueFactory<>("selectedDays"));

        table.getColumns().addAll(yearCol, semesterCol, daysCol);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        return table;
    }

}
