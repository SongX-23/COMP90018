package com.parse.unimelb;

import android.graphics.Bitmap;

/**
 * Created by songxue on 12/10/2015.
 */
public class DiscoverUser {
    private String fullname;


    private String username;
    private String location;
    private String gender;
    private String imgURL;
    private Bitmap profileImage;

    public DiscoverUser() {

    }

    public DiscoverUser(String username, String location, String gender, String imgURL, Bitmap profileImage) {
        this.username = username;
        this.location = location;
        this.gender = gender;
        this.imgURL = imgURL;
        this.profileImage = profileImage;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }
    public Bitmap getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(Bitmap profileImage) {
        this.profileImage = profileImage;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getImgURL() {
        return imgURL;
    }

    public void setImgURL(String imgURL) {
        this.imgURL = imgURL;
    }


}
