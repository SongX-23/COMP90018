package com.parse.unimelb;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.parse.unimelb.Helper.BitmapStore;
import com.parse.unimelb.R;

/**
 * Created by raymond on 10/8/15.
 */


public class PostFragment extends Fragment {
    public static  final String ARG_OBJECT = "object";
    private ImageView imageview = null;
    private Bitmap rawBitmap = null;
    private Bitmap thumbnail = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View post = inflater.inflate(R.layout.fragment_post, container, false);

        imageview = (ImageView) getActivity().findViewById(R.id.thumbnail);
        rawBitmap = BitmapStore.getBitmap();

        thumbnail = Bitmap.createScaledBitmap(rawBitmap, 64, 64, false);
        imageview.setImageBitmap(thumbnail);

        return post;
    }
}
