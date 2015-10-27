package com.teamawesome.geese.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static android.text.TextUtils.isEmpty;

/**
 * Created by lcolam on 10/20/15.
 */
public class Utilities {

    private static final String USERNAME_PATTERN = "^[A-Za-z0-9]{3,15}$";
    private static final String PASSWORD_PATTERN = "^[a-zA-Z0-9~!@.#$%^&*_&\\\\]{8,20}$";

    public static boolean isValidUsername(final String username) {
        if (username == null) {
            return false;
        }
        Pattern pattern = Pattern.compile(USERNAME_PATTERN);
        Matcher matcher = pattern.matcher(username);
        return matcher.matches();
    }

    public static boolean isValidEmail(final String email) {
        if (email == null) {
            return false;
        }
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    public static boolean isValidPassword(final String password) {
        if (password == null) {
            return false;
        }
        Pattern pattern = Pattern.compile(PASSWORD_PATTERN);
        Matcher matcher = pattern.matcher(password);
        return (matcher.matches()
                && getNumberOfDigits(password) >= 1
                && !password.toLowerCase().equals(password));
    }

    public static int getNumberOfDigits(final String inString){
        if (isEmpty(inString)) {
            return 0;
        }
        int numDigits= 0;
        int length= inString.length();
        for (int i = 0; i < length; i++) {
            if (Character.isDigit(inString.charAt(i))) {
                numDigits++;
            }
        }
        return numDigits;
    }
}
