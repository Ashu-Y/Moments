package com.practice.android.moments.Models;

/**
 * Created by Hitesh Goel on 12-07-2017.
 * Made by Hitesh Goel
 */

public class Comment {


    String comment;
    String user_id;

    public Comment() {
    }

    public Comment(String comment, String user_id) {

        this.comment = comment;
        this.user_id = user_id;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }
}
