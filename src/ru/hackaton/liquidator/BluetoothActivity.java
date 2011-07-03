package ru.hackaton.liquidator;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.*;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.SeekBar.OnSeekBarChangeListener;

public class BluetoothActivity extends Activity {

    private GlassView2 glassView1;
    private GlassView2 glassView2;
    private GlassView2 glassView3;


    // Debugging
    private static final String TAG = "BluetoothActivity";
    private static final boolean D = true;

    // Message types sent from the BluetoothService Handler
    public static final int MESSAGE_STATE_CHANGE = 1;
    public static final int MESSAGE_READ = 2;
    public static final int MESSAGE_WRITE = 3;
    public static final int MESSAGE_DEVICE_NAME = 4;
    public static final int MESSAGE_TOAST = 5;

    // Key names received from the BluetoothService Handler
    public static final String DEVICE_NAME = "device_name";
    public static final String TOAST = "toast";

    // Intent request codes
    private static final int REQUEST_CONNECT_DEVICE = 1;
    private static final int REQUEST_ENABLE_BT = 2;

    // Layout Views

    // Name of the connected device
    private String mConnectedDeviceName = null;
    // String buffer for outgoing messages

    // Local Bluetooth adapter
    private BluetoothAdapter mBluetoothAdapter = null;
    // Member object for the chat services
    private BluetoothService mService = null;


    private static boolean CLIENT = false;

    SimpleBottleView mView;
    SeekBar mVolumeSeeker;

    int bottleId;
    SoundPool mSounds;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        ScrProps.initialize(this);
        super.onCreate(savedInstanceState);
        if (D) Log.e(TAG, "+++ ON CREATE +++");

        // Set up the window layout
//        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);

        // Get local Bluetooth adapter
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        // If the adapter is null, then Bluetooth is not supported
        if (mBluetoothAdapter == null) {
            Toast.makeText(this, "Bluetooth is not available", Toast.LENGTH_LONG).show();
            finish();
            return;
        }


        mSounds = new SoundPool(1, AudioManager.STREAM_MUSIC, 1);
        bottleId = mSounds.load(this, R.raw.bulk, 1);


    }

    @Override
    public void onStart() {
        super.onStart();
        if (D) Log.e(TAG, "++ ON START ++");
        // If BT is not on, request that it be enabled.
        // setupChat() will then be called during getActivityResult
        if (!mBluetoothAdapter.isEnabled()) {
            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
            // Otherwise, setup the bluetooth session
        } else {
            if (mService == null) {
                // Initialize the BluetoothService to perform bluetooth connections
                mService = new BluetoothService(this, mHandler);
            }
        }
        Intent intent = getIntent();
        if (intent != null) {
            int type = intent.getIntExtra("type", -1);
            if (type != -1) {
                CLIENT = type == 1;
            } else {
                Log.e(TAG, "wtf???");
            }
            getActivityResult(CLIENT ? REQUEST_ENABLE_BT : REQUEST_CONNECT_DEVICE, Activity.RESULT_OK, intent);
        }
    }

    @Override
    public synchronized void onResume() {
        super.onResume();
        if (D) Log.e(TAG, "+ ON RESUME +");
        if (mService != null) {
            // Only if the state is STATE_NONE, do we know that we haven't started already
            if (mService.getState() == BluetoothService.STATE_NONE) {
                // Start the Bluetooth chat services
                mService.start();
            }
        }
    }

    void playBottle() {
        mSounds.play(bottleId, 1, 1, 1, 1, 1);
    }

    @Override
    public synchronized void onPause() {
        super.onPause();
        if (D) Log.e(TAG, "- ON PAUSE -");
//        if (!CLIENT) mView.stopSimulation();
    }

    @Override
    public void onStop() {
        super.onStop();
        if (D) Log.e(TAG, "-- ON STOP --");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // Stop the Bluetooth chat services
        if (mService != null) mService.stop();
        if (D) Log.e(TAG, "--- ON DESTROY ---");
    }

    private void ensureDiscoverable() {
        if (D) Log.d(TAG, "ensure discoverable");
        if (mBluetoothAdapter.getScanMode() !=
                BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE) {
            Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
            discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
            startActivity(discoverableIntent);
        }
    }

    private void sendXY(int[] x_y) {
        if (mService.getState() != BluetoothService.STATE_CONNECTED) {
            Toast.makeText(this, R.string.not_connected, Toast.LENGTH_SHORT).show();
            return;
        }

        // Get the x and y bytes and tell the BluetoothService to write
        byte[] send = BluetoothService.int2byte(x_y);
        mService.write(send);
    }

    int p1;

    // The Handler that gets information back from the BluetoothService
    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MESSAGE_STATE_CHANGE:
                    if (D) Log.i(TAG, "MESSAGE_STATE_CHANGE: " + msg.arg1);
                    switch (msg.arg1) {
                        case BluetoothService.STATE_CONNECTED:
                            final int[] angle = new int[1];
                            int position;
                            Log.d("!!!!", getString(R.string.title_connected_to));
                            Log.d(">>>", mConnectedDeviceName);
                            if (CLIENT) {
                                startGame();
                            } else {
                                setContentView(R.layout.bottle);
                                mView = (SimpleBottleView) findViewById(R.id.bottle);
                                mView.startSimulation();

                                mView.mBulkListener = new SimpleBottleView.OnBulkListener() {

                                    @Override
                                    public void bulk(int val, int val2) {

                                        sendXY(new int[]{(int) mView.mAngle, p1});
                                    }
                                };

                                mVolumeSeeker = (SeekBar) findViewById(R.id.seekBar2);
                                mVolumeSeeker.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

                                    @Override
                                    public void onStopTrackingTouch(SeekBar seekBar) {
                                    }

                                    @Override
                                    public void onStartTrackingTouch(SeekBar seekBar) {
                                    }

                                    @Override
                                    public void onProgressChanged(SeekBar seekBar, int progress,
                                                                  boolean fromUser) {
                                        mView.setVolumePercent(progress);
                                        p1 = progress;
                                        sendXY(new int[]{(int) mView.mAngle, p1});
                                    }
                                });
                            }
                            break;
                        case BluetoothService.STATE_CONNECTING:
                            Log.d("!!!!", getString(R.string.title_connecting));
                            break;
                        case BluetoothService.STATE_LISTEN:
                        case BluetoothService.STATE_NONE:
                            Log.d("!!!!", getString(R.string.title_not_connected));
                            break;
                    }
                    break;
                case MESSAGE_WRITE:
                    break;
                case MESSAGE_READ:
                    if (CLIENT) {
                        byte[] readBuf = (byte[]) msg.obj;
                        int[] x_y = BluetoothService.byte2int(readBuf);
                        Log.i("result", mConnectedDeviceName + ":  " + x_y[0] + " " + x_y[1]);
                        addItem(x_y[0], x_y[1]);
                    }
                    break;
                case MESSAGE_DEVICE_NAME:
                    // save the connected device's name
                    mConnectedDeviceName = msg.getData().getString(DEVICE_NAME);
                    Toast.makeText(getApplicationContext(), "Connected to "
                            + mConnectedDeviceName, Toast.LENGTH_SHORT).show();
                    break;
                case MESSAGE_TOAST:
                    Toast.makeText(getApplicationContext(), msg.getData().getString(TOAST),
                            Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

    public void getActivityResult(int requestCode, int resultCode, Intent data) {
        if (D) Log.d(TAG, "requestCode: " + requestCode + ", resultCode: " + resultCode + ", data: " + data);
        switch (requestCode) {
            case REQUEST_CONNECT_DEVICE:
                if (resultCode == Activity.RESULT_OK) {
                    String address = data.getStringExtra(DeviceListActivity.EXTRA_DEVICE_ADDRESS);

                    Log.d(TAG, "" + address);
                    BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(address);
                    mService.connect(device);
                }
                break;
            case REQUEST_ENABLE_BT:
                if (resultCode == Activity.RESULT_OK) {
                } else {
                    Log.d(TAG, "BT not enabled");
                    Toast.makeText(this, R.string.bt_not_enabled_leaving, Toast.LENGTH_SHORT).show();
                    finish();
                }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.option_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.scan:
                Intent serverIntent = new Intent(this, DeviceListActivity.class);
                startActivityForResult(serverIntent, REQUEST_CONNECT_DEVICE);
                return true;
            case R.id.discoverable:
                ensureDiscoverable();
                return true;
        }
        return false;
    }

    private void startGame() {
        setContentView(R.layout.three_glass_layout);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        glassView1 = ((GlassView2) findViewById(R.id.glass1));
        glassView2 = ((GlassView2) findViewById(R.id.glass2));
        glassView3 = ((GlassView2) findViewById(R.id.glass3));

    }

    private void addItem(final int angle, final int position) {
        View rootView = findViewById(android.R.id.content);
        rootView.post(new Runnable() {
            @Override
            public void run() {
                updateView(glassView1, position, angle);
                updateView(glassView2, position, angle);
                updateView(glassView3, position, angle);
            }
        });
    }

    private void updateView(GlassView2 glassView, int position, int angle) {
        position = position * 800 / 100;
        System.out.println("BluetoothActivity.addItem: " + angle + ", " + position);
        int left = glassView.getLeft();
        int right = glassView.getRight();
        glassView.setWaterPositionAndWidth(position - left, angle);
        if (left <= position && position <= right) {
            glassView.getGlass().add((int) (angle * 1.5));
        }
        glassView.invalidate();
    }

}