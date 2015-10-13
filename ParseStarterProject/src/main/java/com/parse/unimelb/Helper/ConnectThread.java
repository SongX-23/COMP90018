package com.parse.unimelb.Helper;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.graphics.Bitmap;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.UUID;

/**
 * Created by Rahul on 10/10/2015.
 */
public class ConnectThread extends Thread {

    private final BluetoothSocket mySocket;
    BluetoothAdapter mBluetoothAdapter;
    UUID MY_UUID;
    private InputStream inStream = null;
    private OutputStream outStream = null;
    Bitmap bitmap;
    private byte[] imgBuffer = new byte[8192];
    ObjectOutputStream objectOutputStream;

    // Set the instance variables including the UUID to set the code to enable communication.
    public ConnectThread(BluetoothDevice device, Bitmap bitmap,BluetoothAdapter adapter, UUID uuid) {
        this.bitmap=bitmap;
        mBluetoothAdapter = adapter;
        MY_UUID = uuid;
        BluetoothSocket tmp = null;

        // Create an RF connection socket using the UUID, similar to a normal Java socket for TCP.
        // RfcommnSocket is the part which actually establishes the connection with the server.
        try {
            tmp = device.createRfcommSocketToServiceRecord(MY_UUID);
        } catch (Exception e){
            Log.e("Bluetooth", "Could not connect");
        }
        mySocket = tmp;
    }


    // USed to transmit an image from the client to the server.
    public void sendText() {
        try {

            // Convert the image into a serializable object by using SerialBitmap,
            // as graphics.Bitmap is not serializable.
            SerialBitmap serialBitmap = new SerialBitmap(bitmap, "name", 1);
            Log.e("SB",serialBitmap.filename);
            // byte[] blob=bmpToBlob(bitmap);

            // After converting to bitmap, transmit the data directly
            objectOutputStream.writeObject(serialBitmap);
            objectOutputStream.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    // This runs the method to transmit the data in a while loop. As soon as this thread is run, the
    // image starts to pass from client to server.
    public void run(){

        // This stops dicovery for new devices stating that the connection has been set between two
        // devices and that it should initiate the process of transmission.
        mBluetoothAdapter.cancelDiscovery();

        try{
            mySocket.connect();
        } catch (Exception e){
            Log.e("Bluetooth", this.getName() + ": Could not establish connection with device");
            try{
                mySocket.close();
            } catch (Exception e1){
                Log.e("Bluetooth", this.getName() + ": could not close socket", e1);
                this.destroy();
            }
        }
        try{
            inStream = mySocket.getInputStream();
            outStream = mySocket.getOutputStream();

            // The image is transferred as an object and is sent via ObjectOutputStream
            objectOutputStream = new ObjectOutputStream((outStream));

            // To transmit the data continuously after every 10 seconds.
            // TODO: how to end the client thread after transmission

            sendText();
        } catch (Exception e3){
            Log.e("Bluetooth", "Disconnected");
        }
    }

    public void cancel(){
        try{
            mySocket.close();
        } catch (Exception e){
            Log.e("Bluetooth", this.getName() + ": Could not close the socket");
        }
    }
}