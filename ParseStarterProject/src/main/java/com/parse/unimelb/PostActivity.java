package com.parse.unimelb;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;

import com.parse.unimelb.Helper.BluetoothPair;

import java.io.File;
import java.util.ArrayList;

public class PostActivity extends Activity {

    private ImageView imageview = null;
    private Bitmap rawBitmap = null;
    private Bitmap thumbnail = null;

    private Button btnBluetooth = null;
    private Button btnPost = null;
    private String filePath = "";
    private BluetoothPair btPair;


    // temp arraylist
    private ArrayList<BluetoothPair> bluetoothPairs = new ArrayList<>();

    //TODO: garbage recycle: newBitMap, currentBitmap in the edit activity

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        imageview = (ImageView) findViewById(R.id.thumbnail);

        // get the bitmap from file
        Intent intent = getIntent();
        filePath = intent.getStringExtra("post_img");

        rawBitmap = BitmapFactory.decodeFile(filePath);

        thumbnail = rawBitmap;
        imageview.setImageBitmap(thumbnail);


        //TODO: bluetooth button, either to go to another activity, or show the list
        btnBluetooth = (Button) findViewById(R.id.button_bluetooth);
        btnBluetooth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // add list of device info to the arraylist every time
                bluetoothPairs.add(new BluetoothPair("Mark", "Device_1"));
                bluetoothPairs.add(new BluetoothPair("Drake", "Device_2"));

                showDialog();
            }
        });

        btnPost = (Button) findViewById(R.id.button_post);
        btnPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createInstagramIntent(filePath);
            }
        });
    }


    private void createInstagramIntent(String filePath){
        Intent instagram = new Intent(android.content.Intent.ACTION_SEND);
        instagram.setType("image/*");
        File file = new File(filePath);
        Uri uri = Uri.fromFile(file);
        instagram.putExtra(Intent.EXTRA_STREAM, uri);
        instagram.putExtra(Intent.EXTRA_TEXT, "YOUR TEXT TO SHARE IN INSTAGRAM");
        instagram.setPackage("com.instagram.android");


        startActivity(Intent.createChooser(instagram, "Share to"));
    }

    private void showDialog() {
        final Dialog dialog = new Dialog(this);

        View view = getLayoutInflater().inflate(R.layout.bluetooth_dialog, null);

        ListView lv = (ListView) view.findViewById(R.id.pair_list);

        // Change MyActivity.this and myListOfItems to your own values
        BluetoothDialog clad = new BluetoothDialog(PostActivity.this, bluetoothPairs);

        lv.setAdapter(clad);

        // action on item
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(PostActivity.this,  SwipeActivity.class);
                intent.putExtra("post_img", filePath);
                btPair = bluetoothPairs.get(position);
                intent.putExtra("device_info", btPair.getDevice() + "  " + btPair.getName());

                startActivity(intent);
            }
        });

        dialog.setContentView(view);

        dialog.show();

    }
}



