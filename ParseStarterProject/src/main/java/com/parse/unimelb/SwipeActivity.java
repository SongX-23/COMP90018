package com.parse.unimelb;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.app.Activity;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.unimelb.R;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.spec.ECField;
import java.util.UUID;

public class SwipeActivity extends Activity implements View.OnClickListener {
    private static final int SWIPE_MIN_DISTANCE = 120;
    private static final int SWIPE_MAX_OFF_PATH = 250;
    private static final int SWIPE_THRESHOLD_VELOCITY = 200;
    private GestureDetector gestureDetector;
    View.OnTouchListener gestureListener;


    // Bluetooth instantiation
    private BluetoothAdapter mBTAdapter = BluetoothAdapter.getDefaultAdapter();
    private static final UUID myUUID = UUID.fromString("fa87c0d0-afac-11de-8a39-0800200c9a66");
    private static String address = "20:54:76:47:80:06";
    private ImageView imageView = null;

    private TextView device_name = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_swipe);

        //get intent
        Intent intent = getIntent();
        String filePath = intent.getStringExtra("post_img");
        String deviceName = intent.getStringExtra("device_info");

        Bitmap thumbnail = BitmapFactory.decodeFile(filePath);

        // Gesture detection
        gestureDetector = new GestureDetector(this, new MyGestureDetector());
        gestureListener = new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                return gestureDetector.onTouchEvent(event);
            }
        };

        imageView = (ImageView) findViewById(R.id.swipeThumbnail);
        imageView.setImageBitmap(thumbnail);

        imageView.setOnClickListener(SwipeActivity.this);
        imageView.setOnTouchListener(gestureListener);

        device_name = (TextView) findViewById(R.id.device_name);
        device_name.setText(deviceName);


        // Test if the bluetooth adapter is active
        // then enable bluetooth via intent
        if (mBTAdapter == null) {
            Toast.makeText(SwipeActivity.this, "This device does not support bluetooth!",
                    Toast.LENGTH_LONG).show();
            finish();
            return;
        }
        if (!mBTAdapter.isEnabled()) {
            Toast.makeText(SwipeActivity.this, "Please enable your bluetooth and try again.",
                    Toast.LENGTH_LONG).show();
            finish();
            return;
        }
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
                    SendDataThread sendDataThread = new SendDataThread();
                    sendDataThread.sendMessage();


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




    public class SendDataThread extends Thread {
        private BluetoothDevice device = null;
        private BluetoothSocket socket = null;
        private OutputStream outStream = null;

        public SendDataThread() {
            device = mBTAdapter.getRemoteDevice(address);
            try {
                socket = device.createRfcommSocketToServiceRecord(myUUID);
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(SwipeActivity.this, "Connection Failure", Toast.LENGTH_LONG).show();
            }
            mBTAdapter.cancelDiscovery();
            try {
                socket.connect();
            } catch (IOException e) {
                try {
                    socket.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
            Toast.makeText(SwipeActivity.this, "Connected to: " + device.getName(),
                    Toast.LENGTH_SHORT).show();
            try {
                outStream = socket.getOutputStream();
            } catch(IOException e) {
                e.printStackTrace();
            }
        }

        public void sendMessage() {
            try {
                mBTAdapter = BluetoothAdapter.getDefaultAdapter();
                Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.default_profile_image);
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                byte[] b = baos.toByteArray();
                outStream.write(b);
                outStream.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onClick(View v) {
        Intent indent = new Intent(SwipeActivity.this, LoginActivity.class);
        startActivity(indent);
    }
}
