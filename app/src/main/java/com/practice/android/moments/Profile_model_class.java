package com.practice.android.moments;


public class Profile_model_class {

    public String name;
    public String email;
    public String Phone;
    public String about;
    public String relationship;
    public String Date_of_birth;
    public String gender;
    public String Profile_Photo;

    // Default constructor required for calls to
    // DataSnapshot.getValue(User.class)
    public Profile_model_class() {
    }

    public Profile_model_class(String name, String email, String phone, String photo, String about, String relationship, String date_of_birth, String gender) {

        this.name = name;
        this.email = email;
        this.Phone = phone;
        this.about = about;
        this.relationship = relationship;
        this.Date_of_birth = date_of_birth;
        this.gender = gender;
        this.Profile_Photo = photo;
    }

    public String getPhone() {
        return Phone;
    }


    public String getAbout() {
        return about;
    }


    public String getRelationship() {
        return relationship;
    }


    public String getDate_of_birth() {
        return Date_of_birth;
    }


    public String getGender() {
        return gender;
    }


    public String getPhoto() {
        return Profile_Photo;
    }


    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }


}
