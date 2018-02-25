package com.example.anmol_5732.zoomit.view


import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.Matrix
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
        var translateX: Float = 0.0f
        var translateY: Float = 0.0f
    }

    private val MIN_ZOOM = 1.0f
    private val MAX_ZOOM = 5f
    private var initX: Float = 0.0f
    private var initY: Float = 0.0f

    private var mScaledetector: ScaleGestureDetector

    private var mGestureDetector: GestureDetector

    init {
        setBackgroundColor(Color.CYAN)
        layoutParams = RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT)
        mGestureDetector = GestureDetector(context, GestureListner())
        mScaledetector = ScaleGestureDetector(context, ScaleListner())
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        mGestureDetector.onTouchEvent(event)
        mScaledetector.onTouchEvent(event)

        when (event!!.actionMasked) {
            MotionEvent.ACTION_DOWN -> {
                initX = event.getX()
                initY = event.getY()
            }
            MotionEvent.ACTION_MOVE -> {
                if (!mScaledetector.isInProgress) {
                    var distanceX = event.getX() - initX;
                    var distanceY = event.getY() - initY;
                    translateX += distanceX
                    translateY += distanceY
                }
                initX = event.getX()
                initY = event.getY()
                getChildAt(0).invalidate();
            }

        }
        return true
    }


    fun get(): Bitmap {
        return this.drawingCache
    }

    inner class ScaleListner : ScaleGestureDetector.SimpleOnScaleGestureListener() {
        override fun onScaleBegin(detector: ScaleGestureDetector?): Boolean {
            val scaleMatrix = Matrix()
            val invertScaleMatrix = Matrix()
            scaleMatrix.setScale(scale, scale, focusX, focusY)
            scaleMatrix.invert(invertScaleMatrix)
            val focusPoints: FloatArray = floatArrayOf(detector!!.focusX, detector.focusY)
            invertScaleMatrix.mapPoints(focusPoints)
            focusX = focusPoints.get(0)
            focusY = focusPoints.get(1)
            return true
        }

        override fun onScale(detector: ScaleGestureDetector?): Boolean {
            scale *= detector!!.scaleFactor
            scale = Math.max(MIN_ZOOM, Math.min(scale, MAX_ZOOM))
            getChildAt(0).invalidate();
            return true

        }

        override fun onScaleEnd(detector: ScaleGestureDetector?) {
            super.onScaleEnd(detector)
        }
    }

    inner class GestureListner : GestureDetector.SimpleOnGestureListener() {
        override fun onScroll(e1: MotionEvent?, e2: MotionEvent?, distanceX: Float, distanceY: Float): Boolean {
            return super.onScroll(e1, e2, distanceX, distanceY)
        }

        override fun onFling(e1: MotionEvent?, e2: MotionEvent?, velocityX: Float, velocityY: Float): Boolean {
            return super.onFling(e1, e2, velocityX, velocityY)
        }
    }
}
