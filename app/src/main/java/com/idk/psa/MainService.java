package com.idk.psa;

import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.hardware.TriggerEvent;
import android.hardware.TriggerEventListener;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.location.ActivityRecognitionResult;
import com.google.android.gms.location.DetectedActivity;

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
                handleActionStart(intent);

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
    private void handleActionStart(Intent intent) {
        Log.d(this.getClass().getSimpleName(),"Start START");
        if (ActivityRecognitionResult.hasResult(intent)) {
            Log.d(this.getClass().getSimpleName(),"hasResult");
            ActivityRecognitionResult result = ActivityRecognitionResult.extractResult(intent);

            // Get the most probable activity from the list of activities in the update
            DetectedActivity mostProbableActivity = result.getMostProbableActivity();

            // Get the type of activity
            int activityType = mostProbableActivity.getType();

            if (activityType == DetectedActivity.ON_FOOT) {
                DetectedActivity betterActivity = walkingOrRunning(result.getProbableActivities());
                if (null != betterActivity)
                    mostProbableActivity = betterActivity;
                Log.i("S", "got into running");
                Intent i = new Intent("com.idk.psa.ACTIVITY_RECOGNITION_DATA");
                i.putExtra("Activity", getType(mostProbableActivity.getType()) );
                i.putExtra("Confidence", mostProbableActivity.getConfidence());
                sendBroadcast(i);
            } else if (activityType == DetectedActivity.RUNNING || activityType == DetectedActivity.WALKING) {
                Log.i("S", "got into "+ getType(result.getMostProbableActivity().getType()));
                Intent i = new Intent("com.idk.psa.ACTIVITY_RECOGNITION_DATA");
                i.putExtra("Activity", getType(result.getMostProbableActivity().getType()) );
                i.putExtra("Confidence", result.getMostProbableActivity().getConfidence());
                sendBroadcast(i);
            } else {
                Log.i("S", "got into "+getType(result.getMostProbableActivity().getType())+" in mainservice");
                Intent i = new Intent("com.idk.psa.ACTIVITY_RECOGNITION_DATA");
                i.putExtra("Activity", getType(result.getMostProbableActivity().getType()));
                i.putExtra("Confidence", result.getMostProbableActivity().getConfidence());
                sendBroadcast(i);
            }

            Log.d(this.getClass().getSimpleName(), "send broadcast");
        }
    }

    private String getType(int type){
        if(type == DetectedActivity.UNKNOWN)
            return "Unknown";
        else if(type == DetectedActivity.RUNNING)
            return "Running";
        else if(type == DetectedActivity.WALKING)
            return "Walking";
        else if(type == DetectedActivity.IN_VEHICLE)
            return "In Vehicle";
        else if(type == DetectedActivity.ON_BICYCLE)
            return "On Bicycle";
        else if(type == DetectedActivity.ON_FOOT)
            return "On Foot";
        else if(type == DetectedActivity.STILL)
            return "Still";
        else if(type == DetectedActivity.TILTING)
            return "Tilting";
        else
            return "";
    }

    private DetectedActivity walkingOrRunning(List<DetectedActivity> probableActivities) {
        DetectedActivity myActivity = null;
        int confidence = 0;
        for (DetectedActivity activity : probableActivities) {
            if (activity.getType() != DetectedActivity.RUNNING && activity.getType() != DetectedActivity.WALKING)
                continue;

            if (activity.getConfidence() > confidence)
                myActivity = activity;
        }

        return myActivity;
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
