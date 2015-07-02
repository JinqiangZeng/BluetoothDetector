package com.example.jqzeng.bluetoothdetector;

import android.content.Intent;

/**
 * Created by jq.zeng on 5/6/15.
 */
public class BluetoothSignal {
    private String mBluetoothDeviceName = new String();
    private int mSignalStrength = 0;
    private int mCount = 0;
    private String mDeviceID = new String();

    public final static String DEVICENAME = "devicename";
    public final static String SIGNAL = "signal";
    public final static String COUNT = "count";
    public final static String DEVID = "deviceid";

    BluetoothSignal(String bluetoothDeviceName, int signalStrength, int count, String deviceID) {
        this.mBluetoothDeviceName = bluetoothDeviceName;
        this.mSignalStrength = signalStrength;
        this.mCount = count;
        this.mDeviceID = deviceID;
    }

    BluetoothSignal(Intent intent) {
        mBluetoothDeviceName = intent.getStringExtra(BluetoothSignal.DEVICENAME);
        mDeviceID = intent.getStringExtra(BluetoothSignal.DEVID);
        mCount = intent.getIntExtra(BluetoothSignal.COUNT, 0);
        mSignalStrength = intent.getIntExtra(BluetoothSignal.SIGNAL, 0);
    }

    public String getmBluetoothDeviceName() {return mBluetoothDeviceName;}

    public void setmBluetoothDeviceName(String name) {
        mBluetoothDeviceName = name;
    }

    public int getmSignalStrength() {
        return mSignalStrength;
    }

    public void setmSignalStrength(int signalStrength){
        mSignalStrength = signalStrength;
    }

    public int getmCount() {
        return mCount;
    }

    public void setmCount(int count) {
        mCount = count;
    }

    public String getmDeviceID() {
        return mDeviceID;
    }

    public void setmDeviceID(String deviceID) {
        mDeviceID = deviceID;
    }
}
