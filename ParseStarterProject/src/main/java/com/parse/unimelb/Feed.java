package com.parse.unimelb;

import android.graphics.Bitmap;
import android.widget.ImageView;

import java.util.ArrayList;

/**
 * Created by songxue on 2/10/2015.
 */
public class Feed {
    private String displayName;
    private String userProfileImgURL;
    private String photoURL;
    private Bitmap userProfileImg;
    private Bitmap photo;
    private String location;
    private String mediaID;
    private ArrayList<String> comment;
    private ArrayList<String> like;

    public Feed(String displayName, String userProfileImgURL, String photoURL, String location, ArrayList<String> comment, ArrayList<String> like) {
        this.displayName = displayName;
        this.userProfileImgURL = userProfileImgURL;
        this.photoURL = photoURL;
        this.location = location;
        this.comment = comment;
        this.like = like;
    }
    public Feed(){

    }

    public String getDisplayName() {

        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getUserProfileImgURL() {
        return userProfileImgURL;
    }

    public void setUserProfileImgURL(String userProfileImgURL) {
        this.userProfileImgURL = userProfileImgURL;
    }

    public String getPhotoURL() {
        return photoURL;
    }

    public void setPhotoURL(String photoURL) {
        this.photoURL = photoURL;
    }

    public Bitmap getUserProfileImg() {
        return userProfileImg;
    }

    public void setUserProfileImg(Bitmap userProfileImg) {
        this.userProfileImg = userProfileImg;
    }

    public Bitmap getPhoto() {
        return photo;
    }

    public void setPhoto(Bitmap photo) {
        this.photo = photo;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public ArrayList<String> getComment() {
        return comment;
    }

    public void setComment(ArrayList<String> comment) {
        this.comment = comment;
    }

    public ArrayList<String> getLike() {
        return like;
    }

    public void setLike(ArrayList<String> like) {
        this.like = like;
    }

    public String getMediaID() {
        return mediaID;
    }

    public void setMediaID(String mediaID) {
        this.mediaID = mediaID;
    }

}
