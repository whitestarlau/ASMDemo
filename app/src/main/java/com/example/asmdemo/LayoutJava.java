package com.example.asmdemo;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.MyAnnotation;

@MyAnnotation
public class LayoutJava extends ConstraintLayout {
    private static String TAG = "EffectButtonJava";

    public LayoutJava(@NonNull Context context) {
        super(context);
    }

    public LayoutJava(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public LayoutJava(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean dispatchGenericMotionEvent(MotionEvent event) {
        return super.dispatchGenericMotionEvent(event);
    }
}
