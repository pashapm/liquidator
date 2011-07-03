package ru.hackaton.liquidator;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;

public class SelectTypeActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.select_type);
    }

    public void onVodkaClick(View view) {
        Intent intent = new Intent(this, DeviceListActivity.class);
        intent.putExtra("type", 0); // 0 - server, 1 - client
        startActivity(intent);
    }

    public void onGlassClick(View view) {
        Intent intent = new Intent(this, BluetoothActivity.class);
        intent.putExtra("type", 1); // 0 - server, 1 - client
        startActivity(intent);
    }
}
