package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.BatteryManager;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    public class BatteryLevelReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            if (action != null) {
                if (action.equals(Intent.ACTION_BATTERY_LOW)) {
                    // Pin ở mức yếu
                    mainLayout.setBackgroundColor(Color.YELLOW);
                } else if (action.equals(Intent.ACTION_BATTERY_OKAY)) {
                    // Pin ở mức ổn định
                    mainLayout.setBackgroundColor(Color.GREEN);
                }
            }
        }
    }
    public TextView batteryStatusTv;
    public TextView batteryChargingStatusTv;
    public LinearLayout mainLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        batteryStatusTv = (TextView) findViewById(R.id.batteryStatustv);
        batteryChargingStatusTv = (TextView) findViewById(R.id.chargingStatusTv);
        mainLayout = (LinearLayout) findViewById(R.id.mainLayout);

        IntentFilter ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        Intent batteryStatus = this.registerReceiver(null, ifilter);

        int level = batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
        int scale = batteryStatus.getIntExtra(BatteryManager.EXTRA_SCALE, -1);

        float batteryPct = level * 100 / (float)scale;
        batteryStatusTv.setText("Battery Level: " + batteryPct + "%");

        // Are we charging / charged?
        int status = batteryStatus.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
        boolean isCharging = status == BatteryManager.BATTERY_STATUS_CHARGING ||
                status == BatteryManager.BATTERY_STATUS_FULL;

        if (isCharging)
        {
            mainLayout.setBackgroundColor(Color.GREEN);
            batteryChargingStatusTv.setText("Đang sạc");
        }
        else {
            mainLayout.setBackgroundColor(Color.YELLOW);
            batteryChargingStatusTv.setText("Chưa kết nối bộ sạc");
        }


        boolean isNotFull = status == BatteryManager.BATTERY_STATUS_NOT_CHARGING &&
                batteryPct < 20;
        if (isNotFull)
        {
            mainLayout.setBackgroundColor(Color.RED);
            batteryChargingStatusTv.setText("Chưa kết nối bộ sạc");
        }


    }
}