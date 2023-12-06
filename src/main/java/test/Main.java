package test;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Main extends Application {
    private static final Logger logger = LogManager.getLogger("mainLogger");
    public static void main(String[] args) {
        logger.info("Application started");
        launch(args);

    }
    @Override
    public void start(Stage primaryStage) throws Exception {
        // Загрузка FXML-файла из ресурсов
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/hello-view.fxml"));
        Parent root = loader.load();

        Controller controller = loader.getController();
        controller.setPrimaryStage(primaryStage);

        // Создание сцены
        Scene scene = new Scene(root, 700, 400);

        // Настройка и отображение окна
        primaryStage.setTitle("CINEMA");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();

        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent t) {
                logger.info("Application closed");
                Platform.exit();
                System.exit(0);
            }
        });
    }
}