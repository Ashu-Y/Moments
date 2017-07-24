package com.practice.android.moments.Models;

/**
 * Created by Ashutosh on 7/14/2017.
 */

public class SearchModel {

    String userid;
    String username;
    String profilePic;
    String userToken;

    public SearchModel(String userid, String username, String profilePic, String userToken) {
        this.userid = userid;
        this.username = username;
        this.profilePic = profilePic;
        this.userToken = userToken;
    }


    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }

    public String getUserToken() {
        return userToken;
    }

    public void setUserToken(String userToken) {
        this.userToken = userToken;
    }
}
