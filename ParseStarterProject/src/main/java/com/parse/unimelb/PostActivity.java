package com.parse.unimelb;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;

import com.parse.unimelb.Helper.*;

import java.io.File;
import java.util.ArrayList;
import java.util.Set;
import java.util.UUID;

public class PostActivity extends Activity {

    public ImageView imageview = null;
    private Bitmap rawBitmap = null;
    private Bitmap thumbnail = null;

    private Button btnBluetooth = null;
    private Button btnPost = null;
    private String filePath = "";
    private BluetoothPair btPair;

    private final static int REQUEST_ENABLE_BT = 1;
    private BluetoothAdapter mBluetoothAdapter;

    Set<BluetoothDevice> pairedDevices;


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

        startBluetooth();
    }


    private void startBluetooth() {

        /*
         Check to see if Bluetooth Adapter is enabled or not. If its enabled, set
         the request to enable it.
         */
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (!mBluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        }

        // Start Intent for becoming discoverable. Set the Discoverable duration for 300 seconds.
        Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
        discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
        startActivity(discoverableIntent);
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);

        // Register the devices and start broadcast to get the name of the Devices.
        // This registers the devices using BroadcastReceiver.
        registerReceiver(mReceiver, filter);

        // This code searches for all devices nearby which have their bluetooth on and are currently
        // discoverable. Currently I have hardcoded this process to search for Aurora. If it finds
        // Aurora, it will become the client and start the client thread. Else, the server thread will
        // continue to function.
        pairedDevices = mBluetoothAdapter.getBondedDevices();
        if (pairedDevices.size() > 0) {
            for (BluetoothDevice device : pairedDevices) {
                bluetoothPairs.add(new BluetoothPair(device));
            }
        }
//        if (pairedDevices.size() > 0) {
//            for (BluetoothDevice device : pairedDevices) {
//                String deviceName = device.getName();
//                if (deviceName.equals("Aurora")) {
//                    isServer = false;
//                    Bitmap bitmap= BitmapFactory.decodeResource(this.getResources(),R.drawable.default_profile_image);
//
//                    ConnectThread myConnection = new ConnectThread(device, bitmap,mBluetoothAdapter, MY_UUID);
//                    myConnection.start();
//                    ct = myConnection;
//                }
//                Log.d("Bluetooth Device: ", deviceName);
//            }
//        }

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
        dialog.setTitle("Choose a Bluetooth Pair");

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
                intent.putExtra("device", bluetoothPairs.get(position).getDevice());

                startActivity(intent);
            }
        });

        dialog.setContentView(view);

        dialog.show();

    }

    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                String name = device.getName();
                Log.d("Bluetooth Device : ", name);
            }
        }
    };
}



