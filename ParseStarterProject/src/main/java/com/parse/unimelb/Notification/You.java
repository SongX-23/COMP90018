package com.parse.unimelb;

import android.graphics.Bitmap;
import android.graphics.YuvImage;

/**
 * Created by JOHANESG1508 on 10/13/2015.
 */
public class You {
    // Variables declaration
    private boolean activityType;   // true for new user-following, false for new image-like
    private Bitmap likedImage;
    private String time;
    private UserProfile userProfile;

    // Constructors
    // Default Constructor
    public You () {
        likedImage=null;
        time=null;
        userProfile=null;
    }

    public You(UserProfile aUser, boolean type) {
        userProfile=aUser;
        activityType=type;
    }


    // Variables setter & getter
    public boolean isActivityType() {
        return activityType;
    }

    public void setActivityType(boolean activityType) {
        this.activityType = activityType;
    }

    public Bitmap getLikedImage() {
        return likedImage;
    }

    public void setLikedImage(Bitmap likedImage) {
        this.likedImage = likedImage;
    }

    public UserProfile getUserProfile() {
        return userProfile;
    }

    public void setUserProfile(UserProfile userProfile) {
        this.userProfile = userProfile;
    }

    public String getTime() {
        return time;
/*        long foo = Long.parseLong(time)*1000;
        Date date = new Date(foo);
        DateFormat formatter = new SimpleDateFormat("MMMM dd,yyyy");
        return (formatter.format(date));*/
    }

    public void setTime(String time) {
        this.time = time;
    }


}
