package com.vojtkovszky.rainbowhathub.handlers;

import android.graphics.Color;
import android.os.Handler;

import com.google.android.things.contrib.driver.apa102.Apa102;
import com.vojtkovszky.rainbowhathub.hat.DriverFactory;

import java.io.IOException;

/**
 * Created by mvojtkovszky on 2017-07-04.
 */

public class RainbowBumpHandler {

    private static final int LED_BUMP_DURATION_MS = 1500;

    private Apa102 ledStrip;
    private Handler handler = new Handler();
    private int currentLedIndex = 0;
    private int[] colors = new int[7];

    private volatile boolean bumpingInProgress = false;

    public RainbowBumpHandler(Apa102 ledStrip) {
        this.ledStrip = ledStrip;
    }

    public void startBumping() {
        bumpingInProgress = true;
        scheduleBump();
    }

    public void clearRainbow() {
        try {
            bumpingInProgress = false;
            currentLedIndex = 0;

            colors = new int[7];
            ledStrip.write(colors);
            ledStrip.setBrightness(0);
        }
        catch (IOException ignore) { }
    }

    private void bumpAnotherLed() {
        try {
            if (ledStrip.getBrightness() == 0)
                ledStrip.setBrightness(DriverFactory.DEFAULT_LEDSTRIP_BRIGHTNESS);

            colors[currentLedIndex] = Color.RED;
            ledStrip.write(colors);

            currentLedIndex++;

            scheduleBump();

        }
        catch (IOException ignore) { }
    }

    private void scheduleBump() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (bumpingInProgress && currentLedIndex < 7)
                    bumpAnotherLed();
            }
        }, LED_BUMP_DURATION_MS);
    }
}
