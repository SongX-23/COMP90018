package com.parse.unimelb;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;

/**
 * Created by songxue on 2/10/2015.
 * This class is the adapter class for browse fragment. The function of like and comment button are
 * implemented here. It also controls the view of the list view in browse fragment.
 */
public class BrowseAdapter extends BaseAdapter{
    //variables declaration
    private Context mContext;
    private ArrayList<Feed> feed_array;
    private String finalLikeText;
    private String tmpLike;
    private int likePosition;

    //constructor
    public BrowseAdapter(Context c, ArrayList<Feed> data) {
        mContext = c;
        feed_array = data;
    }

    //setter method to pass the data from fragment to here
    public void setFeed_array(ArrayList<Feed> feed_array) {
        this.feed_array = feed_array;
    }

    //this method updates the list view when the array has been changed
    @Override
    public void notifyDataSetChanged() // Create this function in your adapter class
    {
        super.notifyDataSetChanged();
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

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    //set the view of the list view in browse fragment
    public View getView(final int position, View view, ViewGroup viewGroup) {
        //fetch the fragment view
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View rowView = inflater.inflate(R.layout.feed, null, true);
        //get the feed based on position
        final Feed oneFeed = feed_array.get(position);
        //find all UI elements
        ImageView userProfileImg = (ImageView) rowView.findViewById(R.id.userProfileImageView);
        TextView userName = (TextView) rowView.findViewById(R.id.userNameTextView);
        TextView locationName = (TextView) rowView.findViewById(R.id.locationTextView);
        ImageView photoImg = (ImageView) rowView.findViewById(R.id.photoImageView);
        final TextView likedText = (TextView) rowView.findViewById(R.id.likedTextView);
        TextView commentText = (TextView) rowView.findViewById(R.id.commentTextView);
        final ImageButton likeButton = (ImageButton) rowView.findViewById(R.id.likeButton);
        final ImageButton commentButton = (ImageButton) rowView.findViewById(R.id.commentButton);
        TextView captionText = (TextView) rowView.findViewById(R.id.captionTextView);
        //Set fixed text view
        TextView captionFixText = (TextView) rowView.findViewById(R.id.textView1);
        captionFixText.setTypeface(captionFixText.getTypeface(), Typeface.BOLD);
        TextView likesFixText = (TextView) rowView.findViewById(R.id.textView);
        likesFixText.setTypeface(likesFixText.getTypeface(), Typeface.BOLD);
        TextView commentFixText = (TextView) rowView.findViewById(R.id.commentText);
        commentFixText.setTypeface(commentFixText.getTypeface(), Typeface.BOLD);
        //set user profile image, user name, location name, photo image
        userProfileImg.setImageBitmap(oneFeed.getUserProfileImg());
        userName.setText(oneFeed.getDisplayName());
        locationName.setText(oneFeed.getLocation());
        photoImg.setImageBitmap(oneFeed.getPhoto());
        //set visibility for button, hide from bluetooth feeds
        if (oneFeed.getCaption() != null && oneFeed.getCaption().equals("#In Range")){
            likeButton.setVisibility(View.GONE);
            commentButton.setVisibility(View.GONE);
            likesFixText.setVisibility(View.GONE);
            commentFixText.setVisibility(View.GONE);
            likedText.setVisibility(View.GONE);
            commentText.setVisibility(View.GONE);
            Drawable myDrawable = rowView.getResources().getDrawable(R.drawable.bluetooth_icon);
            Bitmap btImg = ((BitmapDrawable) myDrawable).getBitmap();
            userProfileImg.setImageBitmap(btImg);
            userName.setText("Photo sent via bluetooth");
        }
        // implement the like function here. POST request to Instagram API
        likeButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                if (!oneFeed.getUser_has_liked()) {
                    likeButton.setBackground(rowView.getResources().getDrawable(R.drawable.filled_heart));
                    oneFeed.setUser_has_liked(true);
                    tmpLike = likedText.getText().toString();
                    likePosition = position;
                    String url = mContext.getResources().getString(R.string.instagram_api_url)
                            + mContext.getResources().getString(R.string.instagram_api_media_method)
                            + oneFeed.getMediaID().toString()
                            + "/likes?access_token="
                            + mContext.getResources().getString(R.string.instagram_access_token);
                    //make the request
                    JsonObjectRequest postRequest = new JsonObjectRequest(Request.Method.POST, url,
                            new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    try {
                                        JSONObject jsonResponse = response.getJSONObject("meta");
                                        int code = jsonResponse.getInt("code");
                                        if (code == 200) {
                                            Toast.makeText(mContext.getApplicationContext(),
                                                    "You liked this photo!",
                                                    Toast.LENGTH_LONG).show();
                                            //update liked list
                                        }
                                        System.out.println("Like: " + tmpLike);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    error.printStackTrace();
                                    Toast.makeText(mContext,
                                            "Network failure",
                                            Toast.LENGTH_LONG).show();
                                    likedText.setText(oneFeed.getLike().toString());
                                }
                            });
                    Volley.newRequestQueue(mContext.getApplicationContext()).add(postRequest);
                    //updating the view
                    if (tmpLike.equals(oneFeed.getLike().toString())) {
                        if (tmpLike.length() > 0) {
                            if (Character.isDigit(tmpLike.charAt(1))) {
                                int likeNum = Integer.parseInt(tmpLike.replaceAll("[^0-9]", "")) + 1;
                                finalLikeText = "[" + String.valueOf(likeNum) + " likes]";
                            } else {
                                finalLikeText = tmpLike.replace("]", "") + ", carl_xs]";
                            }
                        }
                        likedText.setText(finalLikeText);
                    }
                    System.out.println("Like: " + finalLikeText);
                } else {
                    Toast.makeText(mContext,
                            "You have already liked this photo",
                            Toast.LENGTH_LONG).show();
                }
            }


        });
        // implement the comment function here. goes to comment activity
        commentButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                Intent intent = new Intent(mContext, CommentActivity.class);
                mContext.startActivity(intent);
            }
        });
        // set up the blank caption text in the view
        if (oneFeed.getCaption() != null) {
            captionText.setText(oneFeed.getCaption());
        } else {
            captionText.setText("There is no caption for this photo.");
        }
        // set up the blank like text in the view
        if (oneFeed.getLike() != null) {
            System.out.println("Like: " + likedText.getText());
            if (tmpLike == null && finalLikeText == null) {
                if (oneFeed.getUser_has_liked()){
                    likeButton.setBackground(rowView.getResources().getDrawable(R.drawable.filled_heart));
                }
                likedText.setText(oneFeed.getLike().toString().replace(',', ' '));
            }else if(tmpLike != null && finalLikeText != null && position == likePosition){
                likedText.setText(finalLikeText);
                likeButton.setBackground(rowView.getResources().getDrawable(R.drawable.filled_heart));
            }else{
                if (oneFeed.getUser_has_liked()){
                    likeButton.setBackground(rowView.getResources().getDrawable(R.drawable.filled_heart));
                }
                likedText.setText(oneFeed.getLike().toString().replace(',', ' '));
            }
        } else {
            likedText.setText("Nobody has liked this photo yet.");
        }
        // set up the blank comment text in the view
        if (oneFeed.getComment() != null) {
            commentText.setText(oneFeed.getComment().toString().replace(',', ' ').substring(1,
                    oneFeed.getComment().toString().length() - 1));
        } else {
            commentText.setText("Nobody has commented on this photo yet.");
        }
        return rowView;
    }

}
