package com.parse.unimelb.Helper;

import android.bluetooth.BluetoothDevice;
import android.os.Parcelable;

/**
 * Created by raymond on 10/11/15.
 */
public class BluetoothPair {
    private BluetoothDevice device;

    public BluetoothPair(BluetoothDevice device) {
        this.device = device;
    }

    public BluetoothDevice getDevice() {
        return this.device;
    }
}
