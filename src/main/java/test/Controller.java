package test;

import groovyjarjarantlr.debug.Event;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.sql.DataSource;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.HashMap;
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
    @FXML
    private Button importXMLButton;
    @FXML
    private Button exportXMLButton;

    /**
     * Prints text to the console when a button is pressed
     */
    @FXML
    private ObservableList<Movie> data = FXCollections.observableArrayList();
    @FXML
    private ObservableList<Session> dataS = FXCollections.observableArrayList();
    @FXML
    private TableView<Movie> movieTableView;
    @FXML
    private TableColumn<Movie,String> movieNameColumn;
    @FXML
    private TableColumn<Movie,Integer> movieYearOfCreationColumn;
    @FXML
    private TableColumn<Movie,String> movieGenreColumn;
    @FXML
    private TableColumn<Movie,String> movieDirectorColumn;
    @FXML
    private TableColumn<Movie,Integer> movieId;
    List<Movie> movies;
    List<Session> sessions;
    private static final Logger logger = LogManager.getLogger("mainLogger");

    @FXML
    public void initialize(){
        logger.info("FXML file was loaded by initialize method");
        searchMovieButton.setOnAction(event -> searchMovieBut());
        addMovieButton.setOnAction(actionEvent -> addMovieBut());
        movieListButton.setOnAction(actionEvent -> movieListBut(data));
        removeMovieButton.setOnAction(actionEvent -> removeMovieBut());
        importXMLButton.setOnAction(event -> {
        try {
            importXML();
            logger.info("Import XML completed");
        } catch (ParserConfigurationException | SAXException | IOException e) {
            logger.error(e.getMessage(),e);
        }
    });

    exportXMLButton.setOnAction(event ->{
        try {
            exportXML(movies);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Success!");
            alert.setHeaderText(null);
            alert.setContentText("XML file {groups.xml} successfully exported!");
            alert.showAndWait();
            logger.info("Export XML completed");
        } catch (ParserConfigurationException | TransformerException | IOException e) {
            logger.error(e.getMessage(),e);
        }
    });

    saveExitButton.setOnAction(actionEvent -> {
        try {
            saveExitBut();
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
        }
    });

    searchMovieButton.setOnAction(event -> searchMovieBut());

    movieTableView.setOnMousePressed(event -> {
        if (event.isPrimaryButtonDown() && event.getClickCount()==2){
            Movie selectedMovie = movieTableView.getSelectionModel().getSelectedItem();
            if (selectedMovie != null) showDetails(selectedMovie);
        }
    });

    EntityManagerFactory emf = Persistence.createEntityManagerFactory("test_persistence");
    EntityManager em = emf.createEntityManager();
    em.getTransaction().begin();
    movies = em.createQuery("from Movie", Movie.class).getResultList();
    data.clear();
    data.addAll(movies);
    em.getTransaction().commit();

    movieListBut(data);
    }


    private void addMovieBut(){
        logger.info("Adding button..");
        final String[] movieName = new String[1];
        final String[] year = new String[1];
        final String[] genre = new String[1];
        final String[] director = new String[1];

        Stage newStage = new Stage();
        logger.info("Adding Movie...");
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
        logger.info("Adding window created");

        okButton.setOnAction(event -> {
            try {
                movieName[0] = validateInput(nameTextField.getText(), "Name");
                year[0] = validateInput(yearTextField.getText(), "Year of Creation");
                genre[0] = validateInput(genreTextField.getText(), "Genre");
                director[0] = validateInput(placeTextField.getText(), "Director");
                Movie mv  = new Movie();
                mv.setMovieName(movieName[0]);
                mv.setGenre(genre[0]);
                mv.setDirector(director[0]);
                mv.setYear(Integer.valueOf(year[0]));
                saveMovieToDB(mv);
                newStage.close();
                logger.info("Movie is created and added to DB");
            } catch (NumberFormatException nfe) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Wrong number format");
                alert.setHeaderText(null);
                alert.setContentText("Error: " + nfe.getMessage().toLowerCase());
                alert.showAndWait();
                logger.warn(nfe.getMessage(),nfe);
            }catch (IllegalArgumentException iae) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Input error");
                alert.setHeaderText(null);
                alert.setContentText(iae.getMessage());
                alert.showAndWait();
                logger.warn(iae.getMessage(),iae);
            }
        });

        newStage.setScene(scene);
        newStage.setTitle("Add Movie");
        newStage.show();
    }

    private void showDetails (Movie movie)
    {
        dataS.clear();
        dataS.addAll(movie.getSessions());

        Stage newStage = new Stage();

        TableView<Session> tableView = new TableView<Session>(dataS);
        Button addSessionButton = new Button("Добавить сеанс");


        TableColumn<Session, Integer> idColumn = new TableColumn<Session,Integer>("Номер");
        idColumn.setCellValueFactory(new PropertyValueFactory<>("sessionId"));
        tableView.getColumns().add(idColumn);

        TableColumn<Session, String> dateColumn = new TableColumn<Session,String>("Дата сеанса");
        dateColumn.setCellValueFactory(cellData -> cellData.getValue().dateOfSessionProperty());
        tableView.getColumns().add(dateColumn);

        TableColumn<Session, String> timeColumn = new TableColumn<Session,String>("Время сеанса");
        timeColumn.setCellValueFactory(cellData -> cellData.getValue().timeOfSessionProperty());
        tableView.getColumns().add(timeColumn);

        TableColumn<Session, Integer> countColumn = new TableColumn<Session,Integer>("Количество проданных билетов");
        countColumn.setCellValueFactory(new PropertyValueFactory<>("countOfSold"));
        tableView.getColumns().add(countColumn);
        tableView.prefHeightProperty().bind(newStage.heightProperty());
        tableView.prefWidthProperty().bind(newStage.widthProperty());

        idColumn.prefWidthProperty().bind(tableView.widthProperty().multiply(0.25));
        dateColumn.prefWidthProperty().bind(tableView.widthProperty().multiply(0.25));
        timeColumn.prefWidthProperty().bind(tableView.widthProperty().multiply(0.25));
        countColumn.prefWidthProperty().bind(tableView.widthProperty().multiply(0.25));


        GridPane gridPane = new GridPane();
        gridPane.add(addSessionButton,0,0);
        gridPane.add(tableView,0,1);

        Scene scene = new Scene(gridPane, 600, 250);

        newStage.setScene(scene);
        newStage.setTitle("Список сеансов для " + movie.getMovieName());
        newStage.show();
    }
    private void movieListBut(ObservableList <Movie> movies){
        movieTableView.setItems(movies);
        movieId.setCellValueFactory(new PropertyValueFactory<>("movieId"));
        movieNameColumn.setCellValueFactory(new PropertyValueFactory<>("movieName"));
        movieYearOfCreationColumn.setCellValueFactory(new PropertyValueFactory<>("year"));
        movieGenreColumn.setCellValueFactory(new PropertyValueFactory<>("genre"));
        movieDirectorColumn.setCellValueFactory(new PropertyValueFactory<>("director"));
        logger.info("Movie List is shown");
    }



    private void saveMovieToDB (Movie movie){
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("test_persistence");
        EntityManager em = emf.createEntityManager();

        logger.info("Saving new band to DataBase");

        em.getTransaction().begin();


        em.persist(movie);
        em.getTransaction().commit();
        initialize();

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Success!");
        alert.setHeaderText(null);
        alert.setContentText("Movie successfully added, " + "id is " + movie.getMovieId());
        alert.showAndWait();
    }
    private void removeMovieBut(){
        logger.info("Remove movie button is pressed");
    }

    public void importXML() throws ParserConfigurationException, IOException, SAXException {
        Stage chooseFileStage = new Stage();
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open XML File");
        File xml = fileChooser.showOpenDialog(chooseFileStage);

        DocumentBuilder dBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        Document document = dBuilder.parse(xml);

        document.getDocumentElement().normalize();

        NodeList moviesNodeList = document.getElementsByTagName("movie");

        for (int temp = 0; temp < moviesNodeList.getLength(); temp++) {
            Node elem = moviesNodeList.item(temp);
            NamedNodeMap attributes = elem.getAttributes();
            String name = attributes.getNamedItem("name").getNodeValue();
            String year = attributes.getNamedItem("year").getNodeValue();
            String genre = attributes.getNamedItem("genre").getNodeValue();
            String director = attributes.getNamedItem("director").getNodeValue();

            Movie movie = new Movie();
            movie.setMovieName(name);
            movie.setYear(Integer.valueOf(year));
            movie.setGenre(genre);
            movie.setDirector(director);
            saveMovieToDB(movie);
        }


        }

    public void exportXML(List<Movie> movieSaved) throws ParserConfigurationException, IOException, TransformerException {
        DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        Document document = builder.newDocument();
        Node movieList = document.createElement("movies");
        document.appendChild(movieList);
        for (Movie movie : movieSaved) {
            Element movieEl = document.createElement("movie");

            movieList.appendChild(movieEl);
            movieEl.setAttribute("name", movie.getMovieName());
            movieEl.setAttribute("year", movie.getYear().toString());
            movieEl.setAttribute("genre", movie.getGenre());
            movieEl.setAttribute("director", movie.getDirector());
        }
        Transformer trans = TransformerFactory.newInstance().newTransformer();
        try(FileWriter fileWriter = new FileWriter("movies.xml")) {
            logger.info("Creating XML file for export....");
            trans.transform(new DOMSource(document), new StreamResult(fileWriter));
        }
    }
    private void saveExitBut () throws Exception {
        logger.info("Save and exit button");
        logger.info("Creating PFD report...");
        new XMLtoPDFReporter().createReport("movies.XML");
    }

    private void searchMovieBut(){
        ObservableList<Movie> movies = FXCollections.observableArrayList();
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("test_persistence");
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        List<Movie> searchedMovies = em.createQuery("select m from Movie m where lower(m.movieName) = lower(:name)", Movie.class).setParameter("name", movieNameField.getText()).getResultList();
        movies.addAll(searchedMovies);
        movieListBut(movies);
        em.getTransaction().commit();
    }

    public static class IllegalArgumentException extends Exception {

        public IllegalArgumentException(String message) {
            super(message);
        }
    }
    public static String validateInput(String input, String fieldName) throws IllegalArgumentException {
        if (input.isEmpty()) {
            throw new IllegalArgumentException("Field " + fieldName + " is empty. Try again.");
        }
        if ((fieldName.equals("Year of Creation")  && !input.matches("-?\\d+(\\.\\d+)?"))) {
            throw new NumberFormatException("Field " + fieldName + " should be an integer!");
        }
        return input;
    }
}

