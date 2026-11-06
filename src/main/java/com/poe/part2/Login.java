package com.poe.part2;

import java.util.regex.Pattern;

public class Login {

    // Messages from the Part 1 test-data table (these are the ones the unit tests check).
    public static final String USERNAME_OK = "Username successfully captured.";
    public static final String USERNAME_BAD = "Username is not correctly formatted; please ensure that your "
            + "username contains an underscore and is no more than five characters in length.";
    public static final String PASSWORD_OK = "Password successfully captured.";
    public static final String PASSWORD_BAD = "Password is not correctly formatted; please ensure that the "
            + "password contains at least eight characters, a capital letter, a number, and a special character.";
    public static final String CELL_OK = "Cell number successfully captured.";
    public static final String CELL_BAD = "Cell number is incorrectly formatted or does not contain an "
            + "international code; please correct the number and try again.";
    public static final String REGISTER_OK = "User has been registered successfully.";
    public static final String LOGIN_FAILED = "Username or password incorrect, please try again.";

    // International code (+27) followed by the number, no more than ten digits after the code.
    private static final Pattern CELL_PATTERN = Pattern.compile("^\\+27\\d{1,10}$");

    // Details stored when the user registers.
    private String firstName;
    private String lastName;
    private String registeredUsername;
    private String registeredPassword;
    private String registeredCell;
    private boolean registered = false;

    /** The username must contain an underscore and be no more than five characters long. */
    public boolean checkUserName(String username) {
        return username != null && username.contains("_") && username.length() <= 5;
    }

    /** At least eight characters, a capital letter, a number and a special character. */
    public boolean checkPasswordComplexity(String password) {
        if (password == null || password.length() < 8) {
            return false;
        }
        boolean hasCapital = false;
        boolean hasNumber = false;
        boolean hasSpecial = false;
        for (char c : password.toCharArray()) {
            if (Character.isUpperCase(c)) {
                hasCapital = true;
            } else if (Character.isDigit(c)) {
                hasNumber = true;
            } else if (!Character.isLetter(c) && !Character.isWhitespace(c)) {
                hasSpecial = true;
            }
        }
        return hasCapital && hasNumber && hasSpecial;
    }

    /** International country code followed by the number (no more than ten characters). */
    public boolean checkCellPhoneNumber(String cellPhoneNumber) {
        return cellPhoneNumber != null && CELL_PATTERN.matcher(cellPhoneNumber).matches();
    }

    public String usernameMessage(String username) {
        return checkUserName(username) ? USERNAME_OK : USERNAME_BAD;
    }

    public String passwordMessage(String password) {
        return checkPasswordComplexity(password) ? PASSWORD_OK : PASSWORD_BAD;
    }

    public String cellMessage(String cellPhoneNumber) {
        return checkCellPhoneNumber(cellPhoneNumber) ? CELL_OK : CELL_BAD;
    }

    /** Registers the user if every condition is met, otherwise says which one failed. */
    public String registerUser(String firstName, String lastName, String username,
                               String password, String cellPhoneNumber) {
        if (!checkUserName(username)) {
            return USERNAME_BAD;
        }
        if (!checkPasswordComplexity(password)) {
            return PASSWORD_BAD;
        }
        if (!checkCellPhoneNumber(cellPhoneNumber)) {
            return CELL_BAD;
        }
        this.firstName = firstName;
        this.lastName = lastName;
        this.registeredUsername = username;
        this.registeredPassword = password;
        this.registeredCell = cellPhoneNumber;
        this.registered = true;
        return REGISTER_OK;
    }

    /** True only if the details match the ones stored at registration. */
    public boolean loginUser(String username, String password) {
        return registered
                && registeredUsername.equals(username)
                && registeredPassword.equals(password);
    }

    public String returnLoginStatus(String username, String password) {
        if (loginUser(username, password)) {
            return "Welcome " + firstName + ", " + lastName + " it is great to see you again.";
        }
        return LOGIN_FAILED;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }
}
