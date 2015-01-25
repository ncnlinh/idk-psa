package com.idk.psa;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.BatteryManager;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by kenhualiew on 25/1/15.
 */
public class PowerConnectionReceiver extends BroadcastReceiver{
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("debug", "see if this class is running");

        int status = intent.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
        Toast.makeText(context, "onReceive class", Toast.LENGTH_LONG).show();
        boolean isCharging = status == BatteryManager.BATTERY_STATUS_CHARGING ||
                status == BatteryManager.BATTERY_STATUS_FULL;

        int chargePlug = intent.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1);
        boolean usbCharge = chargePlug == BatteryManager.BATTERY_PLUGGED_USB;
        boolean acCharge = chargePlug == BatteryManager.BATTERY_PLUGGED_AC;

        if (isCharging){
            Toast.makeText(context, "Eating", Toast.LENGTH_LONG).show();
            Log.d("debug", "It is charging and showing?");
        }
        if (usbCharge){
            Toast.makeText(context, "Eating with USB", Toast.LENGTH_LONG).show();
            Log.d("debug", "Charging with USB");
        }
        if(acCharge){
            Toast.makeText(context, "Eating with AC", Toast.LENGTH_LONG).show();
            Log.d("debug", "Charging with AC");
        }
    }
}
