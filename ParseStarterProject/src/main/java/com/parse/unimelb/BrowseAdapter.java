package com.parse.unimelb;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
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
 */
public class BrowseAdapter extends BaseAdapter{
    private Context mContext;
    private ArrayList<Feed> feed_array;

    public BrowseAdapter(Context c, ArrayList<Feed> data) {
        mContext = c;
        feed_array = data;
    }
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

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.feed, null, true);
        final Feed oneFeed = feed_array.get(position);
        ImageView userProfileImg = (ImageView) rowView.findViewById(R.id.userProfileImageView);
        TextView userName = (TextView) rowView.findViewById(R.id.userNameTextView);
        TextView locationName = (TextView) rowView.findViewById(R.id.locationTextView);
        ImageView photoImg = (ImageView) rowView.findViewById(R.id.photoImageView);
        final TextView likedText = (TextView) rowView.findViewById(R.id.likedTextView);
        TextView commentText = (TextView) rowView.findViewById(R.id.commentTextView);
        Button likeButton = (Button) rowView.findViewById(R.id.likeButton);
        likeButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                String url = mContext.getResources().getString(R.string.instagram_api_url)
                            + mContext.getResources().getString(R.string.instagram_api_media_method)
                            + oneFeed.getMediaID().toString()
                            + "/likes?access_token="
                            + mContext.getResources().getString(R.string.instagram_access_token);

                    JsonObjectRequest postRequest = new JsonObjectRequest(Request.Method.POST, url,
                            new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    try {
                                        String tmpLike = likedText.getText().toString();
                                        JSONObject jsonResponse = response.getJSONObject("meta");
                                        int code = jsonResponse.getInt("code");
                                        if (code == 200){
                                            Toast.makeText(mContext.getApplicationContext(),
                                                    "You liked this photo!",
                                                    Toast.LENGTH_LONG).show();
                                            //update liked list

                                            int likeNum = Integer.parseInt(tmpLike.replaceAll("[^0-9]", "")) + 1;
                                            String finalLikeText = String.valueOf(likeNum) + " likes";
                                            likedText.setText(finalLikeText);
                                            System.out.println("Like: " + finalLikeText);
                                            notifyDataSetChanged();
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    error.printStackTrace();

                                }
                            });
                    Volley.newRequestQueue(mContext.getApplicationContext()).add(postRequest);
                }

        });
        Button commentButton = (Button) rowView.findViewById(R.id.commentButton);
       commentButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                Intent intent = new Intent(mContext, CommentActivity.class);
                mContext.startActivity(intent);
            }
        });


        userProfileImg.setImageBitmap(oneFeed.getUserProfileImg());
        userName.setText(oneFeed.getDisplayName());
        locationName.setText(oneFeed.getLocation());
        photoImg.setImageBitmap(oneFeed.getPhoto());
        if (oneFeed.getLike() != null) {
            likedText.setText(oneFeed.getLike().toString().replace(',',' '));
        }
        if (oneFeed.getComment() != null) {
            commentText.setText(oneFeed.getComment().toString().replace(',',' '));
        }
        return rowView;
    }
}
