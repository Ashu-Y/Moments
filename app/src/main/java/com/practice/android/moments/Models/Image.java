package com.practice.android.moments.Models;

import java.io.Serializable;

/**
 * Created by Ashutosh on 7/12/2017.
 */

public class Image implements Serializable {

    String title;
    String description;
    String picName;
    String medium;
    String thumbnail_pic;
    String user_id;
    String pic;
    String userToken;
    String userName;


    public Image() {
    }

    public Image(String title, String description,
                 String picName, String medium,
                 String thumbnail_pic, String user_id,
                 String pic, String userToken, String userName) {

        this.title = title;
        this.description = description;
        this.picName = picName;
        this.medium = medium;
        this.thumbnail_pic = thumbnail_pic;
        this.user_id = user_id;
        this.pic = pic;
        this.userToken = userToken;
        this.userName = userName;
    }


    public Image(String picName, String title,
                 String description, String thumbnail_pic,
                 String user_id, String pic,
                 String userToken, String userName) {
        this.picName = picName;
        this.title = title;
        this.description = description;
        this.thumbnail_pic = thumbnail_pic;
        this.user_id = user_id;
        this.pic = pic;
        this.userToken = userToken;
        this.userName = userName;
    }

    public String getMedium() {
        return medium;
    }

    public void setMedium(String medium) {
        this.medium = medium;
    }

    public String getPicName() {
        return picName;
    }

    public void setPicName(String picName) {
        this.picName = picName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getThumbnail_pic() {
        return thumbnail_pic;
    }

    public void setThumbnail_pic(String thumbnail_pic) {
        this.thumbnail_pic = thumbnail_pic;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getUserToken() {
        return userToken;
    }

    public void setUserToken(String userToken) {
        this.userToken = userToken;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }


}
