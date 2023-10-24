package com.example.myapplication;

import java.util.ArrayList;

public class Administrator extends Account {
    private Administrator admin;


    private Administrator(){
        super("Adminpassword@2023", "admin@gmail.com", "Administrator", "Approved");
        admin = new Administrator();
    }

    public Administrator getAdmin(){
        if (admin == null) {
            admin = new Administrator();
        }
        return admin;
    }
}
