package com.parse.unimelb;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.Toast;

import com.parse.unimelb.R;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

public class ReceivingActivity extends Activity {

    // Bluetooth instantiation
    private BluetoothAdapter mBTAdapter = BluetoothAdapter.getDefaultAdapter();
    private static final UUID myUUID = UUID.fromString("fa87c0d0-afac-11de-8a39-0800200c9a66");
    private static String address = "20:54:76:47:80:06";
    private byte[] buffer = new byte[8192];
    private ImageView imageView = null;

    Bitmap bitmap = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receiving);

        bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.default_profile_image);

        imageView = (ImageView) findViewById(R.id.imageview);
        imageView.setImageBitmap(bitmap);


        if (mBTAdapter == null) {
            Toast.makeText(ReceivingActivity.this, "This device does not support bluetooth!",
                    Toast.LENGTH_LONG).show();
//            ReceivingActivity.this.finish();
        }
        if (!mBTAdapter.isEnabled()) {
            Toast.makeText(ReceivingActivity.this, "Please enable your bluetooth and try again.",
                    Toast.LENGTH_LONG).show();
//            ReceivingActivity.this.finish();
        } else {
            AcceptDataThread acceptDataThread = new AcceptDataThread();
            acceptDataThread.start();
            bitmap = BitmapFactory.decodeByteArray(buffer, 0, buffer.length);
            imageView.setImageBitmap(bitmap);
        }
    }


    class AcceptDataThread extends Thread {
        private BluetoothServerSocket mServerSocket;
        private BluetoothSocket socket = null;
        private InputStream inStream;
        private String device;

        public AcceptDataThread() {
            BluetoothServerSocket tmp = null;
            try {
                tmp = mBTAdapter.listenUsingRfcommWithServiceRecord("Bluetooth", myUUID);
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(ReceivingActivity.this, "Connection Failure", Toast.LENGTH_LONG).show();
            }

            mServerSocket = tmp;
            try {
                socket = mServerSocket.accept();
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(ReceivingActivity.this, "Connection Failure", Toast.LENGTH_LONG).show();
            }

            device = socket.getRemoteDevice().getName();
            Toast.makeText(ReceivingActivity.this, "Connect to: " + device, Toast.LENGTH_SHORT).show();
            InputStream tmpInStream = null;
            try {
                tmpInStream = socket.getInputStream();
            } catch (IOException e) {
                e.printStackTrace();
            }

            inStream = tmpInStream;
            int byteNo;
            try {
                byteNo = inStream.read(buffer);
                if (byteNo != -1) {
                    int byteNo2 = byteNo;
                    int bufferSize = 7340;
                    while(byteNo2 != bufferSize) {
                        bufferSize = bufferSize - byteNo2;
                        byteNo2 = inStream.read(buffer, byteNo, bufferSize);
                        if (byteNo2 == -1) {
                            break;
                        }
                        byteNo = byteNo + byteNo2;
                    }
                }
                if (socket != null) {
                    try {
                        mServerSocket.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
