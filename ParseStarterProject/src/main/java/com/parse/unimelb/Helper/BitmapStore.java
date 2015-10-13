package com.parse.unimelb.Helper;

import android.graphics.Bitmap;

/**
 * Created by raymond on 9/25/15.
 */
public class BitmapStore {
    static private Bitmap bmp = null;
    static private Bitmap rbmp = null;

    public static void setBitmap (Bitmap myBmp) {
        bmp = myBmp;
    }

    public static Bitmap getBitmap() {
        return bmp;
    }

    public static void setReceivedBitmap (Bitmap receivedBitmap) {
        rbmp = receivedBitmap;
    }

    public static Bitmap getReceivedBitmap () {
        return rbmp;
    }
}
