package com.vojtkovszky.rainbowhathub;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.util.Log;

import com.google.android.things.contrib.driver.apa102.Apa102;
import com.google.android.things.contrib.driver.bmx280.Bmx280SensorDriver;
import com.google.android.things.contrib.driver.button.Button;
import com.google.android.things.contrib.driver.ht16k33.AlphanumericDisplay;
import com.google.android.things.contrib.driver.pwmservo.Servo;
import com.google.android.things.contrib.driver.pwmspeaker.Speaker;
import com.google.android.things.pio.Gpio;

import java.io.IOException;

/**
 * Created by marcel on 2017-05-25.
 */

public class ComponentsManager {

    private static final String TAG = ComponentsManager.class.getSimpleName();

    private SensorManager mSensorManager;

    public enum Component {
        DISPLAY, LED_STRIP, SENSOR, BUTTON_LED_RED, BUTTON_LED_GREEN, BUTTON_LED_BLUE,
        BUTTON_A, BUTTON_B, BUTTON_C, SPEAKER, SERVO
    }

    private AlphanumericDisplay mDisplay;
    private Apa102 mLedstrip;
    private Bmx280SensorDriver mEnvironmentalSensorDriver;
    private Gpio mBlueLed;
    private Gpio mGreenLed;
    private Gpio mRedLed;
    private Speaker mPiezoSpeaker;
    private Servo mServo;
    private Button buttonA;
    private Button buttonB;
    private Button buttonC;

    public ComponentsManager(Context context) {
        mSensorManager = context.getSystemService(SensorManager.class);
    }

    //---------------------------
    // [BEGIN] Init components
    //---------------------------
    public void initAll() {
        initComponents(Component.values());
    }

    public void initComponents(Component... components) {
        for (Component component : components)
            initComponent(component);
    }

    private void initComponent(Component component) {
        try {
            switch (component) {
                case DISPLAY:
                    mDisplay = RainbowHat.openDisplay();
                    mDisplay.clear();
                    break;

                case LED_STRIP:
                    mLedstrip = RainbowHat.openLedStrip();
                    mLedstrip.write(new int[7]);
                    mLedstrip.setBrightness(0);
                    break;

                case SENSOR:
                    mEnvironmentalSensorDriver = RainbowHat.createSensorDriver();
                    // Register the drivers with the framework
                    mEnvironmentalSensorDriver.registerTemperatureSensor();
                    mEnvironmentalSensorDriver.registerPressureSensor();
                    break;

                case BUTTON_LED_RED:
                    mRedLed = RainbowHat.openLedRed();
                    mRedLed.setValue(false);
                    break;

                case BUTTON_LED_GREEN:
                    mGreenLed = RainbowHat.openLedGreen();
                    mGreenLed.setValue(false);
                    break;

                case BUTTON_LED_BLUE:
                    mBlueLed = RainbowHat.openLedBlue();
                    mBlueLed.setValue(false);
                    break;

                case BUTTON_A:
                    buttonA = RainbowHat.openButtonA();
                    break;

                case BUTTON_B:
                    buttonB = RainbowHat.openButtonB();
                    break;

                case BUTTON_C:
                    buttonC = RainbowHat.openButtonC();
                    break;

                case SPEAKER:
                    mPiezoSpeaker = RainbowHat.openPiezo();
                    break;

                case SERVO:
                    mServo = RainbowHat.openServo();
                    break;
            }
            Log.d(TAG, "Initialized " + component);
        }
        catch (IOException e) {
            Log.e(TAG, "Error initializing component " + component, e);
        }
    }
    //---------------------------
    // [END] Init components
    //---------------------------


    //---------------------------
    // [BEGIN] Close components
    //---------------------------
    public void closeAll() {
        closeComponents(Component.values());
    }

    public void closeComponents(Component... components) {
        for (Component component : components)
            closeComponent(component);
    }

    private void closeComponent(Component component) {
        try {
            switch (component) {
                case DISPLAY:
                    if (mDisplay != null) {
                        mDisplay.clear();
                        mDisplay.setEnabled(false);
                        mDisplay.close();
                    }
                    break;

                case LED_STRIP:
                    if (mLedstrip != null) {
                        mLedstrip.write(new int[7]);
                        mLedstrip.setBrightness(0);
                        mLedstrip.close();
                    }
                    break;

                case SENSOR:
                    if (mEnvironmentalSensorDriver != null) {
                        mEnvironmentalSensorDriver.close();
                    }
                    break;

                case BUTTON_LED_BLUE:
                    if (mBlueLed != null) {
                        mBlueLed.close();
                    }
                    break;

                case BUTTON_LED_GREEN:
                    if (mGreenLed != null) {
                        mGreenLed.close();
                    }
                    break;

                case BUTTON_LED_RED:
                    if (mRedLed != null) {
                        mRedLed.close();
                    }
                    break;

                case BUTTON_A:
                    if (buttonA != null) {
                        buttonA.close();
                    }
                    break;

                case BUTTON_B:
                    if (buttonB != null) {
                        buttonB.close();
                    }
                    break;

                case BUTTON_C:
                    if (buttonC != null) {
                        buttonC.close();
                    }
                    break;

                case SPEAKER:
                    if (mPiezoSpeaker != null) {
                        mPiezoSpeaker.close();
                    }
                    break;

                case SERVO:
                    if (mServo != null) {
                        mServo.close();
                    }
                    break;
            }
            Log.d(TAG, "Closed component " + component);
        }
        catch (IOException e) {
            Log.e(TAG, "Error closing component " + component, e);
        }
        finally {
            switch (component) {
                case DISPLAY: mDisplay = null;break;
                case LED_STRIP: mLedstrip = null; break;
                case SENSOR: mEnvironmentalSensorDriver = null; break;
                case BUTTON_LED_BLUE: mBlueLed = null; break;
                case BUTTON_LED_GREEN: mGreenLed = null; break;
                case BUTTON_LED_RED: mRedLed = null; break;
                case BUTTON_A: buttonA = null; break;
                case BUTTON_B: buttonB = null; break;
                case BUTTON_C: buttonC = null; break;
                case SPEAKER: mPiezoSpeaker = null; break;
                case SERVO: mServo = null; break;
            }
        }
    }
    //---------------------------
    // [END] Close components
    //---------------------------


    //---------------------------
    // [BEGIN] Component getters
    //---------------------------
    public AlphanumericDisplay getDisplay() {
        return mDisplay;
    }

    public Apa102 getLedstrip() {
        return mLedstrip;
    }

    public Bmx280SensorDriver getEnvironmentalSensorDriver() {
        return mEnvironmentalSensorDriver;
    }

    public Gpio getBlueLed() {
        return mBlueLed;
    }

    public Gpio getGreenLed() {
        return mGreenLed;
    }

    public Gpio getRedLed() {
        return mRedLed;
    }

    public Speaker getPiezoSpeaker() {
        return mPiezoSpeaker;
    }

    public Servo getServo() {
        return mServo;
    }

    public Button getButtonA() {
        return buttonA;
    }

    public Button getButtonB() {
        return buttonB;
    }

    public Button getButtonC() {
        return buttonC;
    }
    //---------------------------
    // [END] Component getters
    //---------------------------


    //---------------------------
    // [BEGIN] Component callbacks
    //---------------------------
    public void registerSensors(MainActivity activity) {
        // Register the BMP280 temperature sensor
        Sensor temperature = mSensorManager.getDynamicSensorList(Sensor.TYPE_AMBIENT_TEMPERATURE).get(0);
        mSensorManager.registerListener(activity, temperature,
                SensorManager.SENSOR_DELAY_NORMAL);

        // Register the BMP280 pressure sensor
        Sensor pressure = mSensorManager.getDynamicSensorList(Sensor.TYPE_PRESSURE).get(0);
        mSensorManager.registerListener(activity, pressure,
                SensorManager.SENSOR_DELAY_NORMAL);
    }

    public void unregisterSensors(MainActivity activity) {
        mSensorManager.unregisterListener(activity);
    }

    public void setButtonListeners(MainActivity activity) {
        buttonA.setOnButtonEventListener(activity);
        buttonB.setOnButtonEventListener(activity);
        buttonC.setOnButtonEventListener(activity);
    }
    //---------------------------
    // [END] Component callbacks
    //---------------------------
}
