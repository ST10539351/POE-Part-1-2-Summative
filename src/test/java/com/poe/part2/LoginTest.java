package com.poe.part2;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/** Part 1 unit tests, using the test data from the brief. */
class LoginTest {

    private Login login;

    @BeforeEach
    void setUp() {
        login = new Login();
        login.registerUser("Kyle", "Smith", "kyl_1", "Ch&&sec@ke99!", "+27838968976");
    }

    // ---------- assertEquals ----------

    @Test
    void usernameCorrectlyFormattedMessage() {
        assertEquals("Username successfully captured.", login.usernameMessage("kyl_1"));
    }

    @Test
    void usernameIncorrectlyFormattedMessage() {
        assertEquals("Username is not correctly formatted; please ensure that your username contains "
                + "an underscore and is no more than five characters in length.",
                login.usernameMessage("kyle!!!!!!!"));
    }

    @Test
    void passwordMeetsComplexityMessage() {
        assertEquals("Password successfully captured.", login.passwordMessage("Ch&&sec@ke99!"));
    }

    @Test
    void passwordDoesNotMeetComplexityMessage() {
        assertEquals("Password is not correctly formatted; please ensure that the password contains "
                + "at least eight characters, a capital letter, a number, and a special character.",
                login.passwordMessage("password"));
    }

    @Test
    void cellPhoneCorrectlyFormattedMessage() {
        assertEquals("Cell number successfully captured.", login.cellMessage("+27838968976"));
    }

    @Test
    void cellPhoneIncorrectlyFormattedMessage() {
        assertEquals("Cell number is incorrectly formatted or does not contain an international code; "
                + "please correct the number and try again.", login.cellMessage("08966553"));
    }

    @Test
    void registerUserReturnsUsernameMessageFirst() {
        assertEquals(Login.USERNAME_BAD,
                new Login().registerUser("A", "B", "kyle!!!!!!!", "Ch&&sec@ke99!", "+27838968976"));
    }

    @Test
    void registerUserReturnsPasswordMessage() {
        assertEquals(Login.PASSWORD_BAD,
                new Login().registerUser("A", "B", "kyl_1", "password", "+27838968976"));
    }

    @Test
    void registerUserSucceeds() {
        assertEquals("User has been registered successfully.",
                new Login().registerUser("A", "B", "kyl_1", "Ch&&sec@ke99!", "+27838968976"));
    }

    @Test
    void loginStatusMessages() {
        assertEquals("Welcome Kyle, Smith it is great to see you again.",
                login.returnLoginStatus("kyl_1", "Ch&&sec@ke99!"));
        assertEquals("Username or password incorrect, please try again.",
                login.returnLoginStatus("kyl_1", "wrong"));
    }

    // ---------- assertTrue / assertFalse ----------

    @Test
    void loginSuccessful() {
        assertTrue(login.loginUser("kyl_1", "Ch&&sec@ke99!"));
    }

    @Test
    void loginFailed() {
        assertFalse(login.loginUser("kyl_1", "wrongPassword1!"));
    }

    @Test
    void loginFailsBeforeAnyoneRegisters() {
        assertFalse(new Login().loginUser("kyl_1", "Ch&&sec@ke99!"));
    }

    @Test
    void usernameCorrectlyFormatted() {
        assertTrue(login.checkUserName("kyl_1"));
    }

    @Test
    void usernameIncorrectlyFormatted() {
        assertFalse(login.checkUserName("kyle!!!!!!!"));
    }

    @Test
    void passwordMeetsComplexity() {
        assertTrue(login.checkPasswordComplexity("Ch&&sec@ke99!"));
    }

    @Test
    void passwordDoesNotMeetComplexity() {
        assertFalse(login.checkPasswordComplexity("password"));
    }

    @Test
    void cellPhoneCorrectlyFormatted() {
        assertTrue(login.checkCellPhoneNumber("+27838968976"));
    }

    @Test
    void cellPhoneIncorrectlyFormatted() {
        assertFalse(login.checkCellPhoneNumber("08966553"));
    }
}
