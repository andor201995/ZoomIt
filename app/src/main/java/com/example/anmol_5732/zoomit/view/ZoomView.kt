package com.example.anmol_5732.zoomit.view


import android.content.ContentValues.TAG
import android.content.Context
import android.graphics.Color
import android.graphics.Matrix
import android.util.Log
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import android.view.View
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

    private var scaleFactor = 1f
    private var lastScaleFactor: Float = 0f
    private var focusX: Float = 0.0f
    private var focusY: Float = 0.0f

    private val MIN_ZOOM = 1f
    private val MAX_ZOOM = 5f

    private var translateX: Float = 0.0f
    private var translateY: Float = 0.0f

    private var mode = Mode.NONE

    private var initX: Float = 0.0f
    private var initY: Float = 0.0f
    private var prevDx = 0f
    private var prevDy = 0f

    private var mScaledetector: ScaleGestureDetector

    private var mGestureDetector: GestureDetector

    init {
        setBackgroundColor(Color.CYAN)
        layoutParams = RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT)
        mGestureDetector = GestureDetector(context, GestureListner())
        mScaledetector = ScaleGestureDetector(context, ScaleListner())
    }

    override fun onTouchEvent(motionEvent: MotionEvent?): Boolean {
        when (motionEvent!!.getAction() and MotionEvent.ACTION_MASK) {
            MotionEvent.ACTION_DOWN -> {
                Log.i(TAG, "DOWN")
                if (scaleFactor > MIN_ZOOM) {
                    mode = Mode.DRAG
                    initX = motionEvent.getX() - prevDx
                    initY = motionEvent.getY() - prevDy
                }
            }
            MotionEvent.ACTION_MOVE -> if (mode == Mode.DRAG) {
                translateX = motionEvent.getX() - initX
                translateY = motionEvent.getY() - initY
            }
            MotionEvent.ACTION_POINTER_DOWN -> mode = Mode.ZOOM
            MotionEvent.ACTION_POINTER_UP -> mode = Mode.NONE // changed from DRAG, was messing up zoom
            MotionEvent.ACTION_UP -> {
                Log.i(TAG, "UP")
                mode = Mode.NONE
                prevDx = translateX
                prevDy = translateY
            }
        }

        mGestureDetector.onTouchEvent(motionEvent)
        mScaledetector.onTouchEvent(motionEvent)

        if (mode == Mode.DRAG && scaleFactor >= MIN_ZOOM || mode == Mode.ZOOM) {
            parent.requestDisallowInterceptTouchEvent(true)
            val maxDx = child().width * (scaleFactor - 1)  // adjusted for zero pivot
            val maxDy = child().height * (scaleFactor - 1)  // adjusted for zero pivot
            translateX = Math.min(Math.max(translateX, -maxDx), 0f)  // adjusted for zero pivot
            translateY = Math.min(Math.max(translateY, -maxDy), 0f)  // adjusted for zero pivot
            Log.i(TAG, "Width: " + child().width + ", scaleFactor " + scaleFactor + ", translateX " + translateX
                    + ", max " + maxDx)
            applyScaleAndTranslation()
        }

        return true
    }


//    fun get(): Bitmap {
//        return this.drawingCache
//    }

    private fun applyScaleAndTranslation() {
        child().setScaleX(scaleFactor)
        child().setScaleY(scaleFactor)
        child().setPivotX(0f)  // default is to pivot at view center
        child().setPivotY(0f)  // default is to pivot at view center
        child().setTranslationX(translateX)
        child().setTranslationY(translateY)
    }

    private fun child(): View {
        return getChildAt(0)
    }

    inner class ScaleListner : ScaleGestureDetector.SimpleOnScaleGestureListener() {
        override fun onScaleBegin(detector: ScaleGestureDetector?): Boolean {
            val scaleMatrix = Matrix()
            val invertScaleMatrix = Matrix()
            scaleMatrix.setScale(scaleFactor, scaleFactor, focusX, focusY)
            scaleMatrix.invert(invertScaleMatrix)
            val focusPoints: FloatArray = floatArrayOf(detector!!.focusX, detector.focusY)
            invertScaleMatrix.mapPoints(focusPoints)
            focusX = focusPoints[0]
            focusY = focusPoints[1]
            return true
        }

        override fun onScale(scaleDetector: ScaleGestureDetector?): Boolean {
            val scaleFactor = scaleDetector!!.getScaleFactor()
            Log.i(TAG, "onScale(), scaleFactor = " + scaleFactor)
            if (lastScaleFactor == 0f || Math.signum(scaleFactor) == Math.signum(lastScaleFactor)) {
                val prevScale = this@ZoomView.scaleFactor
                this@ZoomView.scaleFactor *= scaleFactor
                this@ZoomView.scaleFactor = Math.max(MIN_ZOOM, Math.min(this@ZoomView.scaleFactor, MAX_ZOOM))
                lastScaleFactor = scaleFactor
                val adjustedScaleFactor = this@ZoomView.scaleFactor / prevScale
                // added logic to adjust translateX and translateY for pinch/zoom pivot point
                Log.d(TAG, "onScale, adjustedScaleFactor = " + adjustedScaleFactor)
                Log.d(TAG, "onScale, BEFORE translateX/translateY = $translateX/$translateY")
                val focusX = scaleDetector.getFocusX()
                val focusY = scaleDetector.getFocusY()
                Log.d(TAG, "onScale, focusX/focusy = $focusX/$focusY")
                translateX += (translateX - focusX) * (adjustedScaleFactor - 1)
                translateY += (translateY - focusY) * (adjustedScaleFactor - 1)
                Log.d(TAG, "onScale, translateX/translateY = $translateX/$translateY")
            } else {
                lastScaleFactor = 0f
            }

            return true

//            scaleFactor *= detector!!.scaleFactor
//            scaleFactor = Math.max(MIN_ZOOM, Math.min(scaleFactor, MAX_ZOOM))
//            getChildAt(0).invalidate()
//            return true

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
