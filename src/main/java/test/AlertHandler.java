package test;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.util.concurrent.atomic.AtomicBoolean;

public class AlertHandler {
    public static void makeAlertWindow(Alert.AlertType alertType,String title,String headerText,String contentText) {
        if (alertType.equals(Alert.AlertType.INFORMATION)) {
            Alert alert = new Alert(alertType);
            alert.setTitle(title);
            alert.setHeaderText(headerText);
            alert.setContentText(contentText);
            alert.showAndWait();
        }
        else{

        }
    }
    public static boolean makeConfAlertWindow(Alert.AlertType alertType,String title,String headerText,String contentText){
        AtomicBoolean k = new AtomicBoolean(false);
        // Создание объекта Alert с типом Confirmation
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.setContentText(contentText);

        // Добавление кнопок "да" и "нет"
        alert.getButtonTypes().setAll(ButtonType.YES, ButtonType.NO);

        // Ожидание результата
        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.YES) {
                System.out.println("User clicked Yes");
                k.set(true);
            } else if (response == ButtonType.NO) {
                System.out.println("User clicked No");
                k.set(false);
            }
        });
        return k.get();
    }
}