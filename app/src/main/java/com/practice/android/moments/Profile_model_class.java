package com.practice.android.moments;


public class Profile_model_class {

    public String name;
    public String email;
/*
    public String Phone;
    public String about;
    public String relationship;
    private String Date_of_birth;
    private String gender;
    private String Photo;
*/

    // Default constructor required for calls to
    // DataSnapshot.getValue(User.class)
    public Profile_model_class() {
    }

    public Profile_model_class(String name,String email) {
//                             String phone,
//                               String about,
//                               String DOB,
//                               String Gender,
//                               String relationShip,
//                               String photo,

        this.name = name;
        this.email = email;
//        this.Phone = phone;
//
//        this.about = about;
//        this.Date_of_birth = DOB;
//        this.gender = Gender;
//        this.relationship = relationShip;
//        this.Photo = photo;
    }


    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

/*

    public String getPhoto() {
        return Photo;
    }

//    public String getPhone() {
//        return Phone;
//    }

    public String getrelationship() {
        return relationship;
    }

    public String getDate() {
        return Date_of_birth;
    }

    public String getAbout() {
        return about;
    }
*/
}
