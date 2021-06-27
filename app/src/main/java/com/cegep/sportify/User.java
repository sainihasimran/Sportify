package com.cegep.sportify;

public class User {

    public String userId;
    public String firstname, lastname;
    public String email,date;

    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public User(String email, String firstname, String lastname, String date) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.date = date;

    }
}
