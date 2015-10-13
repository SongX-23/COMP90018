package com.parse.unimelb;

import android.app.ActionBar;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.app.Activity;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.unimelb.Helper.ConnectThread;
import com.parse.unimelb.Helper.ServerThread;
import com.parse.unimelb.R;

import java.io.File;
import java.util.UUID;

public class SwipeActivity extends ActionBarActivity implements View.OnClickListener {
    private static final int SWIPE_MIN_DISTANCE = 120;
    private static final int SWIPE_MAX_OFF_PATH = 250;
    private static final int SWIPE_THRESHOLD_VELOCITY = 200;
    private GestureDetector gestureDetector;
    View.OnTouchListener gestureListener;

    private boolean isServer = true;
    private ConnectThread ct;
    private ServerThread myServer;
    private static final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB"); //UUID for Bluetooth Connections
    private BluetoothDevice device;
    private BluetoothAdapter mBluetoothAdapter;
    private String filePath;
    private String deviceName;
    private String deviceAdd;

    private TextView device_name = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_swipe);

        //get intent
        Intent intent = getIntent();
        filePath = intent.getStringExtra("post_img");
        device = intent.getExtras().getParcelable("device");
        deviceName = device.getName();
        deviceAdd = device.getAddress();
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        Bitmap thumbnail = BitmapFactory.decodeFile(filePath);

        isServer = false;

        // Gesture detection
        gestureDetector = new GestureDetector(this, new MyGestureDetector());
        gestureListener = new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                return gestureDetector.onTouchEvent(event);
            }
        };

        ImageView imageView = (ImageView) findViewById(R.id.swipeThumbnail);
        imageView.setImageBitmap(thumbnail);

        imageView.setOnClickListener(SwipeActivity.this);
        imageView.setOnTouchListener(gestureListener);

        device_name = (TextView) findViewById(R.id.device_name);
        device_name.setText(deviceName + " " + deviceAdd);
    }

    class MyGestureDetector extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            try {
                if (Math.abs(e1.getY() - e2.getY()) > SWIPE_MAX_OFF_PATH)
                    return false;
                // right to left swipe
                if(e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                    Toast.makeText(SwipeActivity.this, "Sending", Toast.LENGTH_SHORT).show();
                }  else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                    Toast.makeText(SwipeActivity.this, "Sending", Toast.LENGTH_SHORT).show();

                    //do something after detected as right swipe
                    Bitmap bitmap = BitmapFactory.decodeFile(filePath);
                    ConnectThread myConnection = new ConnectThread(device, bitmap, mBluetoothAdapter, MY_UUID);
                    myConnection.start();
                    ct = myConnection;
                    Log.d("Bluetooth Device: ", deviceName);

                    Intent indent = new Intent(SwipeActivity.this, LoginActivity.class);
                    startActivity(indent);
                }
            } catch (Exception e) {
                // nothing
            }
            return false;
        }

        @Override
        public boolean onDown(MotionEvent e) {
            return true;
        }
    }

    @Override
    public void onClick(View v) {
        Intent indent = new Intent(SwipeActivity.this, LoginActivity.class);
        startActivity(indent);
    }
}
