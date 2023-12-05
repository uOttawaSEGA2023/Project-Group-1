package com.example.myapplication;

import static org.junit.Assert.assertTrue;

import android.widget.EditText;

import org.junit.Test;


public class AddressTestCase {
    @Test
    public void address_isCorrect() {
        assertTrue(addressIsValid("Rideau Street"));
    }
    protected boolean addressIsValid(String input) {
        if (input.length() == 0) {
            //  input.setError("Field cannot be empty.");
            return false;
        }
        return true;
    }

}
