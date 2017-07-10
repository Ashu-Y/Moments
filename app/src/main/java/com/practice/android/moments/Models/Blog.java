package com.practice.android.moments.Models;

/**
 * Created by Hitesh Goel on 04-07-2017.
 * Made by Hitesh Goel
 */

public class Blog {

    private String title;
    private String description;
    private String pic;
    private String thumbnail_pic;
    private String userName;
    private String picName;
    private String user_id;

    public Blog() {

    }

    public Blog(String title, String description, String pic, String thumbnail_pic, String userName, String user_id, String picName) {
        this.title = title;
        this.description = description;
        this.pic = pic;
        this.thumbnail_pic = thumbnail_pic;
        this.userName = userName;
        this.user_id = user_id;
        this.picName = picName;
    }

    public String getPicName() {
        return picName;
    }

    public void setPicName(String picName) {
        this.picName = picName;
    }

    public String getThumbnail_pic() {

        return thumbnail_pic;
    }

    public void setThumbnail_pic(String thumbnail_pic) {
        this.thumbnail_pic = thumbnail_pic;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
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

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }
}
