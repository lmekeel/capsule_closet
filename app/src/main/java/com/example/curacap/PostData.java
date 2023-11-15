package com.example.curacap;


import java.util.Map;

public class PostData {


    private String postId;
    private String title;
    private String caption;
    private String userID;
    private String username;
    private String imageUrl;
    private int likeCount;
    private boolean liked;


    private Map<String, Boolean> likedByUsers;

    public PostData(){

    }

    public PostData(String title, String caption, String username, String userID, String imageUrl, int likeCount, String postId, boolean liked) {
        this.title = title;
        this.caption = caption;
        this.username = username;
        this.userID = userID;
        this.imageUrl = imageUrl;
        this.likeCount = likeCount;
        this.postId = postId;
        this.liked = liked;

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

    public boolean isLiked() {
        return liked;
    }

    public void setLiked(boolean liked) {
        this.liked = liked;
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }
    public Map<String, Boolean> getLikedByUsers() {
        return likedByUsers;
    }

    public void setLikedByUsers(Map<String, Boolean> likedByUsers) {
        this.likedByUsers = likedByUsers;
    }
    @Override
    public String toString() {
        return "PostData{" +
                "postId='" + postId + '\'' +
                ", title='" + title + '\'' +
                ", caption='" + caption + '\'' +
                ", userID='" + userID + '\'' +
                ", username='" + username + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                ", likeCount=" + likeCount +
                ", liked=" + liked +
                ", likedByUsers=" + likedByUsers +
                '}';
    }
}
