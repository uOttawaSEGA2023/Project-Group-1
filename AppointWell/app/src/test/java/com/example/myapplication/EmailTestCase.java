package com.example.myapplication;

import static org.junit.Assert.assertTrue;

import android.widget.EditText;

import org.junit.Test;

import java.util.regex.Pattern;

public class EmailTestCase {
    @Test
    public void email_isCorrect() {
        assertTrue(emailIsValid("abc@gmail.com"));
    }


    private boolean emailIsValid(String input) {
        boolean valid;
        String regex = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@"
                + "[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";
        valid = (Pattern.compile(regex).matcher(input).matches());
        if (!valid) {
            valid=false;

        }

        return valid;
    }
}