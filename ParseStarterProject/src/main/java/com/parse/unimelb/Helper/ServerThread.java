package com.parse.unimelb.Helper;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.widget.Toast;

import com.parse.unimelb.HomeActivity;

import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.OutputStream;
import java.util.UUID;

/**
 * Created by Rahul on 10/10/2015.
 */
public class ServerThread extends Thread {

    private final BluetoothServerSocket myServSocket;
    BluetoothAdapter mBluetoothAdapter;
    UUID MY_UUID;
    Bitmap bitmap ;
    SerialBitmap serialBitmap;
    private HomeActivity activity;


    // Sets the instance variables including the UUID for transmission.
    public ServerThread(BluetoothAdapter adapter, UUID uuid, HomeActivity activity) {
        mBluetoothAdapter = adapter;
        MY_UUID = uuid;
        BluetoothServerSocket tmp = null;
        this.activity = activity;

        // Sets up the RfcommnSocket which acts as a normal socket used in TCP connections using java.
        try {
            tmp = mBluetoothAdapter.listenUsingRfcommWithServiceRecord("myServer", MY_UUID);
        } catch (Exception e) {
            Log.e("Bluetooth", "Server establishing failed");
        }
        // Set the BluetoothServeer socket to enable us to accept incoming connections in the thread.
        myServSocket = tmp;
    }

    public void run() {
        Log.e("Bluetooth", "Begin waiting for connection");
        BluetoothSocket connectSocket = null;
        InputStream inStream = null;
        OutputStream outStream = null;
        String line = "";
        int byteNo;


        try {
            // this is a blocking call which waits till it gets a connection from a client.
            connectSocket = myServSocket.accept();
            inStream = connectSocket.getInputStream();

            // Once a connection has been achieved, it stops discovery for new devices.
            mBluetoothAdapter.cancelDiscovery();

            // The image is trasnferred after conversion into an object. Hence we require
            // ObjectOutputStream for receiving the data.
            ObjectInputStream objInStream = new ObjectInputStream(inStream);
            Object received = null;

            // This will continuously accept the incoming data from the client.
            while(true) {
                try {

                    // The object is received from the client.
                    received = objInStream.readObject();

                    // The data received is cast into the appropriate format, namely: SerialBitmap as
                    // the Bitmap object is not directly Serializable.
                    SerialBitmap receivedSerialBitmap = (SerialBitmap) received;
                    final Bitmap receivedBitmap = BitmapFactory.decodeByteArray(receivedSerialBitmap.blob, 0, receivedSerialBitmap.blob.length);

                    // This runs another thread to refresh the UI after getting the image from the client.
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
//                            activity.imageview.setImageBitmap(receivedBitmap);
//                            activity.imageview.refreshDrawableState();
                            Toast.makeText(activity, "You have received a photo, please refresh", Toast.LENGTH_LONG).show();
                        }
                    });
                    Log.e("Bluetooth", "Received Something");
                }catch(Exception e) {
                    e.printStackTrace();
                }

            }
        } catch (Exception e){
            Log.e("Bluetooth", "Connection Failed");
        }
    }
}