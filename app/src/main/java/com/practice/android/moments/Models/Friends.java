package com.practice.android.moments.Models;

/**
 * Created by Hitesh Goel on 23-07-2017.
 * Made by Hitesh Goel
 */

public class Friends {


    String status;
    String userName;

    public Friends() {
    }

    public Friends(String status, String userName) {
        this.status = status;
        this.userName = userName;
    }


    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
