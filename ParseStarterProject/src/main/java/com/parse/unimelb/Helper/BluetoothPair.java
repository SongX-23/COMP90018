package com.parse.unimelb.Helper;

import android.bluetooth.BluetoothDevice;
import android.os.Parcelable;

/**
 * Created by raymond on 10/11/15.
 * This class provides methods to retrieve the information about bluetooth pair
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
