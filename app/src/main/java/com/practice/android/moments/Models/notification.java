package com.practice.android.moments.Models;

/**
 * Created by Hitesh Goel on 21-07-2017.
 * Made by Hitesh Goel
 */

public class notification {

    String frienduserid;
    String userimageid;
    String status;

    public notification() {
    }

    public notification(String frienduserid, String userimageid, String status) {
        this.frienduserid = frienduserid;
        this.userimageid = userimageid;
        this.status = status;
    }

    public String getFrienduserid() {
        return frienduserid;
    }

    public void setFrienduserid(String frienduserid) {
        this.frienduserid = frienduserid;
    }

    public String getUserimageid() {
        return userimageid;
    }

    public void setUserimageid(String userimageid) {
        this.userimageid = userimageid;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
