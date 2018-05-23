package com.app.feut.feut;

public class Helper {

    public static boolean isValidEmail(String email) {
        if (email.matches(".+@.+\\..+")) return true;
        return false;
    }
}
