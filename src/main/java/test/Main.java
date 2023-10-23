package test;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import static java.lang.String.valueOf;

public class Main extends Application {
    public static void main(String[] args) {
        launch(args);


    }
    @Override
    public void start(Stage primaryStage) throws Exception {
        // Загрузка FXML-файла из ресурсов
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/hello-view.fxml"));
        Parent root = loader.load();

        // Создание сцены
        Scene scene = new Scene(root, 700, 400);

        // Настройка и отображение окна
        primaryStage.setTitle("CINEMA");
        primaryStage.setScene(scene);
        primaryStage.show();



    }


}