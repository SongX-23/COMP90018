package com.parse.unimelb.Helper;

import android.graphics.Bitmap;

/**
 * Created by raymond on 10/13/15.
 */
public class ReceivedBitmap {
    private Bitmap receivedBitmap = null;

    public ReceivedBitmap(Bitmap rbmp) {
        this.receivedBitmap = rbmp;
    }

    public Bitmap getReceivedBitmap() {
        return receivedBitmap;
    }
}
