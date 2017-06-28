package com.practice.android.moments;

import android.net.Uri;

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
    public Uri Photo;


    // Default constructor required for calls to
    // DataSnapshot.getValue(User.class)
    public Profile_model_class() {
    }

    public Profile_model_class(String name, String phone, String about, String DOB, String Gender, String relationShip, Uri photo, String email) {
        this.name = name;
        this.email = email;
        this.Phone = phone;
        this.About = about;
        this.Date_of_birth = DOB;
        this.gender = Gender;
        this.relationship = relationShip;
        this.Photo = photo;
    }


    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }


    public Uri Photo() {
        return Photo;
    }
}
