package com.example.myapplication;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import android.widget.EditText;

import org.junit.Test;

public class SignUpActivityTest {

    @Test
    public void number_isCorrect() {
        assertTrue( numberIsValid("3439871131"));
    }

    private boolean numberIsValid(String phoneNum) {
        boolean valid = true;
        if (phoneNum.length() != 10) {
            valid = false;
            if (phoneNum.length() == 0) {
            } else {
            }
        } else {
            for (int i = 0; i < 10; i++) {
                if (phoneNum.charAt(i) < 48 || phoneNum.charAt(i) > 57) {
                    valid = false;

                    break;
                }
            }
        }
        return valid;
    }
}
