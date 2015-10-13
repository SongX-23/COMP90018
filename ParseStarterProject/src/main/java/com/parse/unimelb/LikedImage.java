package com.parse.unimelb;

import android.graphics.Bitmap;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

/**
 * Created by JOHANESG1508 on 10/13/2015.
 */
public class LikedImage {
    // Variables declaration
    private UserProfile postBy;
    private Date created_time;
    private String imageUrl;
    private Bitmap imageBmp;

    // Constructors
    // Default Constructor
    public LikedImage() {

    }

    public LikedImage(JSONObject jsonObject) {
        try {
            postBy=new UserProfile(jsonObject.getJSONObject("user"));
            created_time=Converter.longStrToDate(jsonObject.getString("created_time"));
            JSONObject images=jsonObject.getJSONObject("images");
            JSONObject thumbnail=images.getJSONObject("thumbnail");
            imageUrl=thumbnail.getString("url");
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }



    public UserProfile getPostBy() {
        return postBy;
    }

    public void setPostBy(UserProfile postBy) {
        this.postBy = postBy;
    }

    public Date getCreated_time() {
        return created_time;
    }

    public void setCreated_time(Date created_time) {
        this.created_time = created_time;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Bitmap getImageBmp() {
        return imageBmp;
    }

    public void setImageBmp(Bitmap imageBmp) {
        this.imageBmp = imageBmp;
    }

}
