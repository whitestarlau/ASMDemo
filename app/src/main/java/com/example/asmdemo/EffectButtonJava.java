package com.example.asmdemo;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.MyAnnotation;

@MyAnnotation
public class EffectButtonJava extends androidx.appcompat.widget.AppCompatButton {
    private static String TAG = "EffectButtonJava";

    public EffectButtonJava(@NonNull Context context) {
        super(context);
    }

    public EffectButtonJava(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public EffectButtonJava(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean dispatchGenericMotionEvent(MotionEvent event) {
        return super.dispatchGenericMotionEvent(event);
    }

//    @Override
//    public boolean dispatchTouchEvent(MotionEvent event) {
//        Log.d(TAG,"dispatchTouchEvent from origin code.");
//        return super.dispatchTouchEvent(event);
//    }
//
//    @Override
//    protected void onAttachedToWindow() {
//        Log.d(TAG,"onAttachedToWindow from origin code.");
//        super.onAttachedToWindow();
//    }
//
//    @Override
//    public void setPressed(boolean pressed) {
//        Log.d(TAG,"setPressed from origin code:"+pressed);
//        super.setPressed(pressed);
//    }

//    @Override
//    public void onHoverChanged(boolean hovered) {
//        super.onHoverChanged(hovered);
//    }
//
//    @Override
//    protected void onDetachedFromWindow() {
//        super.onDetachedFromWindow();
//    }
}
