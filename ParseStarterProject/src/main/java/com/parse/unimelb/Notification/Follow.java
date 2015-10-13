package com.parse.unimelb.notification;

import android.graphics.Bitmap;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by JOHANESG1508 on 10/8/2015.
 */
public class Follow {
    private String userName;
    private String full_Name;
    private String id;
    private String url_Image_Profile;
    private Bitmap image_Profile;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getFull_Name() {
        return full_Name;
    }

    public void setFull_Name(String full_Name) {
        this.full_Name = full_Name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUrl_Image_Profile() {
        return url_Image_Profile;
    }

    public void setUrl_Image_Profile(String url_Image_Profile) {
        this.url_Image_Profile = url_Image_Profile;
    }

    public Bitmap getImage_Profile() {
        return image_Profile;
    }

    public void setImage_Profile(Bitmap image_Profile) {
        this.image_Profile = image_Profile;
    }

    private String fullName;
    private String imgProfileURL;
    private String photoURL;
    private Bitmap imgProfile;
    private Bitmap photo;
    private String location;
    private String mediaID;
    private ArrayList<String> comment;
    private ArrayList<String> like;
    private double latitude;
    private double longitude;
    private double distance;
    private boolean recommended;

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public Follow(){

    }
    public Follow(JSONObject jsonObj){
        try {
            this.userName = jsonObj.getString("username");
            this.fullName = jsonObj.getString("full_name");
            this.imgProfileURL = jsonObj.getString("profile_picture");
            this.mediaID = jsonObj.getString("id");
        } catch (JSONException e) {
            e.printStackTrace();
            Log.d("Exception", e.getMessage());
        }

/*        this.imgProfileURL = imgProfileURL;
        this.photoURL = photoURL;
        this.location = location;
        this.comment = comment;
        this.like = like;*/

    }
    public Follow(
            String fullName,
            String imgProfileURL,
            String photoURL,
            String location,
            ArrayList<String> comment,
            ArrayList<String> like
    ) {
        this.fullName = fullName;
        this.imgProfileURL = imgProfileURL;
        this.photoURL = photoURL;
        this.location = location;
        this.comment = comment;
        this.like = like;
    }
    

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getUserProfileImgURL() {
        return imgProfileURL;
    }

    public void setUserProfileImgURL(String imgProfileURL) {
        this.imgProfileURL = imgProfileURL;
    }

    public String getPhotoURL() {
        return photoURL;
    }

    public void setPhotoURL(String photoURL) {
        this.photoURL = photoURL;
    }

    public Bitmap getUserProfileImg() {
        return imgProfile;
    }

    public void setUserProfileImg(Bitmap imgProfile) {
        this.imgProfile = imgProfile;
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

    public boolean isRecommended() {
        return recommended;
    }

    public void setRecommended(boolean recommended) {
        this.recommended = recommended;
    }

}
