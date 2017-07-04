package com.vojtkovszky.rainbowhathub;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.os.Bundle;
import android.util.Log;

import com.google.android.things.contrib.driver.button.Button;
import com.vojtkovszky.rainbowhathub.handlers.RainbowBumpHandler;
import com.vojtkovszky.rainbowhathub.handlers.SpeakerTonesHandler;
import com.vojtkovszky.rainbowhathub.handlers.WorkingModeHandler;
import com.vojtkovszky.rainbowhathub.hat.ComponentsManager;

import java.io.IOException;

/**
 * Created by mvojtkovszky on 2017-05-26.
 *
 * Handling all interaction and user experience here:
 * The logic goes like this:
 * - When the app starts, everything is off
 * - Pressing any capacitive button will light the led above it and play feedback sound
 * - Pressing capacitive button A will toggle between different working modes,
 *   defined in {@link WorkingModeHandler.WorkingMode}, showing result on display
 * - Pressing capacitive button B will light up led strip with random colours.
 * - Pressing capacitive button C will..= // TODO
 */
public class MainActivity extends Activity implements
        SensorEventListener, WorkingModeHandler.OnModeChangedListener, Button.OnButtonEventListener {

    private static final String TAG = MainActivity.class.getSimpleName();

    private ComponentsManager componentsManager;

    private WorkingModeHandler workingModeHandler;
    private RainbowBumpHandler rainbowBumpHandler;
    private SpeakerTonesHandler speakerTonesHandler;

    private long currentUpdateTimestamp = 0;
    private static final long MIN_DISPLAY_REFRESH_TIME_MS = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        componentsManager = new ComponentsManager(this);
        componentsManager.initAll();

        workingModeHandler = new WorkingModeHandler(WorkingModeHandler.WorkingMode.MODE_OFF);
        rainbowBumpHandler = new RainbowBumpHandler(componentsManager.getLedstrip());
        speakerTonesHandler = new SpeakerTonesHandler(componentsManager.getSpeaker());

        workingModeHandler.setOnModeChangedListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        componentsManager.registerSensors(this);
        componentsManager.setButtonListeners(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        componentsManager.unregisterSensors(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        componentsManager.closeAll();
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        final float value = event.values[0];

        if (event.sensor.getType() == Sensor.TYPE_AMBIENT_TEMPERATURE) {
            if (workingModeHandler.getCurrentMode() == WorkingModeHandler.WorkingMode.MODE_TEMPERATURE) {
                updateDisplay(String.valueOf(value));
            }
        }

        if (event.sensor.getType() == Sensor.TYPE_PRESSURE) {
            if (workingModeHandler.getCurrentMode() == WorkingModeHandler.WorkingMode.MODE_PRESSURE) {
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
                    componentsManager.getDisplay().clear();
                    break;
                case MODE_TEMPERATURE:
                case MODE_PRESSURE:
                    break;
            }
            currentUpdateTimestamp = 0;
        }
        catch (IOException ignore) { }
    }

    @Override
    public void onButtonEvent(Button button, boolean pressed) {
        Log.d(TAG, "Clicked button " + button.toString() + " value " + pressed);

        try {
            if (pressed) speakerTonesHandler.playTone(440);
            else speakerTonesHandler.playTone(110);

            if (button.toString().equals(componentsManager.getButtonA().toString())) {
                componentsManager.getRedLed().setValue(pressed);
                if (!pressed) workingModeHandler.toggleMode();
            }
            else if (button.toString().equals(componentsManager.getButtonB().toString())) {
                componentsManager.getGreenLed().setValue(pressed);
                if (pressed) rainbowBumpHandler.startBumping();
                else rainbowBumpHandler.clearRainbow();

            }
            else if (button.toString().equals(componentsManager.getButtonC().toString())) {
                componentsManager.getBlueLed().setValue(pressed);
            }
        }
        catch (IOException e) {
            Log.e(TAG, "Error : ", e);
        }

    }

    private void updateDisplay(String value) {
        // don't let display refresh too often
        if (currentUpdateTimestamp > System.currentTimeMillis() - MIN_DISPLAY_REFRESH_TIME_MS) return;
        else currentUpdateTimestamp = System.currentTimeMillis();

        if (componentsManager.getDisplay() != null) {
            try {
                componentsManager.getDisplay().display(value);
            } catch (IOException e) {
                Log.e(TAG, "Error updating display", e);
            }
        }
    }
}
