package com.idk.psa;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorEventListener2;
import android.hardware.SensorManager;
import android.hardware.TriggerEvent;
import android.hardware.TriggerEventListener;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.BounceInterpolator;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;
import java.util.zip.Inflater;


public class MainActivity2 extends FragmentActivity {
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

    private void RunFallingAnimation()
    {
        FrameLayout container = (FrameLayout) findViewById(R.id.container);
        container.removeAllViewsInLayout();
        View rootView = LayoutInflater.from(this).inflate(R.layout.pop_out_notification,container);
        TextView tv = (TextView) rootView.findViewById(R.id.pop_out_notif);

        if (tv == null) {
            Log.e("E", "tv null");
        }

        tv.setText("FALLING");

        tv.clearAnimation();
        TranslateAnimation translation;
        translation = new TranslateAnimation(0f, 0F, 0f, getDisplayHeight());
        translation.setStartOffset(10);
        translation.setDuration(300);
        translation.setFillAfter(true);
        translation.setInterpolator(new BounceInterpolator());
        tv.startAnimation(translation);
    }

    private int getDisplayHeight() {
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        return metrics.widthPixels;
    }

    private void RunImpactAnimation()
    {
        FrameLayout container = (FrameLayout) findViewById(R.id.container);
        container.removeAllViewsInLayout();
        View rootView = LayoutInflater.from(this).inflate(R.layout.pop_out_ouch,container);
        TextView tv = (TextView) rootView.findViewById(R.id.pop_out_ouch);

        if (tv == null) {
            Log.e("E", "tv null");
        }

        tv.setText("OUCH!");
        tv.clearAnimation();
        TranslateAnimation translation;
        translation = new TranslateAnimation(0f, 0F, 0f, 0);
        translation.setStartOffset(10);
        translation.setDuration(300);
        translation.setFillAfter(true);
        tv.startAnimation(translation);
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

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_main_activity2, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }

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
                // Toast.makeText(mContext, "FALLING " + String.valueOf(rootSumSquare), Toast.LENGTH_SHORT).show();
                RunImpactAnimation();
            } else if (rootSumSquare > 18.5) {
                // Toast.makeText(mContext, "IMPACT " + String.valueOf(rootSumSquare), Toast.LENGTH_SHORT).show();
                RunFallingAnimation();
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
