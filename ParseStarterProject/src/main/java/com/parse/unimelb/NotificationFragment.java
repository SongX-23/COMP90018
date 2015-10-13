package com.parse.unimelb;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import com.parse.unimelb.Notification.*;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link NotificationFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link NotificationFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NotificationFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private ArrayList<String> followingList;
    private ArrayList<String> followerList;
    private OnFragmentInteractionListener mListener;
    private static ArrayList<You> arrayYou;
    private static ArrayList<Follow> arrayFollow;
    private ListView listView;
    private FollowingAdapter adapter;
    private double latitudeCurrent;
    private double longitudeCurrent;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment NotificationFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static NotificationFragment newInstance(String param1, String param2) {
        NotificationFragment fragment = new NotificationFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public NotificationFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_notification, container, false);
        //getting GPS location
        // check if GPS enabled
/*
        GPSTracker gpsTracker = new GPSTracker(this.getActivity());

        if (gpsTracker.getIsGPSTrackingEnabled()) {
            latitudeCurrent = gpsTracker.latitude;

            longitudeCurrent = gpsTracker.longitude;
        } else {
            gpsTracker.showSettingsAlert();
        }
*/

        Button following = (Button) view.findViewById(R.id.btnFollowing);
        Button follower = (Button) view.findViewById(R.id.btnYou);

        listView = (ListView) view.findViewById(R.id.listViewFollow);
        adapter = new FollowingAdapter(getActivity(), getData());
        listView.setAdapter(adapter);

        following.setOnClickListener(

                new View.OnClickListener() {
                    public void onClick(View arg0) {
                        getFollows(true, 10);
                        adapter.setFollow_array(getData());
                        adapter.notifyDataSetChanged();
                    }
                }
        );

        follower.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View arg0) {
//                        Collections.sort(getData(), new BeanComparator("distance"));
                        getFollows(false, 10);
                        adapter.setFollow_array(arrayFollow);
                        adapter.notifyDataSetChanged();
                    }
                }
        );

        return view;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            //Follow();
            getFollows(true, 10);
            //getYou(true);
            getLiked();
        } else {
        }
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

    private String followUrl(boolean isFollowing, int limit) {
        String type = (isFollowing) ? "follows" : "followed-by";
        String request_url = "https://api.instagram.com/v1/users/25846960/"
                + type
                + "?access_token=25846960.1fb234f.1c7c1f3a4843498f88d0f559ff690eb2&count="
                + limit;
        return request_url;
    }

    public ArrayList<Follow> getData() {
        return arrayFollow;
    }

    public void getFollows(final boolean isFollowing, final int size) {

        // DEBUG
        Log.e("Is Following", followUrl(isFollowing, size));
        Log.e("Is Size", "" + size);

        arrayFollow = new ArrayList<>();
        if (isFollowing) {
            followingList = new ArrayList<>();
        } else {
            followerList = new ArrayList<>();
        }

        JsonObjectRequest activityRequest =

                new JsonObjectRequest(
                        Request.Method.GET,
                        followUrl(isFollowing, size),
                        (String) null,

                        new Response.Listener<JSONObject>() {

                            @Override
                            public void onResponse(JSONObject response) {

                                try {

                                    final boolean isRecommended;

                                    //get the feed array
                                    JSONArray array = response.getJSONArray("data");

                                    for (int i = 0; i < size; i++) {

                                        if (isFollowing) {
                                            followingList.add(array.getJSONObject(i).getString("id"));
                                        } else {
                                            followerList.add(array.getJSONObject(i).getString("id"));
                                        }

                                        String feed_url = "https://api.instagram.com/v1/users/" +
                                                array.getJSONObject(i).getString("id") +
                                                "/media/recent?access_token=25846960.1fb234f." +
                                                "1c7c1f3a4843498f88d0f559ff690eb2&count=1";

                                        JsonObjectRequest jsonRequest =

                                                new JsonObjectRequest(
                                                        Request.Method.GET,
                                                        feed_url, (String) null,

                                                        new Response.Listener<JSONObject>() {

                                                            @Override
                                                            public void onResponse(JSONObject response) {

                                                                try {

                                                                    //get the feed array
                                                                    JSONArray array = response.getJSONArray("data");

                                                                    //create follow object obj
                                                                    final Follow follow = new Follow();

                                                                    //set recommended status

                                                                    //get follow object
                                                                    JSONObject oneFollow = array.getJSONObject(0);

                                                                    //get the location block
                                                                    if (!oneFollow.isNull("location")) {

                                                                        JSONObject locationJSON = oneFollow.getJSONObject("location");

                                                                        //get the location string
                                                                        if (locationJSON != null) {
                                                                            String location = locationJSON.getString("name");
                                                                            follow.setLocation(location);
                                                                            double longitude = locationJSON.getDouble("longitude");
                                                                            follow.setLongitude(longitude);
                                                                            double latitude = locationJSON.getDouble("latitude");
                                                                            follow.setLatitude(latitude);
                                                                            double distance =
                                                                                    Math.pow(
                                                                                            Math.pow((latitude - latitudeCurrent), 2)
                                                                                                    + Math.pow((longitude - longitudeCurrent), 2)
                                                                                            , 0.5);

                                                                            follow.setDistance(distance);

                                                                            //DEBUG
                                                                            System.out.println("FEED: location = " + distance);
                                                                        }
                                                                    } else {
                                                                        follow.setLocation("");
                                                                        follow.setDistance(
                                                                                Math.pow((
                                                                                        Math.pow(180.0, 2)
                                                                                                + Math.pow(360.0, 2)
                                                                                ), 0.5));
                                                                    }

                                                                    //get the comment block
                                                                    JSONObject commentsJSON = oneFollow.getJSONObject("comments");

                                                                    //get comment counts
                                                                    int commentsCount = commentsJSON.getInt("count");

                                                                    //get comment content
                                                                    if (commentsCount > 0) {

                                                                        ArrayList<String> comments = new ArrayList<>();

                                                                        //get comment data
                                                                        JSONArray commentArray = commentsJSON.getJSONArray("data");

                                                                        for (int j = 0; j < commentArray.length(); j++) {
                                                                            JSONObject oneComment = commentArray.getJSONObject(j);
                                                                            //get comment text
                                                                            String commentText = oneComment.getString("text");
                                                                            JSONObject commentFrom = oneComment.getJSONObject("from");
                                                                            String commentName = commentFrom.getString("username");
                                                                            String comment = commentName + ": " + commentText + "\n";
                                                                            //DEBUG
                                                                            System.out.println("FEED: comment = " + comment);
                                                                            comments.add(comment);
                                                                        }
                                                                        follow.setComment(comments);
                                                                    } else {
                                                                        follow.setComment(null);
                                                                    }
                                                                    //get the likes block
                                                                    JSONObject likesJSON = oneFollow.getJSONObject("likes");
                                                                    //get likes count
                                                                    int likesCount = likesJSON.getInt("count");
                                                                    //get likes content
                                                                    if (likesCount > 0) {
                                                                        ArrayList<String> likes = new ArrayList<>();
                                                                        if (likesCount <= 4) {
                                                                            //get likes data
                                                                            JSONArray likeArray = likesJSON.getJSONArray("data");
                                                                            for (int k = 0; k < likeArray.length(); k++) {
                                                                                JSONObject oneLike = likeArray.getJSONObject(k);
                                                                                //get like name
                                                                                String likeName = oneLike.getString("username");
                                                                                likes.add(likeName);
                                                                                //DEBUG
                                                                                System.out.println("FEED: like = " + likeName);
                                                                            }
                                                                        } else {
                                                                            String likedString = Integer.toString(likesCount) + " likes";
                                                                            likes.add(likedString);
                                                                        }
                                                                        follow.setLike(likes);

                                                                    } else {
                                                                        follow.setLike(null);
                                                                    }
                                                                    //get the image block
                                                                    JSONObject imageJSON = oneFollow.getJSONObject("images");
                                                                    //get the standard resoultion block
                                                                    JSONObject standardResolution = imageJSON.getJSONObject("thumbnail");
                                                                    //get the image url
                                                                    String imageURL = standardResolution.getString("url");
                                                                    //DEBUG
                                                                    System.out.println("FEED: image = " + imageURL);
                                                                    follow.setPhotoURL(imageURL);
                                                                    if (adapter != null) {
                                                                        adapter.notifyDataSetChanged();
                                                                    }
                                                                    //fetch the image
                                                                    ImageRequest imgRequest = new ImageRequest(imageURL, new Response.Listener<Bitmap>() {
                                                                        @Override
                                                                        public void onResponse(Bitmap response) {
                                                                            //do something with the bitmap
                                                                            follow.setPhoto(response);
                                                                            if (adapter != null) {
                                                                                adapter.notifyDataSetChanged();
                                                                            }
                                                                        }
                                                                    }, 0, 0, ImageView.ScaleType.FIT_XY, Bitmap.Config.RGB_565,
                                                                            new Response.ErrorListener() {
                                                                                @Override
                                                                                public void onErrorResponse(VolleyError error) {
                                                                                    error.printStackTrace();
                                                                                }

                                                                            });
                                                                    if (imgRequest != null) {
                                                                        Volley.newRequestQueue(getActivity()).add(imgRequest);
                                                                    }

                                                                    //get & set the media id
                                                                    String mediaID = oneFollow.getString("id");
                                                                    follow.setMediaID(mediaID);

                                                                    //set recommended status
                                                                    follow.setRecommended(!(isFollowing
                                                                            || followingList.contains(mediaID)));

                                                                    //DEBUG
                                                                    System.out.println("FEED: id = " + mediaID);

                                                                    //get $ set user name
                                                                    JSONObject userJSON = oneFollow.getJSONObject("user");
                                                                    String userName = userJSON.getString("username");
                                                                    String fullName = userJSON.getString("full_name");
                                                                    String name = (fullName.equals("") || fullName == null) ?
                                                                            userName : fullName;
                                                                    follow.setFullName(userName);

                                                                    // get profile image
                                                                    String userProfileImageURL = userJSON.getString("profile_picture");
                                                                    //DEBUG

                                                                    System.out.println("FEED: name = " + userName);
                                                                    ImageRequest profileImgRequest =
                                                                            new ImageRequest(
                                                                                    userProfileImageURL,
                                                                                    new Response.Listener<Bitmap>() {
                                                                                        @Override
                                                                                        public void onResponse(Bitmap response) {
                                                                                            //do something with the bitmap
                                                                                            follow.setUserProfileImg(response);
                                                                                            if (adapter != null) {
                                                                                                adapter.notifyDataSetChanged();
                                                                                            }
                                                                                        }
                                                                                    }, 0, 0,
                                                                                    ImageView.ScaleType.FIT_XY, Bitmap.Config.RGB_565,
                                                                                    new Response.ErrorListener() {
                                                                                        @Override
                                                                                        public void onErrorResponse(VolleyError error) {
                                                                                            error.printStackTrace();
                                                                                        }

                                                                                    });
                                                                    if (profileImgRequest != null) {
                                                                        Volley.newRequestQueue(getActivity()).add(profileImgRequest);
                                                                    }
                                                                    //add feed object into arraylist
                                                                    arrayFollow.add(follow);

                                                                    if (adapter != null) {
                                                                        adapter.notifyDataSetChanged();
                                                                    }
                                                                    //}
                                                                } catch (JSONException e) {
                                                                    e.printStackTrace();
                                                            /*Toast.makeText(getActivity(),
                                                                    "Network failure",
                                                                    Toast.LENGTH_LONG).show();*/
                                                                }
                                                            }
                                                        }, new Response.ErrorListener() {
                                                    @Override
                                                    public void onErrorResponse(VolleyError error) {
                                                        error.printStackTrace();
                                                /*Toast.makeText(getActivity(),
                                                        "Network failure",
                                                        Toast.LENGTH_LONG).show();*/
                                                    }
                                                });
                                        if (jsonRequest != null) {
                                            Volley.newRequestQueue(getActivity()).add(jsonRequest);
                                        }

                                    }

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    /*Toast.makeText(getActivity(),
                                            "Network failure",
                                            Toast.LENGTH_LONG).show();*/
                                }
                            }
                        },
                        new Response.ErrorListener() {

                            @Override
                            public void onErrorResponse(VolleyError error) {
                                error.printStackTrace();
                                if (followerList.isEmpty()) Toast.makeText(getActivity(),
                                        "Network failure",
                                        Toast.LENGTH_LONG).show();
                            }
                        }

                );

        if (activityRequest != null) {
            Volley.newRequestQueue(getActivity()).add(activityRequest);
        }

    }

    public void getYou(boolean isFollowing) {


        //arrayFollow = new ArrayList<>();
        final ArrayList<UserProfile> userProfiles = new ArrayList<>();

        JsonObjectRequest activityRequest =

                new JsonObjectRequest(
                        Request.Method.GET,
                        followUrl(isFollowing, 10),
                        (String) null,

                        new Response.Listener<JSONObject>() {

                            @Override
                            public void onResponse(JSONObject response) {

                                try {

                                    //get the feed array
                                    JSONArray array = response.getJSONArray("data");
                                    for (int i = 0; i < array.length(); i++) {
                                        final UserProfile profileHolder =
                                                new UserProfile(array.getJSONObject(i));
                                        userProfiles.add(profileHolder);

                                        ImageRequest imgRequest = new ImageRequest(
                                                profileHolder.getProfile_picture_url(),
                                                new Response.Listener<Bitmap>() {
                                                    @Override
                                                    public void onResponse(Bitmap response) {
                                                        //do something with the bitmap
                                                        profileHolder.setProfile_picture_bmp(response);
                                                        if (adapter != null) {
                                                            adapter.notifyDataSetChanged();
                                                        }
                                                    }
                                                }, 0, 0, ImageView.ScaleType.FIT_XY, Bitmap.Config.RGB_565,
                                                new Response.ErrorListener() {
                                                    @Override
                                                    public void onErrorResponse(VolleyError error) {
                                                        error.printStackTrace();
                                                    }

                                                });
                                        if (imgRequest != null) {
                                            Volley.newRequestQueue(getActivity()).add(imgRequest);
                                        }

                                        userProfiles.get(i).setProfile_picture_bmp(
                                                profileHolder.getProfile_picture_bmp()
                                        );


                                    }


                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    /*Toast.makeText(getActivity(),
                                            "Network failure",
                                            Toast.LENGTH_LONG).show();*/
                                }
                            }
                        },
                        new Response.ErrorListener() {

                            @Override
                            public void onErrorResponse(VolleyError error) {
                                error.printStackTrace();
                                if (followerList.isEmpty()) Toast.makeText(getActivity(),
                                        "Network failure",
                                        Toast.LENGTH_LONG).show();
                            }
                        }

                );

        if (activityRequest != null) {
            Volley.newRequestQueue(getActivity()).add(activityRequest);
        }



    }

    public void getLiked() {
        String liked_url = "https://api.instagram.com/v1/users/self/media/liked?access_token=25846960.1fb234f.1c7c1f3a4843498f88d0f559ff690eb2";


        //arrayFollow = new ArrayList<>();
        final ArrayList<LikedImage> likedImages = new ArrayList<>();

        JsonObjectRequest activityRequest =

                new JsonObjectRequest(
                        Request.Method.GET,
                        liked_url,
                        (String) null,

                        new Response.Listener<JSONObject>() {

                            @Override
                            public void onResponse(JSONObject response) {

                                try {

                                    //get the feed array
                                    JSONArray array = response.getJSONArray("data");
                                    for (int i = 0; i < array.length(); i++) {
                                        final LikedImage likedHolder =
                                                new LikedImage(array.getJSONObject(i));
                                        likedImages.add(likedHolder);

                                        ImageRequest imgRequest = new ImageRequest(
                                                likedHolder.getImageUrl(),
                                                new Response.Listener<Bitmap>() {
                                                    @Override
                                                    public void onResponse(Bitmap response) {
                                                        //do something with the bitmap
                                                        likedHolder.setImageBmp(response);
                                                        if (adapter != null) {
                                                            adapter.notifyDataSetChanged();
                                                        }
                                                    }
                                                }, 0, 0, ImageView.ScaleType.FIT_XY, Bitmap.Config.RGB_565,
                                                new Response.ErrorListener() {
                                                    @Override
                                                    public void onErrorResponse(VolleyError error) {
                                                        error.printStackTrace();
                                                    }

                                                });
                                        if (imgRequest != null) {
                                            Volley.newRequestQueue(getActivity()).add(imgRequest);
                                        }

                                        likedImages.get(i).setImageBmp(
                                                likedHolder.getImageBmp()
                                        );


                                    }


                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    /*Toast.makeText(getActivity(),
                                            "Network failure",
                                            Toast.LENGTH_LONG).show();*/
                                }
                            }
                        },
                        new Response.ErrorListener() {

                            @Override
                            public void onErrorResponse(VolleyError error) {
                                error.printStackTrace();
                                if (followerList.isEmpty()) Toast.makeText(getActivity(),
                                        "Network failure",
                                        Toast.LENGTH_LONG).show();
                            }
                        }

                );

        if (activityRequest != null) {
            Volley.newRequestQueue(getActivity()).add(activityRequest);
        }




    }
}
