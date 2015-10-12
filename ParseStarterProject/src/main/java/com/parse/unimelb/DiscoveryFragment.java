package com.parse.unimelb;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link DiscoveryFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link DiscoveryFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DiscoveryFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private ListView listView;
    private DiscoveryAdapter discoveryAdapter;
    private ArrayList<DiscoverUser> users;
    private Button search;
    private EditText searchInput;
    private TextView recommendText;
    final private int SEARCH_COUNT = 10;
    private OnFragmentInteractionListener mListener;
    private ParseUser currentUser = ParseUser.getCurrentUser();
    private String currentUserLocation;
    private ArrayList<ParseUser> weightedUsers;
    private ArrayList<Integer> userWeights;
    private ParseUser userToWeigh;
    private int weight;


//    private int melbourne = 0;
//    private int hobart = 1;
//    private int sydney = 2;
//    private int darwin = 3;
//    private int perth = 4;
//    private int adelaide = 5;
//    private int brisbane = 6;
    private int[][] weightOfCities;
    private String [] listOfCitites = new String[]{"Melbourne", "Hobart", "Sydney", "Darwin",
            "Perth", "Adelaide", "Brisbane"};;

    private int[][] arrayOfCities(){
        weightOfCities = new int[][]{{7,5,4,1,3,6,2}, {6,7,4,1,2,5,3}, {6,3,7,2,1,4,5},
                {5,1,4,7,3,6,2},{6,2,4,3,7,5,1},{6,4,5,1,2,7,3},{4,3,6,1,2,5,7}};
        return weightOfCities;
    }

    private int checkCity(String scrapedUserCity){
        int city = 1;
//        listOfCitites = new String[]{"Melbourne", "Hobart", "Sydney", "Darwin",
//                "Perth", "Adelaide", "Brisbane"};
        for(int i = 0; i<listOfCitites.length; i++){
            if (scrapedUserCity.equals(listOfCitites[i])){
                city = i;
            }
        }
        return city;
    }

    private boolean verifyCity(String scrapedUserCity){
        for(int i=0;i<listOfCitites.length;i++) {
            if(scrapedUserCity.equals(listOfCitites[i])) {
                return true;
            }
        }
        return false;
    }


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DiscoveryFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DiscoveryFragment newInstance(String param1, String param2) {
        DiscoveryFragment fragment = new DiscoveryFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public DiscoveryFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        recommendPeople();
    }

    public void recommendPeople(){
        users = new ArrayList<DiscoverUser>();
        ParseQuery<ParseUser> query = ParseUser.getQuery();
        query.whereEqualTo("Gender", "Male");
        query.findInBackground(new FindCallback<ParseUser>() {
            public void done(List<ParseUser> objects, ParseException e) {
                if (e == null) {
                    Log.e("Discovery", objects.toString());
                    calculateWeight((ArrayList<ParseUser>) objects);
                    ArrayList<ParseUser> parseUserDB = weightedUsers;
                    //pass the data into discovery user array list
                    for (int i = 0; i < parseUserDB.size(); i++) {
                        ParseUser tmpUser = parseUserDB.get(i);
                        String username = tmpUser.getUsername();
                        String gender = tmpUser.get("Gender").toString();
                        String location = tmpUser.get("City").toString();
                        ParseFile imageFile = (ParseFile) tmpUser.get("Image");
                        Bitmap profileImage;
                        if (imageFile != null) {
                            byte[] bitmapdata = new byte[0];
                            try {
                                bitmapdata = imageFile.getData();
                            } catch (ParseException eg) {
                                eg.printStackTrace();
                            }
                            Bitmap bitmap = BitmapFactory.decodeByteArray(bitmapdata, 0, bitmapdata.length);
                            profileImage = bitmap;
                        } else {
                            Drawable myDrawable = getResources().getDrawable(R.drawable.default_profile_image);
                            Bitmap defaultImage = ((BitmapDrawable) myDrawable).getBitmap();
                            profileImage = defaultImage;
                        }
                        DiscoverUser discoverUser = new DiscoverUser();
                        discoverUser.setProfileImage(profileImage);
                        discoverUser.setUsername(username);
                        discoverUser.setGender(gender);
                        discoverUser.setLocation(location);

                        users.add(discoverUser);
                    }
                    discoveryAdapter.setUsers(users);
                    discoveryAdapter.notifyDataSetChanged();
                } else {
                    // Something went wrong.
                    Log.e("Discovery", "Exception thrown");
                }
            }
        });
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_discovery, container, false);
        search = (Button) view.findViewById(R.id.searchButton);
        searchInput = (EditText) view.findViewById(R.id.searchEditText);
        recommendText = (TextView) view.findViewById(R.id.recommendTextView);
        listView = (ListView) view.findViewById(R.id.list);
        discoveryAdapter = new DiscoveryAdapter(getActivity(), getData());
        listView.setAdapter(discoveryAdapter);

        search.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {

                final String searchQuery = searchInput.getText().toString();
                if (searchQuery.length() != 0) {
                    recommendText.setText("Search Results");
                    String request_url = view.getResources().getString(R.string.instagram_api_url)
                            + view.getResources().getString(R.string.instagram_api_users_method)
                            + "search?access_token="
                            + view.getResources().getString(R.string.instagram_access_token)
                            + "&q="
                            + searchQuery
                            + "&count="
                            + SEARCH_COUNT;
                    System.out.println("Search URL: " + request_url);
                    users = new ArrayList<>();
                    JsonObjectRequest jsonRequest = new JsonObjectRequest
                            (Request.Method.GET, request_url, (String) null, new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    try {
                                        JSONArray array = response.getJSONArray("data");
                                        for (int i = 0; i < array.length(); i++) {
                                            final DiscoverUser oneUser = new DiscoverUser();
                                            JSONObject oneResult = array.getJSONObject(i);
                                            String username_result = oneResult.getString("username").toString();
                                            String fullname_result = oneResult.getString("full_name").toString();
                                            String userImgURL_result = oneResult.getString("profile_picture").toString().replace("\\", "");
                                            System.out.println("Search IMAGE: " + userImgURL_result);
                                            oneUser.setFullname(fullname_result);
                                            oneUser.setUsername(username_result);
                                            oneUser.setGender("Secret");
                                            oneUser.setLocation("Secret");
                                            oneUser.setImgURL(userImgURL_result);

                                            //set the image to profile image
                                            ImageRequest profileImgRequest =
                                                    new ImageRequest(userImgURL_result, new Response.Listener<Bitmap>() {
                                                        @Override
                                                        public void onResponse(Bitmap response) {
                                                            //do something with the bitmap
                                                            oneUser.setProfileImage(response);

                                                            if (discoveryAdapter != null) {
                                                                discoveryAdapter.notifyDataSetChanged();
                                                            }
                                                        }
                                                    }, 0, 0, ImageView.ScaleType.FIT_XY, Bitmap.Config.RGB_565,
                                                            new Response.ErrorListener() {
                                                                @Override
                                                                public void onErrorResponse(VolleyError error) {
                                                                    error.printStackTrace();
                                                                }

                                                            });
                                            if (profileImgRequest != null) {
                                                Volley.newRequestQueue(getActivity()).add(profileImgRequest);
                                            }

                                            //add parse user to array list
                                            users.add(oneUser);
                                            if (discoveryAdapter != null) {
                                                discoveryAdapter.notifyDataSetChanged();
                                            }
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    error.printStackTrace();
                                    Toast.makeText(getActivity(),
                                            "Network failure",
                                            Toast.LENGTH_LONG).show();
                                }
                            });
                    Volley.newRequestQueue(getActivity()).add(jsonRequest);
                    discoveryAdapter.setUsers(users);
                    if (discoveryAdapter != null) {
                        discoveryAdapter.notifyDataSetChanged();
                    }
                } else {
                    Toast.makeText(getActivity(),
                            "Input is empty, showing recommended users",
                            Toast.LENGTH_LONG).show();
                    recommendPeople();
                    if (discoveryAdapter != null) {
                        discoveryAdapter.notifyDataSetChanged();
                    }
                }
            }
        });
        return view;
    }

    private boolean verifyGender(String userGender){
        if((userGender.equals("Male")) || (userGender.equals("Female"))) {
            return true;
        }
        return false;
    }


//    private void discoveryAlgorithm(ArrayList<ParseUser> listOfUsers){
//        for(int i = 0; i< listOfUsers.size();i++){
//            calculateWeight(listOfUsers.get(i));
//        }
//    }

    private void calculateWeight(ArrayList<ParseUser> listOfUsers){
        for(int i = 0; i< listOfUsers.size();i++) {
            userToWeigh = listOfUsers.get(i);
            weight = 1;
            String currentUserCity = currentUser.get("City").toString();
            String scrapedUserCity = userToWeigh.get("City").toString();
            int userLoc = checkCity(currentUserCity);
            String currentUserGender = currentUser.get("Gender").toString();
            String recommendedUserGender = userToWeigh.get("Gender").toString();

            if (verifyCity(currentUserCity)) {
                if (verifyCity(scrapedUserCity)) {
                    int recommendedUserCity = checkCity(scrapedUserCity);
                    weight = weightOfCities[userLoc][recommendedUserCity];
                }
            }
            if (verifyGender(currentUserGender)) {
                if (verifyGender(recommendedUserGender)) {
                    boolean currentUserPresent = verifyGender(currentUserGender);
                    boolean weighedUserPresent = verifyGender(recommendedUserGender);

                    if (currentUserPresent && weighedUserPresent) {
                        if (currentUserGender.equals(recommendedUserGender)) {
                            weight += 5;
                        }
                    }
                }
            }
            weightedUsers.add(userToWeigh);
            userWeights.add(weight);
        }
    }


    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    public ArrayList<DiscoverUser> getData(){
        return users;
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
        void onFragmentInteraction(Uri uri);
    }
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) { recommendPeople();
        }
        else {  }
    }
}
