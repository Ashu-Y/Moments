package com.practice.android.moments.Models;

/**
 * Created by Ashutosh on 6/15/2017.
 */

public class Post {

    public String username;
    public int imageId;
    public int likeId;
    public int commentId;

    public Post(String username, int imageId, int likeId, int commentId) {
        this.username = username;
        this.imageId = imageId;
        this.likeId = likeId;
        this.commentId = commentId;

    }



    public Post(String gautam, int c1) {
    }


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getImageId() {
        return imageId;
    }



//    public int getLikeId() {
//        return likeId;
//    }
//
//    public void setLikeId(int likeId) {
//        this.likeId = likeId;
//    }
//
//    public int getCommentId() {
//        return commentId;
//    }
//
//    public void setCommentId(int commentId) {
//        this.commentId = commentId;
//    }

}
