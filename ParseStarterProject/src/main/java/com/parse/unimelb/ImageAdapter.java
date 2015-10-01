package com.parse.unimelb;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListAdapter;

import java.util.ArrayList;

/**
 * Created by songxue on 29/09/2015.
 */
public class ImageAdapter extends BaseAdapter {
    private Context mContext;
    private ArrayList<Bitmap> image_array;

    public ImageAdapter(Context c, ArrayList<Bitmap> data){
            mContext = c;
            image_array = data;
//            Drawable myDrawable = c.getResources().getDrawable(R.drawable.default_profile_image);
//            Bitmap defaultImg = ((BitmapDrawable) myDrawable).getBitmap();
//            image_array.add(defaultImg);
    }

    public int getCount(){
        return image_array.size();
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
            imageView.setLayoutParams(new GridView.LayoutParams(400, 400));

            imageView.setPadding(8, 8, 8, 8);
        } else {
            imageView = (ImageView) convertView;
        }
        //set picture
        imageView.setImageBitmap(image_array.get(position));
        return imageView;
    }

    //get the bitmaps


    public ArrayList<Bitmap> getImage_array() {
        return image_array;
    }

    public void setImage_array(ArrayList<Bitmap> image_array) {
        this.image_array = image_array;
    }
}
