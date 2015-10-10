package com.parse.unimelb;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;

import com.astuetz.PagerSlidingTabStrip;
import com.parse.unimelb.Helper.BitmapStore;

import java.io.File;
import java.util.Locale;

public class PostActivity extends Activity {

    private ImageView imageview = null;
    private Bitmap rawBitmap = null;
    private Bitmap thumbnail = null;

    private Button btnBluetooth = null;
    private Button btnPost = null;
    private Switch switchLocation = null;
    private EditText caption = null;
    private String strCaption;


    //TODO: garbage recycle: newBitMap, currentBitmap in the edit activity
    //TODO: use api to post new photos
    //TODO: Add location information at when taking the image

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        imageview = (ImageView) findViewById(R.id.thumbnail);

        // get the bitmap from file
        Intent intent = getIntent();
        String filePath = intent.getStringExtra("post_img");

        rawBitmap = BitmapFactory.decodeFile(filePath);

        thumbnail = rawBitmap;
        imageview.setImageBitmap(thumbnail);

        //TODO: bluetooth button, either to go to another activity, or show the list
        btnBluetooth = (Button) findViewById(R.id.button_bluetooth);
        btnBluetooth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        


    }
}



