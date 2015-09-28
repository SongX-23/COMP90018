package com.parse.unimelb;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.view.ViewGroup.LayoutParams;
import android.support.v7.widget.GridLayout;
import android.widget.ImageView;
import android.widget.ListAdapter;

/**
 * Created by songxue on 29/09/2015.
 */
public class ImageAdapter extends BaseAdapter {
    private Context mContext;

    public ImageAdapter(Context c){
        mContext = c;
    }

    public int getCount(){
        return thumbnails.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        if (convertView == null){
            imageView = new ImageView(mContext);
            imageView.setLayoutParams(new GridLayout.LayoutParams(85, 85));

            imageView.setPadding(8, 8, 8, 8);
        } else {
            imageView = (ImageView) convertView;
        }
        //set picture
        //imageView.setImageBitmap();
        return imageView;
    }

    //get the bitmaps
    private Bitmap[] thumbnails = {};
}
