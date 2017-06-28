package com.practice.android.moments;

/**
 * Created by Hitesh Goel on 27-06-2017.
 * Made by Hitesh Goel
 */

public class Profile_model_class {

    public String name;
    public String email;
    public String Phone;
    public String About;
    public String Date_of_birth;
    public String gender;
    public String relationship;


    // Default constructor required for calls to
    // DataSnapshot.getValue(User.class)
    public Profile_model_class() {
    }

    public Profile_model_class(String name, String email) {
        this.name = name;
        this.email = email;
    }


    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }
}
