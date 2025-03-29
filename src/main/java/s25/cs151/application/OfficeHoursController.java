package s25.cs151.application;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class OfficeHoursController extends Application {
    private final OfficeHourDAO dao = new OfficeHourimpDAO();

    private ComboBox<String> semesterDropdown;
    private TextField yearInput;
    private CheckBox mondayCheckbox, tuesdayCheckbox, wednesdayCheckbox, thursdayCheckbox, fridayCheckbox;

    @Override
    public void start(Stage primarystage){
        Label header = new Label("Office Hour Information");
        header.setStyle("-fx-font-size: 25px; -fx-font-weight: bold;");

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
        root.setPadding(new Insets(10, 20, 10, 20));
        root.setStyle("-fx-alignment: center;-fx-background-color: radial-gradient(center 50% 50%, radius 60%,  #fceabb, #f8b500);");

        Scene scene = new Scene(root, 700, 500);
        primarystage.setScene(scene);
        primarystage.setTitle("Define Office Hours");
        primarystage.show();
    }

    private void onSubmitButtonClick() {
        if (semesterDropdown.getValue() == null || yearInput.getText().isEmpty() || !yearInput.getText().matches("\\d{4}") ||
                !(mondayCheckbox.isSelected() || tuesdayCheckbox.isSelected() ||
                        wednesdayCheckbox.isSelected() || thursdayCheckbox.isSelected() || fridayCheckbox.isSelected())) {
            showAlert("Input Error", "Missing Required Fields", "Please select a semester and enter a valid year.");
            return;
        }

        String year = yearInput.getText();
        String semester = semesterDropdown.getValue();

        StringBuilder selectedDays = new StringBuilder();
        if (mondayCheckbox.isSelected()) selectedDays.append("Monday, ");
        if (tuesdayCheckbox.isSelected()) selectedDays.append("Tuesday, ");
        if (wednesdayCheckbox.isSelected()) selectedDays.append("Wednesday, ");
        if (thursdayCheckbox.isSelected()) selectedDays.append("Thursday, ");
        if (fridayCheckbox.isSelected()) selectedDays.append("Friday, ");
        if (selectedDays.length() > 0) selectedDays.setLength(selectedDays.length() - 2);

        OfficeHour record = new OfficeHour(year, semester, selectedDays.toString());
        dao.store(record);
        displayTableView();
    }

    private void displayTableView() {
        TableView<OfficeHour> table = new TableView<>();

        TableColumn<OfficeHour, String> yearCol = new TableColumn<>("Year");
        yearCol.setCellValueFactory(new PropertyValueFactory<>("year"));

        TableColumn<OfficeHour, String> semesterCol = new TableColumn<>("Semester");
        semesterCol.setCellValueFactory(new PropertyValueFactory<>("semester"));

        TableColumn<OfficeHour, String> daysCol = new TableColumn<>("Selected Days");
        daysCol.setCellValueFactory(new PropertyValueFactory<>("selectedDays"));

        table.getColumns().addAll(yearCol, semesterCol, daysCol);
        table.setItems(FXCollections.observableArrayList(dao.getAll()));
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        VBox container = new VBox(10, new Label("Faculty Office Hours Table:"), table);
        container.setPadding(new Insets(15));
        Scene tableScene = new Scene(container, 700, 400);

        Stage tableStage = new Stage();
        tableStage.setTitle("Office Hours Table");
        tableStage.setScene(tableScene);
        tableStage.show();
    }

    private void showAlert(String title, String header, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }

}