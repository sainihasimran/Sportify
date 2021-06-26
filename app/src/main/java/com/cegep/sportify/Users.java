package com.cegep.sportify;

public class Users {

    public String firstname, lastname;
    public String email;

    public Users() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public Users(String firstname,String lastname,String email) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;

    }
}
