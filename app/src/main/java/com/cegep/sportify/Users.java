package com.cegep.sportify;

public class Users {

    public String firstname, lastname;
    public String email,date;

    public Users() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public Users(String email,String firstname,String lastname,String date) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.date = date;

    }
}
