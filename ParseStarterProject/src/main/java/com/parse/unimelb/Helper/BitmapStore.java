package com.parse.unimelb.Helper;

import android.graphics.Bitmap;

/**
 * Created by raymond on 9/25/15.
 */
public class BitmapStore {
    static private Bitmap bmp = null;

    public static void setBitmap (Bitmap myBmp) {
        bmp = myBmp;
    }

    public static Bitmap getBitmap() {
        return bmp;
    }
}
