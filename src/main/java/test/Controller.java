package test;

import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleIntegerProperty;
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
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.stage.StageStyle;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

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
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

import javafx.scene.control.TableColumn;

import static java.lang.Boolean.valueOf;
import static test.DataBaseHandler.getDataFromDB;

/**
 * This is class for working with stage
 * @author Max
 *
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
    private ComboBox<String> movieChoiceButton;
    /**
     * This is a ComboBox for showing movie list
     */
    @FXML
    private Button movieListButton;
    /**
     * This is a button for removing a movie
     */
    @FXML
    private Button reportButton;
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
    private TableColumn<Movie, String> movieEndDateColumn;
    @FXML
    private TableColumn<Movie,String> movieInceptionDateColumn;
    @FXML
    private TableColumn<Movie, Number> lineNumberColumn;
    @FXML
    private TableColumn<Movie,String> movieIdColumn;
    @FXML
    private Button updateListButton;
    private Stage primaryStage;
    List<Movie> movies;
    List<Session> sessions;
    ObservableList<String> month = FXCollections.observableArrayList(
            "Январь", "Февраль", "Март", "Апрель",
            "Май", "Июнь", "Июль", "Август",
            "Сентябрь", "Октябрь", "Ноябрь", "Декабрь");
    private static final Logger logger = LogManager.getLogger("mainLogger");
    public File xml =null;
    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    @FXML
    public void initialize(){
        Thread exportXMLThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    exportXML(movies);
                    logger.info("Export XML completed");
                } catch (ParserConfigurationException | TransformerException | IOException e) {
                    logger.error(e.getMessage());
                }
            }

        });
        Thread convertReportThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    showReport();
                } catch (Exception ie ) {
                    logger.error(ie.getMessage());
                }
            }
        });
        Thread importXMLThread  = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    importXML(xml);
                } catch (ParserConfigurationException | SAXException | IOException e) {
                    logger.error(e.getMessage(),e);
                }
            }
        });
        exportXMLThread.setName("EXPORT_XML-THREAD");
        convertReportThread.setName("Convert_Report-Thread");
        importXMLThread.setName("Import_XML-Thread");
        updateListButton.setOnAction(event -> showByMonth());
        movieChoiceButton.setItems(month);
        logger.info("FXML file was loaded by initialize method");
        searchMovieButton.setOnAction(event -> showSearchedMovies());
        addMovieButton.setOnAction(actionEvent -> addMovieWindow());
        movieListButton.setOnAction(actionEvent -> showMovieList(data));
        reportButton.setOnAction(actionEvent -> {
            convertReportThread.start();
            try {
                convertReportThread.join();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });

        importXMLButton.setOnAction(event -> {
            try {
                xml = makeFile();
            } catch (ParserConfigurationException e) {
                throw new RuntimeException(e);
            }
            importXMLThread.start();


        });


    exportXMLButton.setOnAction(event ->{
        exportXMLThread.start();
        try {
            exportXMLThread.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Success!");
        alert.setHeaderText(null);
        alert.setContentText("XML file {groups.xml} successfully exported!");
        alert.showAndWait();
    });

    saveExitButton.setOnAction(actionEvent -> {
        try {
            saveExitBut();
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
        }
    });

    searchMovieButton.setOnAction(event -> showSearchedMovies());

    movieTableView.setOnMousePressed(event -> {
        if (event.isPrimaryButtonDown() && event.getClickCount()==2){
            Movie selectedMovie = movieTableView.getSelectionModel().getSelectedItem();
            if (selectedMovie != null) selectionForMovieWindow(selectedMovie);
        }
    });

    data.clear();
    getDataFromDB("test_persistence", data);
    movies = data;
    showMovieList(data);
    }


    public void selectionForSessionWindow(Session session, TableView<Session> sessionsTableView, Movie movie ){
        Stage primaryStage = new Stage();
        Button button1 = new Button("редактировать информацию о сеансе "  );
        Button button2 = new Button("Удалить сеанс");
        Button button3 = new Button("Выйти");

        // Создание вертикальной панели и добавление кнопок
        VBox vbox = new VBox(10); // 10 - вертикальный зазор между элементами
        vbox.setPadding(new Insets(20)); // Отступы вокруг панели
        vbox.getChildren().addAll(button1, button2, button3);

        // Создание сцены и установка ее на primaryStage
        Scene scene = new Scene(vbox, 300, 200);

        primaryStage.setTitle( "Панель выбора для сеанса" + session.getSessionId());
        primaryStage.setScene(scene);

        // Показать окно
        primaryStage.show();

        button1.setOnAction(event -> addSessionDialog(movie, primaryStage,dataS,sessionsTableView,1));
        button2.setOnAction(event ->{
            DataBaseHandler.removeSession(movie, sessionsTableView, dataS, "test_persistence");
            primaryStage.close();
        });
        button3.setOnAction(event -> primaryStage.close());


    }

    private void selectionForMovieWindow(Movie movie){

        Stage primaryStage = new Stage();
        Button button1 = new Button("Просмотреть список сеансов для фильма " );
        Button button2 = new Button("редактировать информацию о фильме "  );
        Button button3 = new Button("Удалить фильм");
        Button button4 = new Button("Выйти");

        primaryStage.initModality(Modality.WINDOW_MODAL);
        primaryStage.initOwner(this.primaryStage);

        // Создание вертикальной панели и добавление кнопок
        VBox vbox = new VBox(10); // 10 - вертикальный зазор между элементами
        vbox.setPadding(new Insets(20)); // Отступы вокруг панели
        vbox.getChildren().addAll(button1, button2, button3, button4);

        // Создание сцены и установка ее на primaryStage
        Scene scene = new Scene(vbox, 300, 200);

        primaryStage.setTitle( "Панель выбора для фильма" + movie.getMovieName());
        primaryStage.setScene(scene);

        // Показать окно
        primaryStage.show();

        button1.setOnAction(event -> showSessions(movie, primaryStage));
        button2.setOnAction(event -> editMovieWindow(movie, primaryStage));
        button3.setOnAction(event ->{
            DataBaseHandler.removeMovie(movie);
            primaryStage.close();
            initialize();
        });
        button4.setOnAction(event -> primaryStage.close());


    }


    private void editMovieWindow(Movie movie, Stage primaryStage){

        Map<String, String> parameters = new HashMap<>();

        parameters.put("movieName", movie.getMovieName());
        parameters.put("year", movie.getYear().toString());
        parameters.put("director", movie.getDirector());
        parameters.put("genre", movie.getGenre());
        parameters.put("inceptionDate", movie.inceptionDateProperty().getValue());
        parameters.put("finalDate", movie.finalDateProperty().getValue());

        Stage dialogStage = new Stage();
        dialogStage.initModality(Modality.WINDOW_MODAL);
        dialogStage.initModality(Modality.WINDOW_MODAL);
        dialogStage.initOwner(this.primaryStage);

        GridPane dialogGrid = new GridPane();
        dialogGrid.setPadding(new Insets(20));
        dialogGrid.setHgap(10);
        dialogGrid.setVgap(10);

        TextField title = new TextField(parameters.get("movieName"));
        TextField year = new TextField(parameters.get("year"));
        TextField director = new TextField(parameters.get("director"));
        TextField genre = new TextField(parameters.get("genre"));
        DatePicker beginPicker = new DatePicker(LocalDate.parse(parameters.get("inceptionDate"),DateTimeFormatter.ISO_DATE));
        DatePicker endPicker = new DatePicker(LocalDate.parse(parameters.get("finalDate"),DateTimeFormatter.ISO_DATE));

        dialogGrid.add(new Label("Название фильма: "),0,0);
        dialogGrid.add(title,1,0);
        dialogGrid.add(new Label("Год выпуска: "), 0, 1);
        dialogGrid.add(year,1,1);
        dialogGrid.add(new Label("Режиссёр: "), 0,2);
        dialogGrid.add(director, 1 , 2);
        dialogGrid.add(new Label("Жанр:"), 0,3);
        dialogGrid.add(genre, 1,3);
        dialogGrid.add(new Label("Начальная дата:"), 0,4);
        dialogGrid.add(beginPicker, 1,4);
        dialogGrid.add(new Label("Конечная дата:"), 0,5);
        dialogGrid.add(endPicker, 1,5);

        Button editButton = new Button("Применить изменения: ");
        dialogGrid.add(editButton, 1, 6);

        editButton.setOnAction(event -> {
            try {
                validateInputMovie(title.getText(), year.getText(), director.getText(), genre.getText());

                movie.setMovieName(title.getText());
                movie.setYear(Integer.parseInt(year.getText()));
                movie.setGenre(genre.getText());
                movie.setDirector(director.getText());
                movie.setInceptionDate(beginPicker.getValue());
                movie.setFinalDate(endPicker.getValue());

                Map<String, String> newValues = new HashMap<>();
                newValues.put("movieName",title.getText());
                newValues.put("year",year.getText());
                newValues.put("genre", genre.getText());
                newValues.put("director", director.getText());
                newValues.put("inceptionDate",movie.inceptionDateProperty().getValue());
                newValues.put("finalDate", movie.finalDateProperty().getValue());
                DataBaseHandler.editDataMovie(movie.getMovieId(), newValues,"test_persistence");
                data.clear();
                getDataFromDB("test_persistence", data);
                movieTableView.setItems(data);
                dialogStage.close();
            }
            catch (IllegalArgumentException iae) {
                AlertHandler.makeAlertWindow(Alert.AlertType.ERROR, "Error!", null, "Fill all the fields!");
                logger.warn("Tried to edit movie, but some fields are empty");
            }

        });

        Scene dialogScene = new Scene(dialogGrid, 350, 300);
        dialogStage.setScene(dialogScene);
        dialogStage.showAndWait();
    }



    private void addMovieWindow(){
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
        Scene scene = new Scene(gridPane, 300, 300);

        // Create labels and text fields for each form
        Label nameLabel = new Label("Название:");
        TextField nameTextField = new TextField();

        Label yearLabel = new Label("Год создания:");
        TextField yearTextField = new TextField();

        Label genreLabel = new Label("Жанр");
        TextField genreTextField = new TextField();

        Label directorLabel = new Label("Режиссёр");
        TextField placeTextField = new TextField();

        Label beginLabel = new Label("Начальная дата:");
        DatePicker beginDatePicker = new DatePicker();

        Label endLabel = new Label("Конечная дата:");
        DatePicker endDatePicker = new DatePicker();

        endDatePicker.setEditable(false);
        beginDatePicker.setEditable(false);

        Button okButton = new Button("OK");

        // Add labels and text fields to the grid pane
        gridPane.add(nameLabel, 0, 0);
        gridPane.add(nameTextField, 1, 0);

        gridPane.add(yearLabel, 0, 1);
        gridPane.add(yearTextField, 1, 1);

        gridPane.add(genreLabel, 0, 2);
        gridPane.add(genreTextField, 1, 2);

        gridPane.add(directorLabel, 0, 3);
        gridPane.add(placeTextField, 1, 3);

        gridPane.add(beginLabel,0, 4);
        gridPane.add(beginDatePicker, 1, 4);

        gridPane.add(endLabel,0, 5);
        gridPane.add(endDatePicker, 1, 5);



        gridPane.add(okButton,1,6);
        logger.info("Adding window created");

        okButton.setOnAction(event -> {
            try {
                movieName[0] = validateInput(nameTextField.getText(), "Название");
                year[0] = validateInput(yearTextField.getText(), "Год создания");
                genre[0] = validateInput(genreTextField.getText(), "Жанр");
                director[0] = validateInput(placeTextField.getText(), "Режиссёр");
                Movie mv  = new Movie();
                mv.setMovieName(movieName[0]);
                mv.setGenre(genre[0]);
                mv.setDirector(director[0]);
                mv.setYear(Integer.valueOf(year[0]));
                mv.setInceptionDate(beginDatePicker.getValue());
                mv.setFinalDate(endDatePicker.getValue());
                DataBaseHandler.saveMovieToDB(mv);
                initialize();
                newStage.close();
                logger.info("Movie is created and added to DB");
            } catch (NumberFormatException nfe) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Неправильный формат для числового поля");
                alert.setHeaderText(null);
                alert.setContentText("Ошибка: " + nfe.getMessage().toLowerCase());
                alert.showAndWait();
                logger.warn(nfe.getMessage(),nfe);
            }catch (IllegalArgumentException iae) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Ошибка ввода");
                alert.setHeaderText(null);
                alert.setContentText(iae.getMessage());
                alert.showAndWait();
                logger.warn(iae.getMessage(),iae);
            }
        });


        newStage.setScene(scene);
        newStage.setTitle("Добавление фильма");
        newStage.show();

    }


    private void addSessionDialog(Movie movie, Stage primaryStage, ObservableList<Session> SessionsData, TableView<Session> sessionsTableView, int param) {
        logger.info("open session dialog");
        Session session;
        if (param == 1) session = sessionsTableView.getSelectionModel().getSelectedItem();
        else {
            session = null;
        }
        logger.info("get movie if it possible");
        Map<String, String> parameters = new HashMap<>();
        if (session != null) {
            parameters.put("countOfSold", session.getCountOfSold().toString());
            parameters.put("date", session.getSessionDate().toString());
            parameters.put("time", session.getSessionTime().toString());
        }

        Stage dialogStage = new Stage();
        dialogStage.initModality(Modality.WINDOW_MODAL);
        dialogStage.initOwner(primaryStage);

        GridPane dialogGrid = new GridPane();
        dialogGrid.setPadding(new Insets(20));
        dialogGrid.setHgap(10);
        dialogGrid.setVgap(10);

        TextField countOfSoldField;
        DatePicker dateOfSessionPicker;
        Spinner<Integer> hourSpinner ;
        Spinner<Integer> minuteSpinner ;

        if (!parameters.isEmpty()) {
            countOfSoldField = new TextField(parameters.get("countOfSold"));
            dateOfSessionPicker = new DatePicker(LocalDate.parse(parameters.get("date")));
            hourSpinner = new Spinner<>(0,23,(LocalTime.parse(parameters.get("time"),DateTimeFormatter.ofPattern("HH:mm"))).getHour());
            minuteSpinner = new Spinner<>(0,59,(LocalTime.parse(parameters.get("time"),DateTimeFormatter.ofPattern("HH:mm"))).getMinute());
        }
        else {
            countOfSoldField = new TextField();
            dateOfSessionPicker = new DatePicker();
            dateOfSessionPicker.setEditable(false);
            hourSpinner = new Spinner<>(8, 23, 0);
            minuteSpinner = new Spinner<>(0, 59, 0);
        }
        HBox timePicker = new HBox(3,  hourSpinner, new Label(":"), minuteSpinner);

        dialogGrid.add(new Label("Кол-во проданных билетов"), 0, 0);
        dialogGrid.add(countOfSoldField, 1, 0);
        dialogGrid.add(new Label("Дата сеанса"), 0, 1);
        dialogGrid.add(dateOfSessionPicker, 1, 1);
        dialogGrid.add(new Label("Время сеанса"), 0, 2);
        dialogGrid.add(timePicker, 1, 2);

        Button addButton = new Button("Add");

        int selectedHour = hourSpinner.getValue();
        int selectedMinute = minuteSpinner.getValue();
        addButton.setOnAction(e -> {
            try {
                validateInputSession( dateOfSessionPicker.getValue(), LocalTime.of(selectedHour, selectedMinute, 0).toString());
                if (session != null) {
                    session.setCountOfSold(Integer.parseInt(validateInput(countOfSoldField.getText(), "Кол-во проданных билетов")));
                    session.setSessionDate(dateOfSessionPicker.getValue());
                    session.setSessionTime(LocalTime.of(selectedHour, selectedMinute, 0));
                    Map<String, String> newValues = new HashMap<>();
                    newValues.put("countOfSold",countOfSoldField.getText());
                    newValues.put("date",dateOfSessionPicker.getValue().toString());
                    newValues.put("time", LocalTime.of(selectedHour, selectedMinute, 0).toString());
                    DataBaseHandler.editDataSession(session.getSessionId(),newValues,"test_persistence");
                    logger.info("added session and saved to DB");
                }
                else {
                    validateInput(countOfSoldField.getText(), "Кол-во проданных билетов");
                    Session newSession = new Session();
                    newSession.setCountOfSold(Integer.parseInt(countOfSoldField.getText()));
                    newSession.setSessionDate(dateOfSessionPicker.getValue());
                    newSession.setSessionTime(LocalTime.of(selectedHour, selectedMinute, 0));
                    DataBaseHandler.saveSessionToDB(movie, newSession);
                }
                SessionsData.clear();
                SessionsData.addAll(movie.getSessions());
                sessionsTableView.setItems(SessionsData);
                dialogStage.close();
            }
            catch (IllegalArgumentException iae) {
                AlertHandler.makeAlertWindow(Alert.AlertType.ERROR, "Ошибка!", null, "Заполните ввсе поля!");
                logger.warn("Tried to add session, but some fields are empty");
            }catch (NumberFormatException nfe) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Неправильный формат для числового поля");
                alert.setHeaderText(null);
                alert.setContentText("Ошибка: " + nfe.getMessage().toLowerCase());
                alert.showAndWait();
                logger.warn(nfe.getMessage(),nfe);}
        });

        dialogGrid.add(addButton, 1, 3);

        Scene dialogScene = new Scene(dialogGrid, 350, 200);
        dialogStage.setScene(dialogScene);
        dialogStage.showAndWait();
    }


    private void showSessions (Movie movie, Stage parentStage)
    {
        logger.info("showing sessions");
        dataS.clear();
        dataS.addAll(movie.getSessions());

        Stage newStage = new Stage();
        newStage.initModality(Modality.WINDOW_MODAL);
        newStage.initStyle(StageStyle.UNIFIED);
        newStage.initOwner(parentStage);

        TableView<Session> tableView = new TableView<Session>(dataS);
        Button addSessionButton = new Button("Добавить сеанс");


        TableColumn<Session, Integer> lineNumberColumn = new TableColumn<Session,Integer>("№");
        lineNumberColumn.setCellValueFactory(param ->
                Bindings.createIntegerBinding(() ->
                                tableView.getItems().indexOf(param.getValue()) + 1,
                        movieTableView.getItems()
                ).asObject()
        );
        tableView.getColumns().add(lineNumberColumn);

        TableColumn<Session, String> dateColumn = new TableColumn<Session,String>("Дата сеанса");
        dateColumn.setCellValueFactory(cellData -> cellData.getValue().dateOfSessionProperty());
        tableView.getColumns().add(dateColumn);

        TableColumn<Session, String> timeColumn = new TableColumn<Session,String>("Время сеанса");
        timeColumn.setCellValueFactory(cellData -> cellData.getValue().timeOfSessionProperty());
        tableView.getColumns().add(timeColumn);

        TableColumn<Session, Integer> countColumn = new TableColumn<Session,Integer>("Кол-во проданных билетов");
        countColumn.setCellValueFactory(new PropertyValueFactory<>("countOfSold"));
        tableView.getColumns().add(countColumn);
        tableView.prefHeightProperty().bind(newStage.heightProperty());
        tableView.prefWidthProperty().bind(newStage.widthProperty());

        TableColumn<Session, Integer> idColumn = new TableColumn<Session,Integer>("Номер");
        idColumn.setCellValueFactory(new PropertyValueFactory<>("sessionId"));
        tableView.getColumns().add(idColumn);

        lineNumberColumn.prefWidthProperty().bind(tableView.widthProperty().multiply(0.07));
        dateColumn.prefWidthProperty().bind(tableView.widthProperty().multiply(0.22));
        timeColumn.prefWidthProperty().bind(tableView.widthProperty().multiply(0.22));
        countColumn.prefWidthProperty().bind(tableView.widthProperty().multiply(0.32));
        idColumn.prefWidthProperty().bind(tableView.widthProperty().multiply(0.18));


        GridPane gridPane = new GridPane();
        gridPane.add(addSessionButton,0,0);
        gridPane.add(tableView,0,1);

        Scene scene = new Scene(gridPane, 600, 250);

        newStage.setScene(scene);
        newStage.setTitle("Список сеансов для " + movie.getMovieName());
        newStage.show();

        String[] lables = {"Дата сеанса","Время сеанса","Кол-во проданных билетов"};
        List<String> lablesList = Arrays.asList(lables);
        addSessionButton.setOnAction(event -> addSessionDialog(movie, newStage,dataS,tableView , 0));
        tableView.setOnMousePressed(event -> {
            if (event.isPrimaryButtonDown() && event.getClickCount()==2){
                Session selectedSession = tableView.getSelectionModel().getSelectedItem();
                if (selectedSession != null) selectionForSessionWindow(selectedSession, tableView, movie);
            }
        });
    }
    private void showMovieList(ObservableList <Movie> movies){
        logger.info("showing movies");
        movieTableView.setItems(movies);
        lineNumberColumn.setCellValueFactory(param -> new SimpleIntegerProperty(movieTableView.getItems().indexOf(param.getValue()) + 1));
        movieIdColumn.setCellValueFactory(new PropertyValueFactory<>("movieId"));
        movieNameColumn.setCellValueFactory(new PropertyValueFactory<>("movieName"));
        movieYearOfCreationColumn.setCellValueFactory(new PropertyValueFactory<>("year"));
        movieGenreColumn.setCellValueFactory(new PropertyValueFactory<>("genre"));
        movieDirectorColumn.setCellValueFactory(new PropertyValueFactory<>("director"));
        movieInceptionDateColumn.setCellValueFactory((cellData -> cellData.getValue().inceptionDateProperty()));
        movieEndDateColumn.setCellValueFactory((cellData -> cellData.getValue().finalDateProperty()));
        logger.info("Movie List is shown");
    }



    private void showReport() throws Exception {
        logger.info("Save and exit button");
        logger.info("Creating PFD report...");
        new XMLtoPDFReporter().createReport("D:\\GAMES\\JetBrains\\cinema\\movies.xml");
    }

    public File makeFile() throws ParserConfigurationException {
        logger.info("importing xml file");
        Stage chooseFileStage = new Stage();
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open XML File");
        File xml = fileChooser.showOpenDialog(chooseFileStage);
        Document document;
        return xml;
    }
    public void importXML(File xml) throws ParserConfigurationException, IOException, SAXException {

        Document document;
        DocumentBuilder dBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        if (xml!=null) {
            document = dBuilder.parse(xml);
        }
        else{
            logger.warn("Null file. End of importing");
            return;
        }

        document.getDocumentElement().normalize();

        NodeList moviesNodeList = document.getElementsByTagName("movie");

        for (int temp = 0; temp < moviesNodeList.getLength(); temp++) {
            Node elem = moviesNodeList.item(temp);
            NamedNodeMap attributes = elem.getAttributes();
            String name = attributes.getNamedItem("name").getNodeValue();
            String year = attributes.getNamedItem("year").getNodeValue();
            String genre = attributes.getNamedItem("genre").getNodeValue();
            String director = attributes.getNamedItem("director").getNodeValue();
            String incDate = attributes.getNamedItem("movie_inceptionDate").getNodeValue();
            String endDate = attributes.getNamedItem("movie_finalDate").getNodeValue();

            Movie movie = new Movie();
            movie.setMovieName(name);
            movie.setYear(Integer.valueOf(year));
            movie.setGenre(genre);
            movie.setDirector(director);
            movie.setInceptionDate(LocalDate.parse(incDate,DateTimeFormatter.ISO_DATE));
            movie.setFinalDate(LocalDate.parse(endDate, DateTimeFormatter.ISO_DATE));
            DataBaseHandler.saveMovieToDB(movie);
            logger.info("Movie is created and added to DB");
        }
        initialize();
        logger.info("Import XML completed");

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
            movieEl.setAttribute("movie_inceptionDate", movie.inceptionDateProperty().getValue());
            movieEl.setAttribute("movie_finalDate", movie.finalDateProperty().getValue());
        }
        Transformer trans = TransformerFactory.newInstance().newTransformer();
        try(FileWriter fileWriter = new FileWriter("movies.xml")) {
            logger.info("Creating XML file for export....");
            trans.transform(new DOMSource(document), new StreamResult(fileWriter));
        }
    }
    private void saveExitBut () throws Exception {
        System.out.println("Exit button..");
        Platform.exit();

    }

    private void showSearchedMovies(){
        ObservableList<Movie> movie_data = FXCollections.observableArrayList();
        movies = DataBaseHandler.getSearchedByNameMovies(movieNameField.getText());
        movie_data.addAll(movies);
        try{
            validateList(movie_data);
            showMovieList(movie_data);
            logger.info("show searched movies");
        } catch (IllegalArgumentException e) {
            AlertHandler.makeAlertWindow(Alert.AlertType.ERROR, "Ошибка!", null, "Фильмоы не найдены");
            logger.error(e.getMessage(),e);
        }

    }


    private void showByMonth() {
        logger.info("showing movies by chosen month");
        ObservableList<Movie> movie_data = FXCollections.observableArrayList();
        String month = movieChoiceButton.getValue();
        List<Movie> filtredMovies = DataBaseHandler.getSearchedByMonthMovies(month);
        movie_data.addAll(filtredMovies);
        showMovieList(movie_data);
    }

    /**
     * This is exception class for working with user inputs
     */
    public static class IllegalArgumentException extends Exception {

        public IllegalArgumentException(String message) {
            super(message);
        }
    }
    public static void validateList(ObservableList<Movie> movie_data) throws IllegalArgumentException{
        if (movie_data.isEmpty()){
            throw new IllegalArgumentException("Список филмов пуст.");
        }
    }

    public static String validateInput(String input, String fieldName) throws IllegalArgumentException {
        if (input.isEmpty()) {
            throw new IllegalArgumentException("Поле " + fieldName + " пустое. Попробуйте ещё раз.");
        }
        if (((fieldName.equals("Год создания") || fieldName.equals("Кол-во проданных билетов"))&& !input.matches("-?\\d+(\\.\\d+)?"))) {

            throw new NumberFormatException("Поле " + fieldName + " должно быть целочисленным");
        }
        if (((fieldName.equals("Год создания"))&& ( Integer.parseInt(input)<1850 || Integer.parseInt(input)>2027))) {
            throw new NumberFormatException("Фильмов с таким годов выпуска не существует");
        }
        if ((fieldName.equals("Кол-во проданных билетов")&& Integer.parseInt(input)<0 )) {

            throw new NumberFormatException("Количество не может быть отрицательным");
        }

        if (input.matches(".*[a-zA-Z].*")){
            throw new IllegalArgumentException("Используйте только русский язык");
        }
        return input;
    }
    public void validateInputMovie(String text, String text1, String text2, String text3) throws IllegalArgumentException {
        if (text.isEmpty() || text1.isEmpty() || text2.isEmpty() || text3.isEmpty()) {
            throw new IllegalArgumentException("Пустое поле. Попробуйте ещё раз.");
        }
    }

    public void validateInputSession( LocalDate value,String value1) throws IllegalArgumentException {
        if ( value== null || value1== null) {
            throw new IllegalArgumentException("Пустое поле. Попробуйте ещё раз.");
        }
    }


}

