package com.practice.android.moments.Models;

import java.io.Serializable;

/**
 * Created by Ashutosh on 7/12/2017.
 */

public class Image implements Serializable {

    private String title;
    private String description;
    private String name;
    private String small, medium, large;
    private String timestamp;

    public Image() {
    }

    public Image(String picName, String thumbnail_pic, String pic,
//                 String timestamp
                 String title,
                 String description) {
        this.name = picName;
        this.small = thumbnail_pic;
//        this.medium = medium;
        this.large = pic;
        this.title = title;
        this.description = description;

//        this.timestamp = timestamp;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSmall() {
        return small;
    }

    public void setSmall(String small) {
        this.small = small;
    }

    public String getMedium() {
        return medium;
    }

    public void setMedium(String medium) {
        this.medium = medium;
    }

    public String getLarge() {
        return large;
    }

    public void setLarge(String large) {
        this.large = large;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}
