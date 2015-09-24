package com.parse.unimelb;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ProfileFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    Button editProfile;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private TextView username, postNum, followerNum, followingNum;
    private OnFragmentInteractionListener mListener;
    private ImageButton imageButton;
    private ParseUser currentUser;
    int postNumber, followerNumber, followingNumber;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public ProfileFragment() {
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
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        username = (TextView) view.findViewById(R.id.usernameTextView);
        editProfile = (Button) view.findViewById(R.id.editProfileButton);
        imageButton = (ImageButton) view.findViewById(R.id.profileImageButton);
        postNum = (TextView) view.findViewById(R.id.postNumTextView);
        followerNum = (TextView) view.findViewById(R.id.followerNumTextView);
        followingNum = (TextView) view.findViewById(R.id.followingNumTextView);
        getUserCountResponse();
        //set the username
        currentUser = ParseUser.getCurrentUser();
        username.setText(currentUser.get("FullName").toString());
        //add listener to button
        editProfile.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                Intent intent = new Intent(getActivity(), EditProfileActivity.class);
                startActivity(intent);
            }
        });
        //set profile image
        ParseFile imageFile = (ParseFile) currentUser.get("Image");
        if (imageFile != null) {
            imageFile.getDataInBackground(new GetDataCallback() {
                public void done(byte[] data, ParseException e) {
                    if (e == null) {
                        Bitmap bitmapImage = BitmapFactory.decodeByteArray(data, 0, data.length);
                        imageButton.setImageBitmap(bitmapImage);
                        imageButton.setBackground(null);
                    } else {
                        // something went wrong
                    }
                }
            });
        }else{
            Drawable myDrawable = getResources().getDrawable(R.drawable.default_profile_image);
            Bitmap defaultImg = ((BitmapDrawable) myDrawable).getBitmap();
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            defaultImg.compress(Bitmap.CompressFormat.PNG, 0 /*ignored for PNG*/, bos);
            byte[] bitmapdata = bos.toByteArray();
            ParseFile image = new ParseFile("profile_pic.jpg", bitmapdata);
            currentUser.put("Image", image);
            currentUser.saveInBackground();
            imageButton.setImageBitmap(defaultImg);
            imageButton.setBackground(null);
        }
        registerForContextMenu(imageButton);
        imageButton.setOnClickListener(new ImageButton.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.showContextMenu();
            }
        });
        return view;
    }


    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getActivity().getMenuInflater();
        inflater.inflate(R.menu.menu_profile_img, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.profile_img_button_remove:
                // your first action code
                resetProfileImage();
                return true;
            case R.id.profile_img_button_new:
                // your second action code
                dispatchTakePictureIntent();
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }


    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }
    private void resetProfileImage(){
        Drawable myDrawable = getResources().getDrawable(R.drawable.default_profile_image);
        Bitmap defaultImg = ((BitmapDrawable) myDrawable).getBitmap();
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        defaultImg.compress(Bitmap.CompressFormat.PNG, 0 /*ignored for PNG*/, bos);
        byte[] bitmapdata = bos.toByteArray();
        ParseFile image = new ParseFile("profile_pic.jpg", bitmapdata);
        currentUser.put("Image", image);
        currentUser.saveInBackground();
        imageButton.setImageBitmap(defaultImg);
        imageButton.setBackground(null);
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == getActivity().RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            imageButton.setBackground(null);
            imageButton.setImageBitmap(imageBitmap);

//Convert bitmap to byte array
            Bitmap bitmap = imageBitmap;
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 0 /*ignored for PNG*/, bos);
            byte[] bitmapdata = bos.toByteArray();

//write the bytes in file
            ParseFile image = new ParseFile("profile_pic.jpg", bitmapdata);
            currentUser.put("Image",image);
            currentUser.saveInBackground(new SaveCallback() {
                public void done(ParseException e) {
                    if (e == null) {
                        Toast.makeText(getActivity().getApplicationContext(),
                                "Profile image successfully updated",
                                Toast.LENGTH_LONG).show();
                    } else {
                        // ParseException
                        Toast.makeText(getActivity().getApplicationContext(),
                                "Network failure",
                                Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
    }

    public void getUserCountResponse(){
        // request url
        String request_url = getResources().getString(R.string.instagram_api_url)
                + getResources().getString(R.string.instagram_api_users_method)
                + getResources().getString(R.string.instagram_user_id)
                + "?access_token=" + getResources().getString(R.string.instagram_access_token);
        System.out.println("Response" + request_url);
        // request a json response

        JsonObjectRequest jsonRequest = new JsonObjectRequest
                (Request.Method.GET, request_url, (String)null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // the response is already constructed as a JSONObject!
                        try {
                            response = response.getJSONObject("data");
                            JSONObject count = response.getJSONObject("counts");
                            postNumber = count.getInt("media");
                            followerNumber = count.getInt("followed_by");
                            followingNumber = count.getInt("follows");
                            System.out.println("Response" + postNumber);
                            postNum.setText(String.valueOf(postNumber));
                            followerNum.setText(String.valueOf(followerNumber));
                            followingNum.setText(String.valueOf(followingNumber));
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
    public void getUserPhoto(){

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

}
