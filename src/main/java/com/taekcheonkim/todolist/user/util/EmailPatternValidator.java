package com.taekcheonkim.todolist.user.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EmailPatternValidator {
    public boolean isValidEmail(String email) {
        String regex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
}
