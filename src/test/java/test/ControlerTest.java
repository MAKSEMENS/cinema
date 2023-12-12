package test;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.apache.commons.lang.RandomStringUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import java.time.LocalDate;

import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * This is Test Class which testing validate methods
 */
public class ControlerTest {

    private Controller appController;
    private String randomString;
    @BeforeEach
    void setUp() {
        appController = new Controller();
        randomString = RandomStringUtils.randomAlphabetic(10);
    }
    @Test
    public void testValidateInputNotEmpty() throws Controller.IllegalArgumentException {
        String input = "Непустая строка";
        String fieldName = "Тестовое поле";
        assertEquals(input, Controller.validateInput(input, fieldName));
    }

    @Test
    public void testValidateInputEmpty() {
        String input = "";
        String fieldName = "Тестовое поле";
        assertThrows(Controller.IllegalArgumentException.class, () -> Controller.validateInput(input, fieldName));
    }

    @Test
    public void testValidateInputEnglishCharacters() {
        String input = "EnglishCharacters";
        String fieldName = "Тестовое поле";
        assertThrows(Controller.IllegalArgumentException.class, () -> Controller.validateInput(input, fieldName));
    }

    @Test
    public void testValidateInputInvalidNumberFormat() {
        String input = "InvalidNumber";
        String fieldName = "Кол-во проданных билетов";
        assertThrows(NumberFormatException.class, () -> Controller.validateInput(input, fieldName));
    }

    @Test
    public void testValidateInputInvalidYearRange() {
        String input = "1800";
        String fieldName = "Год создания";
        assertThrows(NumberFormatException.class, () -> Controller.validateInput(input, fieldName));
    }

    @Test
    public void testValidateInputNegativeTicketCount() {
        String input = "-5";
        String fieldName = "Кол-во проданных билетов";
        assertThrows(NumberFormatException.class, () -> Controller.validateInput(input, fieldName));
    }

    @Test
    public void testValidateInputMovieNotEmpty() {
        // Test that the method doesn't throw an exception when inputs are not empty
        String text = "Non-empty text";
        String text1 = "Non-empty text1";
        String text2 = "Non-empty text2";
        String text3 = "Non-empty text3";
        Controller controller = new Controller(); // Assuming Controller is your class

        // Use try-catch block to capture any unexpected exceptions
        try {
            controller.validateInputMovie(text, text1, text2, text3);
            // If no exception is thrown, the test is successful
        } catch (IllegalArgumentException e) {
            Assertions.fail("Unexpected exception thrown: " + e.getMessage());
        } catch (Controller.IllegalArgumentException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testValidateInputMovieEmpty() {
        // Test that the method throws IllegalArgumentException when any input is empty
        String text = "Non-empty text";
        String text1 = "";
        String text2 = "Non-empty text2";
        String text3 = "Non-empty text3";
        Controller controller = new Controller(); // Assuming Controller is your class

        assertThrows(Controller.IllegalArgumentException.class,() -> controller.validateInputMovie(text, text1, text2, text3));
        // If the method doesn't throw an exception, the test fails
    }

    @Test
    public void testValidateListNotEmpty() {
        // Test that the method doesn't throw an exception when the list is not empty
        ObservableList<Movie> movieData = FXCollections.observableArrayList(new Movie());
        Controller controller = new Controller(); // Assuming Controller is your class



        // Use try-catch block to capture any unexpected exceptions
        try {
            controller.validateList(movieData);
            // If no exception is thrown, the test is successful
        } catch (Controller.IllegalArgumentException e) {
            Assertions.fail("Unexpected exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void testValidateListEmpty() {
        // Test that the method throws IllegalArgumentException when the list is empty
        ObservableList<Movie> emptyMovieData = FXCollections.observableArrayList();
        Controller controller = new Controller(); // Assuming Controller is your class

        assertThrows(Controller.IllegalArgumentException.class,()-> controller.validateList(emptyMovieData));
        // If the method doesn't throw an exception, the test fails
    }

    @Test
    public void testValidateInputSessionNotNull() {
        // Test that the method doesn't throw an exception when both values are not null
        LocalDate date = LocalDate.now();
        String value1 = "SomeValue";
        Controller controller = new Controller(); // Assuming Controller is your class

        // Use assertThrows to check for an exception
        try {
            controller.validateInputSession(date, value1);
            // If no exception is thrown, the test is successful
        } catch (Controller.IllegalArgumentException e) {
            fail("Unexpected exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void testValidateInputSessionNullValue() {
        // Test that the method throws IllegalArgumentException when the LocalDate value is null
        LocalDate date = null;
        String value1 = "SomeValue";
        Controller controller = new Controller(); // Assuming Controller is your class

        // Use assertThrows to check for an exception
        assertThrows(Controller.IllegalArgumentException.class, () -> controller.validateInputSession(date, value1));
    }

    @Test
    public void testValidateInputSessionNullValue1() {
        // Test that the method throws IllegalArgumentException when the String value1 is null
        LocalDate date = LocalDate.now();
        String value1 = null;
        Controller controller = new Controller(); // Assuming Controller is your class

        // Use assertThrows to check for an exception
        assertThrows(Controller.IllegalArgumentException.class, () -> controller.validateInputSession(date, value1));
    }

    @Test
    public void testValidateInputSessionBothNull() {
        // Test that the method throws IllegalArgumentException when both values are null
        LocalDate date = null;
        String value1 = null;
        Controller controller = new Controller(); // Assuming Controller is your class

        // Use assertThrows to check for an exception
        assertThrows(Controller.IllegalArgumentException.class, () -> controller.validateInputSession(date, value1));
    }
}

