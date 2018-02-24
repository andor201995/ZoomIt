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

    private enum class Mode {
        NONE,
        DRAG,
        ZOOM
    }

    companion object {
        var scale = 1f
        var focusX: Float = 0.0f;
        var focusY: Float = 0.0f;
        var translateX: Float = 0.0f
        var translateY: Float = 0.0f
    }

    private val MIN_ZOOM = 1f
    private val MAX_ZOOM = 5f
    private var initX: Float = 0.0f
    private var initY: Float = 0.0f
    private var previousTranslateX: Float = 0f;


    private var previousTranslateY: Float = 0f;

    private lateinit var mode: Mode

    private var mScaledetector: ScaleGestureDetector

    private var mGestureDetector: GestureDetector

    init {
        setBackgroundColor(Color.CYAN)
        layoutParams = RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT)
        mGestureDetector = GestureDetector(context, GestureListner())
        mScaledetector = ScaleGestureDetector(context, ScaleListner())
    }

    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        return super.dispatchTouchEvent(ev)
    }


    override fun onTouchEvent(event: MotionEvent?): Boolean {
        when (event!!.actionMasked) {
            MotionEvent.ACTION_DOWN -> {
                mode = Mode.DRAG
                initX = event.getX() - previousTranslateX;
                initY = event.getY() - previousTranslateY;
            }
            MotionEvent.ACTION_MOVE -> {
                translateX = event.getX() - initX;
                translateY = event.getY() - initY;

            }
            MotionEvent.ACTION_POINTER_DOWN -> mode = Mode.ZOOM
            MotionEvent.ACTION_POINTER_UP -> {
                previousTranslateX = translateX;
                previousTranslateY = translateY;
                mode = Mode.DRAG
            }
            MotionEvent.ACTION_UP -> {
                previousTranslateX = translateX;
                previousTranslateY = translateY;
                mode = Mode.NONE
            }

        }
        mGestureDetector.onTouchEvent(event)
        mScaledetector.onTouchEvent(event)

        if ((mode == Mode.DRAG && scale != MIN_ZOOM) || mode == Mode.ZOOM) {
            getChildAt(0).invalidate();
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
    }
}
