package test;
import org.apache.commons.lang.RandomStringUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
public class ControlerTest {
    private Controller appController;
    private String randomString;
    @BeforeEach
    void setUp() {
        appController = new Controller();
        randomString = RandomStringUtils.randomAlphabetic(10);
    }
    @Test
    void validateInput_emptyFieldName() {
        Assertions.assertThrows(Controller.IllegalArgumentException.class, () -> Controller.validateInput("","Name"));
    }
    @Test
    void validateInput_emptyFieldGenre() {
        Assertions.assertThrows(Controller.IllegalArgumentException.class, () -> Controller.validateInput("","Genre"));
    }
    @RepeatedTest(5)
    void validateInput_NaNYear() {
        Assertions.assertThrows(NumberFormatException.class, () -> Controller.validateInput(randomString, "Year of Creation"));

    }
}
