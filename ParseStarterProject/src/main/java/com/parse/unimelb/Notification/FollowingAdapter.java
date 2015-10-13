package com.parse.unimelb.notification;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.parse.unimelb.R;

import java.util.ArrayList;

/**
 * Created by JOHANESG1508 on 10/8/2015.
 */
public class FollowingAdapter extends BaseAdapter {
    private Context mContext;

    public void setFollow_array(ArrayList<Follow> follow_array) {
        this.follow_array = follow_array;
    }

    private ArrayList<Follow> follow_array;
    private String finalLikeText;
    private String tmpLike;
    private int likePosition;

    public FollowingAdapter(Context c, ArrayList<Follow> data) {
        mContext = c;
        follow_array = data;
    }
    @Override
    public void notifyDataSetChanged() // Create this function in your adapter class
    {
        super.notifyDataSetChanged();
    }
    @Override
    public int getCount() {
        return follow_array.size();
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
    public View getView(final int position, View view, ViewGroup viewGroup) {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.follow, null, true);

        final Follow oneFollow = follow_array.get(position);
        ImageView userProfileImg = (ImageView) rowView.findViewById(R.id.userProfileImageView);
        TextView userName = (TextView) rowView.findViewById(R.id.userNameTextView);
        TextView locationName = (TextView) rowView.findViewById(R.id.locationTextView);
        ImageView photoImg = (ImageView) rowView.findViewById(R.id.photoImageView);
        final TextView likedText = (TextView) rowView.findViewById(R.id.likedTextView);
        TextView commentText = (TextView) rowView.findViewById(R.id.commentTextView);

        userProfileImg.setImageBitmap(oneFollow.getUserProfileImg());
        userName.setText(oneFollow.getFullName());
        locationName.setText(oneFollow.getLocation());
        photoImg.setImageBitmap(oneFollow.getPhoto());
        if (oneFollow.getLike() != null) {
            System.out.println("Like: " + likedText.getText());
            if (tmpLike == null && finalLikeText == null) {
                likedText.setText(oneFollow.getLike().toString().replace(',', ' '));
            }else if(tmpLike != null && finalLikeText != null && position == likePosition){
                likedText.setText(finalLikeText);
            }else{
                likedText.setText(oneFollow.getLike().toString().replace(',', ' '));
            }
        }
        if (oneFollow.getComment() != null) {
            commentText.setText(oneFollow.getComment().toString().replace(',',' ').substring(1,
                    oneFollow.getComment().toString().length()-1));
        }
        return rowView;
    }

}
