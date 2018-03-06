package com.example.anmol_5732.zoomit.view;

import android.content.Context;
import android.util.Log;
import android.view.ScaleGestureDetector;

/**
 * Created by nor on 11/23/2015.
 */
public class ZoomTextView extends android.support.v7.widget.AppCompatEditText {
    private static final String TAG = "ZoomTextView";
    private ScaleGestureDetector mScaleDetector;

    private float mScaleFactor = 1.f;
    private float defaultSize;

    private float zoomLimit = 3.0f;


    public ZoomTextView(Context context) {
        super(context);
        initialize();
    }

    private void initialize() {
        defaultSize = getTextSize();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    public void onscale(float scale) {
        mScaleFactor = scale;
        Log.e(TAG, String.valueOf(mScaleFactor));
    }

}
