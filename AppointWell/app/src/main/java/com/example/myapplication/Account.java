package com.example.myapplication;

public class Account {
    protected String email, password, type, status;
    public Account(){}

    public Account(String email, String password, String type, String status) {
        this.type = type;
        this.email = email;
        this.password = password;
        this.status = status;
    }
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStatus() { return status; }

    public void setStatus(String status) { this.status = status; }
}
