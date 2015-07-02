package com.example.jqzeng.bluetoothdetector;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jq.zeng on 5/6/15.
 */
public class BluetoothSignalAdapter  extends BaseAdapter {
    private final List<BluetoothSignal> mSignals = new ArrayList<BluetoothSignal>();
    private final Context mContext;

    private static final String TAG = "bluetoothSignalAdapter";


    public BluetoothSignalAdapter(Context context) {
        mContext = context;
    }

    public void add(BluetoothSignal bleSignal) {
        mSignals.add(bleSignal);
        notifyDataSetChanged();
    }

    public void clear() {
        mSignals.clear();
        notifyDataSetChanged();
    }

    public int getCount(){
        return mSignals.size();
    }

    public Object getItem(int pos) {
        return mSignals.get(pos);
    }

    public long getItemId(int pos) {
        return pos;
    }

    public void set(int pos, BluetoothSignal signal) {
        mSignals.set(pos, signal);
        notifyDataSetChanged();
    }

    static class ViewHolder{
        TextView deviceNameView;
        TextView signalStrengthView;
        TextView countView;
        TextView deviceIDView;
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        Log.i(TAG, "Get view");
        //get current BLEsignal
        final BluetoothSignal bleSignal = (BluetoothSignal) getItem(position);
        ViewHolder viewHolder;

        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.bluetooth_signals,null);
            viewHolder = new ViewHolder();
            viewHolder.deviceNameView = (TextView) convertView.findViewById(R.id.DeviceName);
            viewHolder.signalStrengthView = (TextView) convertView.findViewById(R.id.SignalStrength);
            viewHolder.countView = (TextView) convertView.findViewById(R.id.Count);
            viewHolder.deviceIDView = (TextView) convertView.findViewById(R.id.DeviceID);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder)convertView.getTag();
        }

        Log.i(TAG, "start to setText");

        final TextView deviceNameView = viewHolder.deviceNameView;
        deviceNameView.setText(bleSignal.getmBluetoothDeviceName());

        final TextView signalStrengthView = viewHolder.signalStrengthView;
        signalStrengthView.setText(" " + bleSignal.getmSignalStrength() + "dBm");

        final TextView countView = viewHolder.countView;
        countView.setText(" " + bleSignal.getmCount());

        final TextView deviceIDView = viewHolder.deviceIDView;
        deviceIDView.setText("UUID: "+bleSignal.getmDeviceID());

        return convertView;
    }
}
