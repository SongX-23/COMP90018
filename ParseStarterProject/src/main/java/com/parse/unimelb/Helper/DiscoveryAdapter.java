package com.parse.unimelb.Helper;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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

    private List<ParseUser> users;
    private Context mContext;

    public DiscoveryAdapter (Context c, List<ParseUser> data){
        mContext = c;
        users = data;
    }

    public void setUsers(List<ParseUser> users) {
        this.users = users;
    }

    @Override
    public int getCount() {
        return 0;
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
        //ParseUser userDetails = users.get(position);
        final ImageView userProfileImg = (ImageView) rowView.findViewById(R.id.ProfilePic);
        TextView userName = (TextView) rowView.findViewById(R.id.UserName);

        ParseFile imageFile = (ParseFile) users.get(0).get("Image");
        if (imageFile != null) {
            imageFile.getDataInBackground(new GetDataCallback() {
                public void done(byte[] data, ParseException e) {
                    if (e == null) {
                        Bitmap bitmapImage = BitmapFactory.decodeByteArray(data, 0, data.length);
                        userProfileImg.setImageBitmap(bitmapImage);
                    } else {
                        // something went wrong
                    }
                }
            });
        }

      //  userProfileImg.setImageBitmap(users.getUserProfileImg());
       // userName.setText(users.getDisplayName());


        return null;
    }



}
