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
    public String thumbnailProfilephoto;
    public String thumbnailCoverPhoto;
    public String coverPhoto;
    public String userToken;

    public Profile_model_class() {
    }

    public Profile_model_class(String name, String email, String phone, String about, String relationship,
                               String date_of_birth, String photo, String gender,
                               String thumbnailProfilephoto, String thumbnailCoverPhoto,
                               String coverPhoto, String userToken) {
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.about = about;
        this.relationship = relationship;
        this.date_of_birth = date_of_birth;
        this.photo = photo;
        this.gender = gender;
        this.thumbnailProfilephoto = thumbnailProfilephoto;
        this.thumbnailCoverPhoto = thumbnailCoverPhoto;
        this.coverPhoto = coverPhoto;
        this.userToken = userToken;
    }

    public Profile_model_class(String name, String email, String phone,
                               String about, String relationship, String date_of_birth,
                               String photo, String gender, String thumbnailProfilephoto,
                               String thumbnailCoverPhoto, String coverPhoto) {
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.about = about;
        this.relationship = relationship;
        this.date_of_birth = date_of_birth;
        this.photo = photo;
        this.gender = gender;
        this.thumbnailProfilephoto = thumbnailProfilephoto;
        this.thumbnailCoverPhoto = thumbnailCoverPhoto;
        this.coverPhoto = coverPhoto;
    }

    public Profile_model_class(String name, String email, String phone, String about, String relationship, String date_of_birth, String photo, String gender, String coverPhoto) {
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.about = about;
        this.relationship = relationship;
        this.date_of_birth = date_of_birth;
        this.photo = photo;
        this.gender = gender;
        this.coverPhoto = coverPhoto;
    }

    public Profile_model_class(String name, String email, String relationship, String about, String phone, String gender, String photo, String date_of_birth) {

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

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public String getRelationship() {
        return relationship;
    }

    public void setRelationship(String relationship) {
        this.relationship = relationship;
    }

    public String getDate_of_birth() {
        return date_of_birth;
    }

    public void setDate_of_birth(String date_of_birth) {
        this.date_of_birth = date_of_birth;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getThumbnailProfilephoto() {
        return thumbnailProfilephoto;
    }

    public void setThumbnailProfilephoto(String thumbnailProfilephoto) {
        this.thumbnailProfilephoto = thumbnailProfilephoto;
    }

    public String getThumbnailCoverPhoto() {
        return thumbnailCoverPhoto;
    }

    public void setThumbnailCoverPhoto(String thumbnailCoverPhoto) {
        this.thumbnailCoverPhoto = thumbnailCoverPhoto;
    }

    public String getCoverPhoto() {
        return coverPhoto;
    }

    public void setCoverPhoto(String coverPhoto) {
        this.coverPhoto = coverPhoto;
    }

    public String getUserToken() {
        return userToken;
    }

    public void setUserToken(String userToken) {
        this.userToken = userToken;
    }
}
