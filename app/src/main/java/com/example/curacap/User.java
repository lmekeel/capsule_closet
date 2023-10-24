package com.example.curacap;

public class User {
    private String firstName;
    private String lastName;
    private String username;
    private Integer profilePic;

    private Integer followers;



    private Integer following;
    private Integer likesSaves;


    public User(String firstName, String lastName, String username, Integer profilePic, Integer followers, Integer following, Integer likesSaves) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.profilePic = profilePic;
        this.followers = followers;
        this.following = following;
        this.likesSaves = likesSaves;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Integer getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(Integer profilePic) {
        this.profilePic = profilePic;
    }

    public Integer getFollowers() {
        return followers;
    }

    public void setFollowers(Integer followers) {
        this.followers = followers;
    }

    public Integer getFollowing() {
        return following;
    }

    public void setFollowing(Integer following) {
        this.following = following;
    }

    public Integer getTotalLikes() {
        return likesSaves;
    }

    public void setTotalLikes(Integer totalLikes) {
        this.likesSaves = totalLikes;
    }





}
