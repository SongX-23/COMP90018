package com.parse.unimelb;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by raymond on 10/8/15.
 */
public class BluetoothFragment extends Fragment {
    public static  final String ARG_OBJECT = "object";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View bluetooth = inflater.inflate(R.layout.fragment_bluetooth, container, false);
        return bluetooth;
    }
}
