package com.idk.psa;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.hardware.TriggerEvent;
import android.hardware.TriggerEventListener;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class MainService extends IntentService {
    private static final String ACTION_START = "com.idk.psa.action.START";
    private static final String ACTION_BAZ = "com.idk.psa.action.BAZ";

    private static final String EXTRA_PARAM1 = "com.idk.psa.extra.PARAM1";
    private static final String EXTRA_PARAM2 = "com.idk.psa.extra.PARAM2";

    private List<Long> latestStepsTime;
    public static void startService(Context context) {
        Intent intent = new Intent(context, MainService.class);
        intent.setAction(ACTION_START);
        context.startService(intent);
    }

    public static void startActionBaz(Context context, String param1, String param2) {
        Intent intent = new Intent(context, MainService.class);
        intent.setAction(ACTION_BAZ);
        intent.putExtra(EXTRA_PARAM1, param1);
        intent.putExtra(EXTRA_PARAM2, param2);
        context.startService(intent);
    }

    public MainService() {
        super("MainService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_START.equals(action)) {
                handleActionStart();
            } else if (ACTION_BAZ.equals(action)) {
                final String param1 = intent.getStringExtra(EXTRA_PARAM1);
                final String param2 = intent.getStringExtra(EXTRA_PARAM2);
                handleActionBaz(param1, param2);
            }
        }
    }

    /**
     * Handle action Foo in the provided background thread with the provided
     * parameters.
     */
    private void handleActionStart() {
        SensorManager sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        Sensor runningSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR);
        latestStepsTime = new ArrayList<>(5);
        TriggerEventListener triggerEventListener = new StepDetectorEventListener();
        sensorManager.requestTriggerSensor(triggerEventListener, runningSensor);
    }

    private void openRunningScreen() {
        //TODO
    }

    private class StepDetectorEventListener extends TriggerEventListener {
        @Override
        public void onTrigger(TriggerEvent triggerEvent) {
            latestStepsTime.add(new Date().getTime());
            Log.d("Running",latestStepsTime.get(latestStepsTime.size()-1).toString());
            if (latestStepsTime.size()>1){
                if (latestStepsTime.get(latestStepsTime.size()-1) - latestStepsTime.get(latestStepsTime.size()-2) < 15) {
                    Toast.makeText(getApplicationContext(),"Running",Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    /**
     * Handle action Baz in the provided background thread with the provided
     * parameters.
     */
    private void handleActionBaz(String param1, String param2) {
        // TODO: Handle action Baz
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
