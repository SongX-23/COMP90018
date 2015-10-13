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
/*        final Button likeButton = (Button) rowView.findViewById(R.id.likeButton);
        likeButton.setVisibility(View.INVISIBLE);
        likeButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                tmpLike = likedText.getText().toString();
                likePosition = position;
                String url = mContext.getResources().getString(R.string.instagram_api_url)
                        + mContext.getResources().getString(R.string.instagram_api_media_method)
                        + oneFollow.getMediaID().toString()
                        + "/likes?access_token="
                        + mContext.getResources().getString(R.string.instagram_access_token);

                JsonObjectRequest postRequest = new JsonObjectRequest(Request.Method.POST, url,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                try {
                                    JSONObject jsonResponse = response.getJSONObject("meta");
                                    int code = jsonResponse.getInt("code");
                                    if (code == 200){
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
                                likedText.setText(oneFollow.getLike().toString());
                            }
                        });
                Volley.newRequestQueue(mContext.getApplicationContext()).add(postRequest);
                if (tmpLike.equals(oneFollow.getLike().toString())) {
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
            }
        });
        Button commentButton = (Button) rowView.findViewById(R.id.commentButton);
        if(!oneFollow.isRecommended())commentButton.setVisibility(View.INVISIBLE);
        commentButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                Intent intent = new Intent(mContext, CommentActivity.class);
                mContext.startActivity(intent);
            }
        });*/


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
