package com.practice.android.moments.Models;

/**
 * Created by Hitesh Goel on 03-07-2017.
 * Made by Hitesh Goel
 */

public class TImeLine_Images {

    String pic;
    String description;

    public TImeLine_Images() {
    }


    public TImeLine_Images(String description, String pic) {
        this.pic = pic;
        this.description = description;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
