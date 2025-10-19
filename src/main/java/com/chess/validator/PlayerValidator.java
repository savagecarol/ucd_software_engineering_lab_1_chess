package com.chess.validator;

public class PlayerValidator {

    private static final int MAX_NAME_LENGTH = 20;
    private static final int MIN_PASSWORD_LENGTH = 6;


    public static String validateName(String name) {
        if (name == null || name.trim().isEmpty()) {
            return "Player name cannot be empty.";
        }
        if (name.length() > MAX_NAME_LENGTH) {
            return "Player name too long. Maximum " + MAX_NAME_LENGTH + " characters allowed.";
        }
        return null;
    }

    public static String validatePassword(String password) {
        if (password == null || password.trim().isEmpty()) {
            return "Password cannot be empty.";
        }
        if (password.length() < MIN_PASSWORD_LENGTH) {
            return "Password too short. Minimum " + MIN_PASSWORD_LENGTH + " characters required.";
        }
        return null;
    }


    public static boolean isValid(String name, String password) {
        String nameError = validateName(name);
        String passwordError = validatePassword(password);
        if (nameError != null) {
            System.err.println(" ▶ " + nameError);
            return false;
        }
        if (passwordError != null) {
            System.err.println(" ▶ " + passwordError);
            return false;
        }
        return true;
    }
}
