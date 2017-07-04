package com.vojtkovszky.rainbowhathub.handlers;

import android.os.Handler;

import com.google.android.things.contrib.driver.pwmspeaker.Speaker;

import java.io.IOException;

/**
 * Created by mvojtkovszky on 2017-07-04.
 */

public class SpeakerTonesHandler {

    private static final int DEFAULT_TONE_DURATION_MS = 50;
    private Speaker speaker;

    public SpeakerTonesHandler(Speaker speaker) {
        this.speaker = speaker;
    }

    public void playTone(double frequency) {

        try {
            speaker.play(frequency);
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    stopPlaying();
                }
            }, DEFAULT_TONE_DURATION_MS);
        }
        catch (IOException ignore) { }
    }

    private void stopPlaying() {
        try {
            speaker.stop();
        }
        catch (IOException ignore) { }
    }
}
