package test;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.time.LocalTime;

public class DateTimePickerApp extends Application {

    @Override
    public void start(Stage stage) {
        VBox root = new VBox(10);

        // Date Picker
        DatePicker datePicker = new DatePicker();
        datePicker.setValue(LocalDate.now());

        // Time Picker (using Spinner for hours, minutes, and seconds)
        Spinner<Integer> hourSpinner = new Spinner<>(0, 23, 0);
        Spinner<Integer> minuteSpinner = new Spinner<>(0, 59, 0);
        Spinner<Integer> secondSpinner = new Spinner<>(0, 59, 0);

        Label timeLabel = new Label("Time:");
        HBox timePicker = new HBox(5, timeLabel, hourSpinner, new Label(":"), minuteSpinner, new Label(":"), secondSpinner);

        // Button to get the selected date and time
        Button submitButton = new Button("Submit");
        submitButton.setOnAction(e -> {
            LocalDate selectedDate = datePicker.getValue();
            int selectedHour = hourSpinner.getValue();
            int selectedMinute = minuteSpinner.getValue();
            int selectedSecond = secondSpinner.getValue();

            LocalTime selectedTime = LocalTime.of(selectedHour, selectedMinute, selectedSecond);

            System.out.println("Selected Date: " + selectedDate);
            System.out.println("Selected Time: " + selectedTime);
        });

        root.getChildren().addAll(datePicker, timePicker, submitButton);

        Scene scene = new Scene(root, 300, 200);

        stage.setTitle("DateTime Picker");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}