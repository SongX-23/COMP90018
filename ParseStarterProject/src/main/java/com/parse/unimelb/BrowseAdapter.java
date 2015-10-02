package com.parse.unimelb;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.ArrayList;

/**
 * Created by songxue on 2/10/2015.
 */
public class BrowseAdapter extends BaseAdapter{
    private Context mContext;
    private ArrayList<Feed> feed_array;

    public BrowseAdapter(Context c, ArrayList<Feed> data) {
        mContext = c;
        feed_array = data;
    }

    @Override
    public int getCount() {
        return feed_array.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
        View rowView = inflater.inflate(R.layout.feed, null, true);

        ImageView userProfileImg = (ImageView) rowView.findViewById(R.id.userProfileImageView);
        TextView userName = (TextView) rowView.findViewById(R.id.userNameTextView);
        TextView locationName = (TextView) rowView.findViewById(R.id.locationTextView);
        ImageView photoImg = (ImageView) rowView.findViewById(R.id.photoImageView);
        TextView likedText = (TextView) rowView.findViewById(R.id.likedTextView);
        TextView commentText = (TextView) rowView.findViewById(R.id.commentTextView);

        Feed oneFeed = feed_array.get(position);

        userProfileImg.setImageBitmap(oneFeed.getUserProfileImg());
        userName.setText(oneFeed.getDisplayName());
        locationName.setText(oneFeed.getLocation());
        photoImg.setImageBitmap(oneFeed.getPhoto());
        likedText.setText(oneFeed.getLike().toString());
        commentText.setText(oneFeed.getComment().toString());

        return rowView;
    }
}
