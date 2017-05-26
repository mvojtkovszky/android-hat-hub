package com.vojtkovszky.rainbowhathub;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.os.Bundle;
import android.util.Log;

import com.google.android.things.contrib.driver.button.Button;
import com.vojtkovszky.rainbowhathub.hat.ComponentsManager;

import java.io.IOException;

/**
 * Created by mvojtkovszky on 2017-05-26.
 *
 */
public class MainActivity extends Activity implements
        SensorEventListener, WorkingModeHandler.OnModeChangedListener, Button.OnButtonEventListener {

    private static final String TAG = MainActivity.class.getSimpleName();

    private ComponentsManager mComponentsManager;
    private WorkingModeHandler mWorkingModeHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.d(TAG, "Weather Station Started");

        mComponentsManager = new ComponentsManager(this);
        mComponentsManager.initAll();

        mWorkingModeHandler = new WorkingModeHandler(WorkingModeHandler.WorkingMode.MODE_OFF);
        mWorkingModeHandler.setOnModeChangedListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mComponentsManager.registerSensors(this);
        mComponentsManager.setButtonListeners(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mComponentsManager.unregisterSensors(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mComponentsManager.closeAll();
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        final float value = event.values[0];

        if (event.sensor.getType() == Sensor.TYPE_AMBIENT_TEMPERATURE) {
            if (mWorkingModeHandler.getCurrentMode() == WorkingModeHandler.WorkingMode.MODE_TEMPERATURE) {
                updateDisplay(String.valueOf(value));
            }
        }

        if (event.sensor.getType() == Sensor.TYPE_PRESSURE) {
            if (mWorkingModeHandler.getCurrentMode() == WorkingModeHandler.WorkingMode.MODE_PRESSURE) {
                updateDisplay(String.valueOf(value));
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        Log.d(TAG, "accuracy changed: " + accuracy + "\nfor sensor: " + sensor.getName());
    }

    @Override
    public void onWorkingModeChanged(WorkingModeHandler.WorkingMode mode) {
        try {
            switch (mode) {
                case MODE_OFF:
                    mComponentsManager.getDisplay().clear();
                    break;
                case MODE_TEMPERATURE:

                    break;
                case MODE_PRESSURE:

                    break;
            }
        }
        catch (IOException ignore) { }
    }

    @Override
    public void onButtonEvent(Button button, boolean b) {
        Log.d(TAG, "Clicked button " + button.toString() + " value " + b);

        try {
            if (button.toString().equals(mComponentsManager.getButtonA().toString())) {
                mComponentsManager.getRedLed().setValue(b);
            } else if (button.toString().equals(mComponentsManager.getButtonB().toString())) {
                mComponentsManager.getGreenLed().setValue(b);
            } else if (button.toString().equals(mComponentsManager.getButtonC().toString())) {
                mComponentsManager.getBlueLed().setValue(b);
            }

            if (!b)
                mWorkingModeHandler.toggleMode();
        }
        catch (IOException e) {
            Log.e(TAG, "Error : ", e);
        }

    }

    private void updateDisplay(String value) {
        if (mComponentsManager.getDisplay() != null) {
            try {
                mComponentsManager.getDisplay().display(value);
            } catch (IOException e) {
                Log.e(TAG, "Error updating display", e);
            }
        }
    }
}
