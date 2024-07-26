package com.example.asmdemo;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class EffectButtonJavaCode extends androidx.appcompat.widget.AppCompatButton {

    public EffectButtonJavaCode(@NonNull Context context) {
        super(context);
    }

    public EffectButtonJavaCode(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public EffectButtonJavaCode(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean dispatchGenericMotionEvent(MotionEvent event) {
        getOSUIEffectHelper().dispatchGenericMotionEvent(event);
        return super.dispatchGenericMotionEvent(event);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        getOSUIEffectHelper().dispatchTouchEvent(event);
        return super.dispatchTouchEvent(event);
    }

    @Override
    protected void onAttachedToWindow() {
        getOSUIEffectHelper().onAttachedToWindow();
        super.onAttachedToWindow();
    }

    @Override
    protected void onDetachedFromWindow() {
        getOSUIEffectHelper().onDetachedFromWindow();
        super.onDetachedFromWindow();
    }

    @Override
    public void setPressed(boolean pressed) {
        getOSUIEffectHelper().setPressed(pressed);
        super.setPressed(pressed);
    }

    @Override
    public void onHoverChanged(boolean hovered) {
        getOSUIEffectHelper().onHoverChanged(hovered);
        super.onHoverChanged(hovered);
    }

    public void setVibrateEnabled(boolean enabled) {
        getOSUIEffectHelper().setVibrateEnabled(enabled);
    }

    public void setSoundEffectEnabled(boolean enabled) {
        getOSUIEffectHelper().setSoundEffectEnabled(enabled);
    }

    private EffectHelper mEffectHelper;

    private EffectHelper getOSUIEffectHelper() {
        if (mEffectHelper == null) {
            mEffectHelper = new EffectHelper(this);
        }
        return mEffectHelper;
    }
}
