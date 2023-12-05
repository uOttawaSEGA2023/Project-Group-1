package com.example.myapplication;

import static org.junit.Assert.assertTrue;


import org.junit.Test;


public class PasswordTestCase {
    @Test
    public void password_isCorrect() {
        assertTrue(passwordIsValid("@BC#123abc"));
    }
    protected boolean passwordIsValid(String input) {
        boolean valid = true;
        char c;
        boolean specialChar = false;
        boolean uppercase = false;
        boolean lowercase = false;
        boolean num = false;

        if (input.length() < 9) {
            if (input.trim().length() == 0) {
                return false;
            }
            valid = false;
        } else {
            for (int i = 0; i < input.length(); i++) {
                c = input.charAt(i);
                if (c >= 65 && c <= 90) {
                    uppercase = true;
                } else {
                    if (c >= 97 && c <= 122) {
                        lowercase = true;
                    } else {
                        if (c >= 48 && c <= 57) {
                            num = true;
                        } else {
                            if ((c >= 33 && c <= 47) || (c >= 58 && c <= 64) || (c >= 91 && c <= 96) || (c >= 123 && c <= 126)) {
                                specialChar = true;
                            } else {
                                valid = false;
                               break;
                            }
                        }
                    }
                }

            }
            if (!(uppercase && lowercase && specialChar && num)) {
                valid = false;
                String errorMessage = "";
                if (!uppercase) {
                    errorMessage = errorMessage + "Password should contain an uppercase alphabet. ";
                }
                if (!lowercase) {
                    errorMessage = errorMessage + "Password should contain a lowercase alphabet. ";
                }
                if (!num) {
                    errorMessage = errorMessage + "Password should contain a number. ";
                }
                if (!specialChar) {
                    errorMessage = errorMessage + "Password should contain a symbol.";
                }
            }
        }


        return valid;
    }

}
