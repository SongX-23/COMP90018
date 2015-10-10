package com.parse.unimelb.Helper;

/**
 * Created by raymond on 10/11/15.
 */
public class BluetoothPair {
    private String name;
    private String device;

    public BluetoothPair(String name, String device) {
        this.name = name;
        this.device = device;
    }

    public String getName() {
        return this.name;
    }

    public String getDevice(){
        return this.device;
    }
}
