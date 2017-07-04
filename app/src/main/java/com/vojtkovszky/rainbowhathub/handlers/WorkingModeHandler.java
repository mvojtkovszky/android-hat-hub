package com.vojtkovszky.rainbowhathub.handlers;

import android.util.Log;

/**
 * Created by mvojtkovszky on 2017-05-25.
 *
 * Handling the logic of current active mode.
 */

public class WorkingModeHandler {

    private static final String TAG = WorkingModeHandler.class.getSimpleName();

    public enum WorkingMode {
        MODE_OFF,
        MODE_TEMPERATURE,
        MODE_PRESSURE
    }

    private WorkingMode mCurrentMode;
    private OnModeChangedListener mListener;

    public WorkingModeHandler(WorkingMode mode) {
        mCurrentMode = mode;
    }

    public void setOnModeChangedListener(OnModeChangedListener listener) {
        mListener = listener;
    }

    public void setMode(WorkingMode mode) {
        Log.d(TAG, "Working mode changed to " + mode);

        mCurrentMode = mode;

        if (mListener != null)
            mListener.onWorkingModeChanged(mode);
    }

    public void toggleMode() {
        int currentMode = mCurrentMode.ordinal() + 1;
        if (currentMode == WorkingMode.values().length)
            currentMode = 0;

        setMode(WorkingMode.values()[currentMode]);
    }

    public WorkingMode getCurrentMode() {
        return mCurrentMode;
    }

    public interface OnModeChangedListener {
        void onWorkingModeChanged(WorkingMode mode);
    }
}
