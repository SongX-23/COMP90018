package com.parse.unimelb;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.parse.GetDataCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;
import com.parse.unimelb.Feed;
import com.parse.unimelb.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rahul on 10/11/2015.
 */
public class DiscoveryAdapter extends BaseAdapter{

    private ArrayList<DiscoverUser> users;
    private Context mContext;
    private ImageView userProfileImg;
    private TextView userName;
    private TextView gender;
    private ParseFile imageFile;

    public DiscoveryAdapter (Context c, ArrayList<DiscoverUser> data){
        mContext = c;
        users = data;
    }

    public void setUsers(ArrayList<DiscoverUser> users) {
        this.users = users;
    }

    @Override
    public int getCount() {
        return users.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

           View rowView = inflater.inflate(R.layout.discovery, null, true);
           DiscoverUser userDetails = users.get(position);
           userProfileImg = (ImageView) rowView.findViewById(R.id.ProfilePic);
           userName = (TextView) rowView.findViewById(R.id.UserName);
          gender = (TextView) rowView.findViewById(R.id.Gender);
        String[] userNameParts = userDetails.getUsername().toString().split("@");
           userName.setText(userNameParts[0]);
           String userGender = userDetails.getLocation().toString();

        if((userGender != null) || (userGender != "Blank")) {
            gender.setText(userGender);
        }
        Bitmap profileImage = userDetails.getProfileImage();
        if (profileImage != null) {
            userProfileImg.setImageBitmap(profileImage);
        }else{
            Drawable myDrawable = rowView.getResources().getDrawable(R.drawable.default_profile_image);
            Bitmap defaultImage = ((BitmapDrawable) myDrawable).getBitmap();
            userProfileImg.setImageBitmap(defaultImage);
        }

        return rowView;
    }



}
