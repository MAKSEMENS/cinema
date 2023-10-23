package test;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import java.lang.IllegalArgumentException;

/**
 * @author Max
 * This is class for working with stage
 */
public class Controller {
    /**
     *This is a button for adding movie at list
     */
    @FXML
    private Button addMovieButton;
    /**
    * This is a button for name of a movie
    */
    @FXML
    private TextField movieNameField;
    /**
    *This is a button for movie choice
    * */
    @FXML
    private ComboBox<?> movieChoiceButton;
    /**
     * This is a ComboBox for showing movie list
     */
    @FXML
    private Button movieListButton;
    /**
     * This is a button for removing a movie
     */
    @FXML
    private Button removeMovieButton;
    /**
     * This is a button for save information and exit
     */
    @FXML
    private Button saveExitButton;
    /**
     *This is a button for searching movie by name
     */
    @FXML
    private Button searchMovieButton;
    /**
     *This is image for searching button
     */
    @FXML
    private ImageView searchPng;

    /**
     * Prints text to the console when a button is pressed
     */
    @FXML
    public void initialize(){
    searchMovieButton.setOnAction(event -> searchMovieBut());
    addMovieButton.setOnAction(actionEvent -> addMovieBut());
    movieListButton.setOnAction(actionEvent -> movieListBut());
    removeMovieButton.setOnAction(actionEvent -> removeMovieBut());
    saveExitButton.setOnAction(actionEvent -> saveExitBut());

    }

    private void addMovieBut(){
        System.out.println("Adding button..");
        final String[] movieName = new String[1];
        final String[] year = new String[1];
        final String[] genre = new String[1];
        final String[] director = new String[1];

        Stage newStage = new Stage();
        System.out.println("Adding Movie...");
        GridPane gridPane = new GridPane();
        gridPane.setPadding(new Insets(10));
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        Scene scene = new Scene(gridPane, 300, 200);

        // Create labels and text fields for each form
        Label nameLabel = new Label("Name:");
        TextField nameTextField = new TextField();

        Label yearLabel = new Label("Year of Creation:");
        TextField yearTextField = new TextField();

        Label genreLabel = new Label("Genre:");
        TextField genreTextField = new TextField();

        Label placeLabel = new Label("Director:");
        TextField placeTextField = new TextField();
        Button okButton = new Button("OK");

        // Add labels and text fields to the grid pane
        gridPane.add(nameLabel, 0, 0);
        gridPane.add(nameTextField, 1, 0);

        gridPane.add(yearLabel, 0, 1);
        gridPane.add(yearTextField, 1, 1);

        gridPane.add(genreLabel, 0, 2);
        gridPane.add(genreTextField, 1, 2);

        gridPane.add(placeLabel, 0, 3);
        gridPane.add(placeTextField, 1, 3);

        gridPane.add(okButton,1,4);

        okButton.setOnAction(event -> {
            try {
                movieName[0] = validateInput(nameTextField.getText(), "Name");
                year[0] = validateInput(yearTextField.getText(), "Year of Creation");
                genre[0] = validateInput(genreTextField.getText(), "Genre");
                director[0] = validateInput(placeTextField.getText(), "Director");
                int a = Integer.parseInt(year[0]);
                newStage.close();
            } catch (NumberFormatException nfe) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Wrong number format");
                alert.setHeaderText(null);
                alert.setContentText("Error: " + nfe.getMessage().toLowerCase());
                alert.showAndWait();
            }catch (IllegalArgumentException iae) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Input error");
                alert.setHeaderText(null);
                alert.setContentText(iae.getMessage());
                alert.showAndWait();
            }
        });

        newStage.setScene(scene);
        newStage.setTitle("Add Movie");
        newStage.show();
    }


    private void movieListBut(){
        System.out.println("Movie list button..");
    }

    private void removeMovieBut(){
        System.out.println("Remove movie button");
    }

    private void saveExitBut (){
        System.out.println("Save and exit button");
    }

    private void searchMovieBut(){
        System.out.println("Search movie button..");
    }

    public static class IllegalArgumentException extends Exception {

        public IllegalArgumentException(String message) {
            super(message);
        }
    }
    private String validateInput(String input, String fieldName) throws IllegalArgumentException {
        if (input.isEmpty()) {
            throw new IllegalArgumentException("Field " + fieldName + " is empty. Try again.");
        }
        return input;
    }
}

