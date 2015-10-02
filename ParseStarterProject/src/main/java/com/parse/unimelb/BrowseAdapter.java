package com.parse.unimelb;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;

/**
 * Created by songxue on 2/10/2015.
 */
public class BrowseAdapter extends BaseAdapter{
    private Context mContext;
    private ArrayList<Feed> feed_array;

    public BrowseAdapter(Context c, ArrayList<Feed> data) {
        mContext = c;
        feed_array = data;
    }

    @Override
    public int getCount() {
        return feed_array.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        return null;
    }
}
