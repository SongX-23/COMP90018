package com.parse.unimelb;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.parse.unimelb.Helper.BluetoothPair;

import java.util.ArrayList;

/**
 * Bluetooth pairs should be in the format of BluetoothPair, and be passed in as ArrayList
 * This class creates a dialog which lists all of the paired device
 * Created by raymond on 10/11/15.
 */
public class BluetoothDialog extends BaseAdapter {

    private ArrayList<BluetoothPair> listData;

    private LayoutInflater layoutInflater;

    public BluetoothDialog(Context context, ArrayList<BluetoothPair> listData) {
        this.listData = listData;
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return listData.size();
    }

    @Override
    public Object getItem(int position) {
        return listData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.list_row_dialog, null);
            holder = new ViewHolder();
            holder.nameView = (TextView) convertView.findViewById(R.id.name);
            holder.deviceView = (TextView) convertView.findViewById(R.id.device);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.nameView.setText(listData.get(position).getDevice().getName());
        holder.deviceView.setText(listData.get(position).getDevice().getAddress());

        return convertView;
    }

    static class ViewHolder {
        TextView nameView;
        TextView deviceView;
    }
}
