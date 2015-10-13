package com.parse.unimelb.Helper;

import java.io.File;
import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * This class provides a temporary store (ArrayList) for received image from bluetooth
 * Created by raymond on 10/13/15.
 */
public class BluetoothImageTempStore {
    private static ArrayList<String> bits = new ArrayList<>();

    public ArrayList<String> getArrayList() {
        return bits;
    }
}
