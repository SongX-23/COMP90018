package com.muaranauli.db;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;

import com.muaranauli.utils.Info;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by JOHANESG1508 on 9/29/2015.
 */
public class ImageInstance {
    protected String imageName;
    protected byte[] imageBlob=null;

    // Construct image instance using it byte array and name
    public ImageInstance(String name,byte[] blob) {
        imageName=name;
        imageBlob=blob;
    }

    // Construct image instance using a bitmap and its name
    public ImageInstance(String name,Bitmap bmp) {
        imageName=name;
        imageBlob=bmpToBlob(bmp);
    }

    public static byte[] bmpToBlob(Bitmap bmp) {
        byte[] blob;
        ByteArrayOutputStream baos=new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG,0,baos);
        blob=baos.toByteArray();
        bmp.recycle();
        try {
            baos.close();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
        return blob;
    }

    //    // retrieve all image instances from data base
    public static ArrayList<ImageInstance> getImages(Cursor cursor) {
        ArrayList<ImageInstance> images=new ArrayList<>();
        int index1,index2;
        String name;
        byte[] blob;

        while (cursor.moveToNext()) {
            index1=cursor.getColumnIndex(DBImageAdapter.KEY_NAME);
            name=cursor.getString(index1);
            index2=cursor.getColumnIndex(DBImageAdapter.KEY_IMAGE);
            blob=cursor.getBlob(index2);

            images.add(new ImageInstance(name,blob));
        }

        cursor.close();

        return images;
    }

    public Bitmap getImageBmp() {
        return BitmapFactory.decodeByteArray(imageBlob,0,imageBlob.length);
    }


    public String getImageName() {
        return imageName;
    }

    public byte[] getImageBlob() {
        return imageBlob;
    }

}
