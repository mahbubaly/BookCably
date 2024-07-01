package com.bookcably;



public class User {
    private String username;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;

   

    public User(String username, String firstName, String lastName, String email, String phone) {
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phone = phone;
    }

    public User(String string, String string1, String string2) {
    }

    public String getUsername() {
        return username;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }
}
