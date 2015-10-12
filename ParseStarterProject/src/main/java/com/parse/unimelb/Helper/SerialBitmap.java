package com.parse.unimelb.Helper;


import android.graphics.Bitmap;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Serializable;


public class SerialBitmap implements Serializable {

    // This is used to set the version number for the serialization
    // which is used during serialization and de-serialization to ensure
    // that the client and server have loaded the classes for that object
    // and are compatible with respect to serialization.
    private static final long serialVersionUID = 1L;

    public String filename;
    public long timestamp;
    public byte[] blob;

    // Set the instance variables. TimeStamp is not exactly useful.
    public SerialBitmap(Bitmap img,String aFile,long aTimestamp){
        blob = bmpToBlob(img);
        filename=aFile;
        timestamp=aTimestamp;
    }

    // Used to convert into an Array of bytes for transmission and serialization.
    public byte[] bmpToBlob(Bitmap bmp) {
        // byte[] blob;
        ByteArrayOutputStream baos=new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG,0,baos);
        blob=baos.toByteArray();
        //bmp.recycle();
        try {
            baos.close();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
        return blob;
    }
}
