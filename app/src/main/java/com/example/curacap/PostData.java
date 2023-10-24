package com.example.curacap;


public class PostData {
    private String title;
    private String caption;
    private String userID;
    private String username;
    private String imageUrl;
    private int likeCount;

    public PostData(){

    }

    public PostData(String title, String caption, String username, String userID, String imageUrl, int likeCount) {
        this.title = title;
        this.caption = caption;
        this.username = username;
        this.userID = userID;
        this.imageUrl = imageUrl;
        this.likeCount = likeCount;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getImage() {
        return imageUrl;
    }

    public void setImage(String image) {
        this.imageUrl = image;
    }

    public int getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(int likeCount) {
        this.likeCount = likeCount;
    }



}
