package com.example.anmol_5732.zoomit.view

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import android.widget.RelativeLayout


/**
 * Created by anmol-5732 on 20/02/18.
 */

class ZoomView(context: Context) : RelativeLayout(context) {


    companion object {
        var scale = 1f
        var focusX: Float = 0.0f;
        var focusY: Float = 0.0f;
    }

    private var mGestureDetector: GestureDetector
    private val MIN_ZOOM = 1f
    private val MAX_ZOOM = 5f
    private var mScaledetector: ScaleGestureDetector

    init {
        isDrawingCacheEnabled = true;
        setBackgroundColor(Color.CYAN)
        layoutParams = RelativeLayout.LayoutParams(2000, 2000)
        mGestureDetector = GestureDetector(context, GestureListner())
        mScaledetector = ScaleGestureDetector(context, ScaleListner())
    }

    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        return super.dispatchTouchEvent(ev)
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        mGestureDetector.onTouchEvent(event)
        mScaledetector.onTouchEvent(event)
        getChildAt(0).invalidate()
        return true
    }


    fun get(): Bitmap {
        return this.drawingCache
    }


    inner class ScaleListner : ScaleGestureDetector.SimpleOnScaleGestureListener() {
        override fun onScaleBegin(detector: ScaleGestureDetector?): Boolean {
            focusX = detector!!.focusX
            focusY = detector.focusY
            return true
        }

        override fun onScale(detector: ScaleGestureDetector?): Boolean {
            scale *= detector!!.scaleFactor
            scale = Math.max(MIN_ZOOM, Math.min(scale, MAX_ZOOM))
            return true

        }

        override fun onScaleEnd(detector: ScaleGestureDetector?) {
        }
    }

    inner class GestureListner : GestureDetector.SimpleOnGestureListener() {
        override fun onScroll(e1: MotionEvent?, e2: MotionEvent?, distanceX: Float, distanceY: Float): Boolean {
            return super.onScroll(e1, e2, distanceX, distanceY)
        }
    }
}
