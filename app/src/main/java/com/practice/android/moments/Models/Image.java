package com.practice.android.moments.Models;

import java.io.Serializable;

/**
 * Created by Ashutosh on 7/12/2017.
 */

public class Image implements Serializable {

    String picName;
    String title;
    String description;
    String thumbnail_pic, medium, pic;

    public Image() {
    }

    public Image(String picName, String thumbnail_pic, String pic,
//                 String timestamp
                 String title,
                 String description) {
        this.picName = picName;
        this.thumbnail_pic = thumbnail_pic;
        this.pic = pic;
        this.title = title;
        this.description = description;

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

    public String getMedium() {
        return medium;
    }

    public void setMedium(String medium) {
        this.medium = medium;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }


}
