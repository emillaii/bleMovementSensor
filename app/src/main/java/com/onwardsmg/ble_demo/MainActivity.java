package com.onwardsmg.ble_demo;

import android.bluetooth.BluetoothAdapter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.os.Handler;

import android.widget.Button;
import android.graphics.Color;

public class MainActivity extends AppCompatActivity {

    private BluetoothAdapter mBluetoothAdapter;
    private boolean mScanning;
    private Handler mHandler;
    private static final long SCAN_PERIOD = 5000000;
    private static final int ALARM_ON_DURATION = 10;
    private Button signalButton;
    private int loopCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mHandler = new Handler();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Initializes Bluetooth adapter.
        final BluetoothManager bluetoothManager =
                (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        mBluetoothAdapter = bluetoothManager.getAdapter();

        //Start to scan BLE device
        scanLeDevice(true);

        signalButton = findViewById(R.id.signalButton);
        signalButton.setBackgroundColor(Color.BLACK);
    }

    private BluetoothAdapter.LeScanCallback mLeScanCallback =
            new BluetoothAdapter.LeScanCallback() {
                @Override
                public void onLeScan(final BluetoothDevice device, int rssi,
                                     byte[] scanRecord) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            System.out.println(device.getAddress());
                            System.out.println(device.getName());
                            if (device.getName() != null && device.getName().contains("iSensor")){
                                loopCount = ALARM_ON_DURATION;
                            }
                            if (loopCount > 0)
                            {
                                signalButton.setBackgroundColor(Color.GREEN);
                            } else {
                                signalButton.setBackgroundColor(Color.BLACK);
                            }
                            if (loopCount > 0) loopCount--;
                        }
                    });
                }
            };

    private void scanLeDevice(final boolean enable) {
        if (enable) {
            // Stops scanning after a pre-defined scan period.
            /*mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mScanning = false;
                    mBluetoothAdapter.stopLeScan(mLeScanCallback);
                }
            }, SCAN_PERIOD);*/

            mScanning = true;
            mBluetoothAdapter.startLeScan(mLeScanCallback);
        } else {
            mScanning = false;
            mBluetoothAdapter.stopLeScan(mLeScanCallback);
        }
    }


}
