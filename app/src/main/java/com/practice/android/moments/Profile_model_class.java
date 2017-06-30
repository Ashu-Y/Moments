package com.practice.android.moments;


public class Profile_model_class {

    public String name;
    public String email;
    public String phone;
    public String about;
    public String relationship;
//    public String Date_of_birth;
//    public String gender;
//    public String Profile_Photo;


    public Profile_model_class() {
        }

    public Profile_model_class(String name, String email,String relationship,String about, String phone) {
//, String photo, String about, String relationship, String date_of_birth, String gender
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.about = about;
        this.relationship = relationship;
//        this.Date_of_birth = date_of_birth;
//        this.gender = gender;
//        this.Profile_Photo = photo;
    }

    public String getPhone() {
        return phone;
    }


    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public void setRelationship(String relationship) {
        this.relationship = relationship;
    }

    //
//
    public String getRelationship() {
        return relationship;
    }
//
//
//    public String getDate_of_birth() {
//        return Date_of_birth;
//    }
//
//
//    public String getGender() {
//        return gender;
//    }
//
//
//    public String getPhoto() {
//        return Profile_Photo;
//    }


    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }


}
