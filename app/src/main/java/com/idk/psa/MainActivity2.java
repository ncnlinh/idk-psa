package com.idk.psa;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorEventListener2;
import android.hardware.SensorManager;
import android.hardware.TriggerEvent;
import android.hardware.TriggerEventListener;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;
import android.widget.Toast;

import java.util.List;


public class MainActivity2 extends ActionBarActivity {
    private SensorManager mSensorManager;
    private Sensor mSensor;
    private AcceListener mAcceListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_activity2);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        if ((mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)) != null) {
            Log.d(this.getClass().getSimpleName(), "have sensor");
        } else {
            List<Sensor> sensors = mSensorManager.getSensorList(Sensor.TYPE_ALL);
            for (int i = 0; i < sensors.size(); i++) {
                Log.d("sensors", sensors.get(i).getName());
            }
            Log.d(this.getClass().getSimpleName(), "dont have sensor");
        }


    }

    @Override
    protected void onResume() {
        super.onResume();
        mAcceListener = new AcceListener(this);
        mSensorManager.registerListener(mAcceListener, mSensor, SensorManager.SENSOR_DELAY_FASTEST);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(mAcceListener);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main_activity2, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private class AcceListener implements SensorEventListener {
        private Context mContext;
        public AcceListener(Context context){
            mContext = context;
        }
        @Override
        public void onSensorChanged(SensorEvent event) {
            float gravity = SensorManager.STANDARD_GRAVITY;
            double rootSumSquare = Math.sqrt(event.values[0] * event.values[0] +
                    event.values[1] * event.values[1] +
                    (event.values[2]) * (event.values[2]));
            if (rootSumSquare < 2.5) {
                Toast.makeText(mContext, "FALLING " + String.valueOf(rootSumSquare), Toast.LENGTH_SHORT).show();
            } else if (rootSumSquare > 18.5) {
                Toast.makeText(mContext, "IMPACT " + String.valueOf(rootSumSquare), Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
}

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main_activity2, container, false);
            return rootView;
        }
    }
}
