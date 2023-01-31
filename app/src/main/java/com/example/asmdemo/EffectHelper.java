package com.example.asmdemo;

import android.content.Context;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.lang.ref.WeakReference;

public class EffectHelper {
    private static String TAG = "EffectHelper";

    private final WeakReference<View> mTarget;

    public EffectHelper(View target) {
        mTarget = new WeakReference<>(target);
    }

    public void dispatchGenericMotionEvent(MotionEvent event) {
        Log.d(TAG, "dispatchGenericMotionEvent:" + event);
    }

    public void dispatchTouchEvent(MotionEvent event) {
        Log.d(TAG, "dispatchTouchEvent in EffectHelper:" + event.toString());
    }

    public void onWindowVisibilityChanged(int visibility) {
        Log.d(TAG, "onWindowVisibilityChanged:" + visibility);
    }

    public void onHoverChanged(boolean hovered) {
        Log.d(TAG, "onHoverChanged:" + hovered);
    }

    public void setPressed(boolean pressed) {
        Log.d(TAG, "setPressed:" + pressed);
    }

    public void onAttachedToWindow() {
        Log.d(TAG, "onAttachedToWindow in EffectHelper.");
    }

    public void onDetachedFromWindow() {
        Log.d(TAG, "onDetachedFromWindow in EffectHelper.");
    }

    public void setVibrateEnabled(boolean enabled) {
        Log.d(TAG, "setVibrateEnabled:" + enabled);
    }

    // 关闭音效功能
    public void setSoundEffectEnabled(boolean enabled) {
        Log.d(TAG, "setSoundEffectEnabled:" + enabled);
    }

}
