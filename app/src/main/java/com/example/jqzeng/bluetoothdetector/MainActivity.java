package com.example.jqzeng.bluetoothdetector;

import android.app.ListActivity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends ListActivity {

    private BluetoothAdapter mBluetoothAdapter;
    private final String TAG = "BLE Detector";

    private static final int MENU_BLEON = Menu.FIRST;
    private static final int MENU_CLEAR = Menu.FIRST + 1;
    private static final int REQUEST_ENABLE_BT = 1;
    private static boolean autoScan = true;
    private TextView mBluetoothStrength;

    BluetoothSignalAdapter mAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        registerReceiver(receiver, new IntentFilter(BluetoothDevice.ACTION_FOUND));
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

       // setContentView(R.layout.activity_main)
        mAdapter = new BluetoothSignalAdapter(getApplicationContext());

        getListView().setFooterDividersEnabled(true);
        TextView footerView = (TextView) getLayoutInflater().inflate(R.layout.footer_view, null);

        getListView().addFooterView(footerView);
        footerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "Clicked signal");

                if(autoScan) {
                    autoScan = false;
                    new Thread(new Runnable(){
                        public void run() {
                           // while(true){
                                if (mBluetoothAdapter != null && mBluetoothAdapter.isEnabled()) {
                                    Log.i(TAG, "start discovery");
                                    mBluetoothAdapter.startDiscovery();
                                } else
                                    Toast.makeText(getApplicationContext(), "bluetooth is disable", Toast.LENGTH_SHORT).show();
                                try {
                                    Thread.sleep(2000);
                                    if (mBluetoothAdapter != null && mBluetoothAdapter.isEnabled()) {
                                        mBluetoothAdapter.cancelDiscovery();
                                        Log.i(TAG, "cancel discovery");
                                    }
                                }catch (InterruptedException e) {
                                    Log.e(TAG, e.toString());
                                }
                            }

                       // }
                    },"BloothtoothDiscovery") {
                    }.start();
                }
            }
        });


       setListAdapter(mAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        super.onCreateOptionsMenu(menu);
        menu.add(Menu.NONE, MENU_BLEON, Menu.NONE, "BLE On/Off");
        menu.add(Menu.NONE, MENU_CLEAR, Menu.NONE, "Clear All");

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        switch (id) {
            case MENU_BLEON:
                if (mBluetoothAdapter == null) {
                    Log.e(TAG, "bluetooth Not support");
                } else if (!mBluetoothAdapter.isEnabled()) {

                    Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                    startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
                } else {
                    mBluetoothAdapter.disable();
                }
                return true;
            case MENU_CLEAR:
                Log.i(TAG, "Clear");
                mAdapter.clear();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_ENABLE_BT) {
            if (resultCode == RESULT_OK) {
                //mBluetoothStrength.setText("start bluetooth successful");
                Toast.makeText(getApplicationContext(), "start bluetooth successful", Toast.LENGTH_SHORT).show();
            }
        }

    }

    private final BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                int rssi = intent.getShortExtra(BluetoothDevice.EXTRA_RSSI, Short.MIN_VALUE);
                String name = intent.getStringExtra(BluetoothDevice.EXTRA_NAME);
                String devID = intent.getStringExtra(BluetoothDevice.EXTRA_DEVICE);
                Boolean flag = false;

                for(int i = 0; i < mAdapter.getCount(); i++){
                    BluetoothSignal signal = (BluetoothSignal)mAdapter.getItem(i);
                    if ( name.equals(signal.getmBluetoothDeviceName())) {
                        signal.setmCount(((BluetoothSignal) mAdapter.getItem(i)).getmCount() + 1);
                        signal.setmSignalStrength(rssi);
                        mAdapter.set(i, signal);
                        flag = true;
                    } else {
                        Log.i(TAG,signal.getmBluetoothDeviceName() + "!=" + name );
                    }
                }
                if (!flag) {
                    BluetoothSignal bluetoothSignal = new BluetoothSignal(name, rssi, 1, devID);
                    mAdapter.add(bluetoothSignal);
                }
                //Toast.makeText(getApplicationContext(), "  " + name + " : " + rssi + "dBm", Toast.LENGTH_SHORT).show();
                Log.i(TAG,"  " + name + "("+ devID +") : " + rssi + "dBm");
            }
        }
    };
}
