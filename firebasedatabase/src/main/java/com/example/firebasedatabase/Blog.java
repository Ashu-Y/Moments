package com.example.firebasedatabase;

/**
 * Created by Hitesh Goel on 04-07-2017.
 * Made by Hitesh Goel
 */

public class Blog {

    private String title;
    private String description;
    private String photo;


    public Blog() {

    }


    public Blog(String title, String description, String photo) {
        this.title = title;
        this.description = description;
        this.photo = photo;
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

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }


}
