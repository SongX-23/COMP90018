package com.parse.unimelb;

import android.graphics.Bitmap;
import android.text.format.DateFormat;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by JOHANESG1508 on 10/13/2015.
 */
public class UserProfile {

    // Variables declaration
    private String username;
    private String profile_picture_url;
    private String userId;
    private String full_name;
    private String time;
    private Bitmap profile_picture_bmp;


    // Default constructor
    public UserProfile() {

        // Variables Initialization
        username=null;
        profile_picture_url=null;
        userId=null;
        full_name=null;
        time=null;
        profile_picture_bmp=null;

    }

    //
    public UserProfile(JSONObject jsonObject){

        // Variables Initialization
        try {
            username=jsonObject.getString("username");
            profile_picture_url=jsonObject.getString("username");
            userId=jsonObject.getString("username");
            full_name=jsonObject.getString("username");
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    // Variables setter & getter
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setProfile_picture_url(String profile_picture_url) {
        this.profile_picture_url = profile_picture_url;
    }

    public Bitmap getProfile_picture_bmp() {
        return profile_picture_bmp;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
    public String getFull_name() {
        return full_name;
    }

    public void setFull_name(String full_name) {
        this.full_name = full_name;
    }

    public String getProfile_picture_url() {
        return profile_picture_url;
    }

    public void setProfile_picture_bmp(Bitmap profile_picture_bmp) {
        this.profile_picture_bmp = profile_picture_bmp;
    }

}
