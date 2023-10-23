package test;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;

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
}

