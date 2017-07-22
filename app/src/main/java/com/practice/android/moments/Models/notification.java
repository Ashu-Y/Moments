package com.practice.android.moments.Models;

/**
 * Created by Hitesh Goel on 21-07-2017.
 * Made by Hitesh Goel
 */

public class notification {

    String userid;
    String Status;

    public notification(String userid, String status) {
        this.userid = userid;
        Status = status;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }
}
