package com.parse.unimelb;

import android.app.Activity;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.android.volley.Cache;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.parse.unimelb.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link BrowseFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link BrowseFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BrowseFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    private RequestQueue mRequestQueue;
    private ArrayList<Feed> feeds_array;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment BrowseFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static BrowseFragment newInstance(String param1, String param2) {
        BrowseFragment fragment = new BrowseFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public BrowseFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        //getFeedResponse();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Cache cache = new DiskBasedCache(getActivity().getCacheDir(), 1024*1024);
        BasicNetwork network = new BasicNetwork(new HurlStack());

        mRequestQueue = new RequestQueue(cache, network);
        mRequestQueue.start();
        loadFeeds();
        return inflater.inflate(R.layout.fragment_browse, container, false);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    public void getFeedResponse(){
        String request_url = getResources().getString(R.string.instagram_api_url)
                + getResources().getString(R.string.instagram_api_users_method)
                + "self/feed?access_token="
                + getResources().getString(R.string.instagram_access_token);
        System.out.println("Response" + request_url);
        // request a json response

        JsonObjectRequest jsonRequest = new JsonObjectRequest
                (Request.Method.GET, request_url, (String)null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // the response is already constructed as a JSONObject!
                        try {
                            response = response.getJSONObject("data");
                            System.out.println("Response: "+response);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                });

        Volley.newRequestQueue(getActivity()).add(jsonRequest);
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }
    public void loadFeeds(){
        //request url
        //String request_url = FeedFragment.getResources().getString(R.string.instagram_api_url)
        //        + FeedFragment.getResources().getString(R.string.instagram_api_users_method)
        //        + "self/feed?access_token="
        //        + FeedFragment.getResources().getString(R.string.instagram_access_token);
        String request_url = "https://api.instagram.com/v1/users/self/feed?access_token=25846960.1fb234f.1c7c1f3a4843498f88d0f559ff690eb2";
        //DEBUG
        System.out.println("Requesting from: " + request_url);
        //create a feed array list
        feeds_array = new ArrayList<>();
        //request a json response
        JsonObjectRequest jsonRequest = new JsonObjectRequest
                (Request.Method.GET, request_url, (String)null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            //get the feed array
                            JSONArray array = response.getJSONArray("data");
                            for (int i = 0; i < array.length(); i++) {
                                //create one feed obj
                                Feed feedObj = new Feed();
                                //get one feed
                                JSONObject oneFeed = array.getJSONObject(i);
                                //get the location block
                                if (!oneFeed.isNull("location")) {
                                    JSONObject locationJSON = oneFeed.getJSONObject("location");
                                    //get the location string
                                    if (locationJSON != null) {
                                        String location = locationJSON.getString("name");
                                        feedObj.setLocation(location);
                                        //DEBUG
                                        System.out.println("FEED: location = " + location);
                                    }
                                }else{
                                    feedObj.setLocation("");
                                }

                                //get the comment block
                                JSONObject commentsJSON = oneFeed.getJSONObject("comments");
                                //get comment counts
                                int commentsCount = commentsJSON.getInt("count");
                                //get comment content
                                if (commentsCount > 0){
                                    ArrayList<String> comments = new ArrayList<>();
                                    //get comment data
                                    JSONArray commentArray = commentsJSON.getJSONArray("data");
                                    for (int j = 0; j < commentArray.length(); j++){
                                        JSONObject oneComment = commentArray.getJSONObject(j);
                                        //get comment text
                                        String commentText = oneComment.getString("text");
                                        JSONObject commentFrom = oneComment.getJSONObject("from");
                                        String commentName = commentFrom.getString("full_name");
                                        String comment = commentName + ": " + commentText;
                                        //DEBUG
                                        System.out.println("FEED: comment = " + comment);
                                        comments.add(comment);
                                    }
                                    feedObj.setComment(comments);
                                }
                                //get the likes block
                                JSONObject likesJSON = oneFeed.getJSONObject("likes");
                                //get likes count
                                int likesCount = likesJSON.getInt("count");
                                //get likes content
                                if (likesCount > 0) {
                                    ArrayList<String > likes = new ArrayList<>();
                                    //get likes data
                                    JSONArray likeArray = likesJSON.getJSONArray("data");
                                    for (int k = 0; k < likeArray.length(); k++) {
                                        JSONObject oneLike = likeArray.getJSONObject(k);
                                        //get like name
                                        String likeName = oneLike.getString("full_name");
                                        likes.add(likeName);
                                        //DEBUG
                                        System.out.println("FEED: like = " + likeName);
                                    }
                                    feedObj.setLike(likes);
                                }
                                //get the image block
                                JSONObject imageJSON = oneFeed.getJSONObject("images");
                                //get the standard resoultion block
                                JSONObject standardResolution = imageJSON.getJSONObject("standard_resolution");
                                //get the image url
                                String imageURL = standardResolution.getString("url");
                                //DEBUG
                                System.out.println("FEED: image = " + imageURL);
                                feedObj.setPhotoURL(imageURL);
                                //fetch the image
                                getFeedImage(imageURL);
                                //get the media id
                                String mediaID = oneFeed.getString("id");
                                //DEBUG
                                System.out.println("FEED: id = " + mediaID);
                                //get user name
                                JSONObject userJSON = oneFeed.getJSONObject("user");
                                String userName = userJSON.getString("full_name");
                                //DEBUG
                                System.out.println("FEED: name = " + userName);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error){
                        error.printStackTrace();
                    }
                });
        mRequestQueue.add(jsonRequest);
    }

    private void getFeedImage(String imageURL) {
        ImageRequest imgRequest = new ImageRequest(imageURL, new Response.Listener<Bitmap>() {
            @Override
            public void onResponse(Bitmap response) {
                //do something with the bitmap
            }
        },0,0, ImageView.ScaleType.FIT_XY, Bitmap.Config.ARGB_8888,
                new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }

                });
        mRequestQueue.add(imgRequest);
    }

}
