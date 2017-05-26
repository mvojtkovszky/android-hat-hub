package com.vojtkovszky.rainbowhathub.hat;

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
import com.vojtkovszky.rainbowhathub.MainActivity;

import java.io.IOException;

/**
 * Created by mvojtkovszky on 2017-05-25.
 *
 * Class responsible for initializing, closing and retrieving all HAT's components
 * and registering components' callbacks
 */
public class ComponentsManager {

    private static final String TAG = ComponentsManager.class.getSimpleName();

    private SensorManager mSensorManager;

    public enum Component {
        DISPLAY,
        LED_STRIP,
        SENSORS,
        BUTTON_LED_BLUE,
        BUTTON_LED_GREEN,
        BUTTON_LED_RED,
        BUTTON_A,
        BUTTON_B,
        BUTTON_C,
        SPEAKER,
        SERVO
    }

    private AlphanumericDisplay display;
    private Apa102 ledStrip;
    private Bmx280SensorDriver sensors;
    private Gpio ledBlue;
    private Gpio ledGreen;
    private Gpio ledRed;
    private Speaker speaker;
    private Servo servo;
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
                    display = DriverFactory.openDisplay();
                    display.clear();
                    break;

                case LED_STRIP:
                    ledStrip = DriverFactory.openLedStrip();
                    ledStrip.write(new int[7]);
                    ledStrip.setBrightness(0);
                    break;

                case SENSORS:
                    sensors = DriverFactory.createSensorDriver();
                    // Register the drivers with the framework
                    sensors.registerTemperatureSensor();
                    sensors.registerPressureSensor();
                    break;

                case BUTTON_LED_RED:
                    ledRed = DriverFactory.openLedRed();
                    ledRed.setValue(false);
                    break;

                case BUTTON_LED_GREEN:
                    ledGreen = DriverFactory.openLedGreen();
                    ledGreen.setValue(false);
                    break;

                case BUTTON_LED_BLUE:
                    ledBlue = DriverFactory.openLedBlue();
                    ledBlue.setValue(false);
                    break;

                case BUTTON_A:
                    buttonA = DriverFactory.openButtonA();
                    break;

                case BUTTON_B:
                    buttonB = DriverFactory.openButtonB();
                    break;

                case BUTTON_C:
                    buttonC = DriverFactory.openButtonC();
                    break;

                case SPEAKER:
                    speaker = DriverFactory.openPiezo();
                    break;

                case SERVO:
                    servo = DriverFactory.openServo();
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
                    if (display != null) {
                        display.clear();
                        display.setEnabled(false);
                        display.close();
                    }
                    break;

                case LED_STRIP:
                    if (ledStrip != null) {
                        ledStrip.write(new int[7]);
                        ledStrip.setBrightness(0);
                        ledStrip.close();
                    }
                    break;

                case SENSORS:
                    if (sensors != null) {
                        sensors.close();
                    }
                    break;

                case BUTTON_LED_BLUE:
                    if (ledBlue != null) {
                        ledBlue.close();
                    }
                    break;

                case BUTTON_LED_GREEN:
                    if (ledGreen != null) {
                        ledGreen.close();
                    }
                    break;

                case BUTTON_LED_RED:
                    if (ledRed != null) {
                        ledRed.close();
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
                    if (speaker != null) {
                        speaker.close();
                    }
                    break;

                case SERVO:
                    if (servo != null) {
                        servo.close();
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
                case DISPLAY: display = null;break;
                case LED_STRIP: ledStrip = null; break;
                case SENSORS: sensors = null; break;
                case BUTTON_LED_BLUE: ledBlue = null; break;
                case BUTTON_LED_GREEN: ledGreen = null; break;
                case BUTTON_LED_RED: ledRed = null; break;
                case BUTTON_A: buttonA = null; break;
                case BUTTON_B: buttonB = null; break;
                case BUTTON_C: buttonC = null; break;
                case SPEAKER: speaker = null; break;
                case SERVO: servo = null; break;
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
        return display;
    }

    public Apa102 getLedstrip() {
        return ledStrip;
    }

    public Bmx280SensorDriver getSensors() {
        return sensors;
    }

    public Gpio getBlueLed() {
        return ledBlue;
    }

    public Gpio getGreenLed() {
        return ledGreen;
    }

    public Gpio getRedLed() {
        return ledRed;
    }

    public Speaker getSpeaker() {
        return speaker;
    }

    public Servo getServo() {
        return servo;
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
