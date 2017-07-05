package com.practice.android.moments.Models;

/**
 * Created by Hitesh Goel on 04-07-2017.
 * Made by Hitesh Goel
 */

public class Blog {

    private String title;
    private String description;
    private String pic;


    public Blog() {

    }


    public Blog(String title, String description, String pic) {
        this.title = title;
        this.description = description;
        this.pic = pic;
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
