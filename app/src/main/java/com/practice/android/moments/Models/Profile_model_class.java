package com.practice.android.moments.Models;


public class Profile_model_class {

    public String name;
    public String email;
    public String phone;
    public String about;
    public String relationship;
    public String date_of_birth;
    public String photo;
    public String gender;



    public Profile_model_class() {
    }

    public Profile_model_class(String name, String email, String relationship, String about, String phone,String gender,String photo, String date_of_birth) {

        this.name = name;
        this.email = email;
        this.phone = phone;
        this.about = about;
        this.relationship = relationship;
        this.date_of_birth = date_of_birth;
        this.gender = gender;
        this.photo = photo;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getRelationship() {
        return relationship;
    }

    public String getDate_of_birth() {
        return date_of_birth;
    }

    public String getPhoto() {
        return photo;
    }

    public String getGender() {
        return gender;
    }

    public String getPhone() {
        return phone;
    }


    public String getAbout() {
        return about;
    }


}
