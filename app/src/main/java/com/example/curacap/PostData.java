package com.example.curacap;


public class PostData {
    private String title;
    private String username;
    private Integer image;
    private int likeCount;

    public PostData(String title, String username, Integer image, int likeCount) {
        this.title = title;
        this.username = username;
        this.image = image;
        this.likeCount = likeCount;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Integer getImage() {
        return image;
    }

    public void setImage(Integer image) {
        this.image = image;
    }

    public int getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(int likeCount) {
        this.likeCount = likeCount;
    }



}
