package com.example.anmol_5732.zoomit.view


import android.content.ContentValues.TAG
import android.content.Context
import android.graphics.Color
import android.graphics.Rect
import android.util.Log
import android.view.*
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

    private val MIN_ZOOM = 0.5f
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
        clipChildren = false
        clipToPadding = false
    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        mGestureDetector.onTouchEvent(ev)
        mScaledetector.onTouchEvent(ev)
        if (mScaledetector.isInProgress) {
            applyScaleAndTranslation()
            return false
        }
        return super.dispatchTouchEvent(ev)
    }

    override fun onTouchEvent(motionEvent: MotionEvent?): Boolean {
        when (motionEvent!!.getAction() and MotionEvent.ACTION_MASK) {
            MotionEvent.ACTION_DOWN -> {
                Log.i(TAG, "DOWN")
                mode = Mode.DRAG
                initX = motionEvent.getX() - prevDx
                initY = motionEvent.getY() - prevDy
            }
            MotionEvent.ACTION_MOVE -> if (mode == Mode.DRAG) {
                translateX = motionEvent.getX() - initX
                translateY = motionEvent.getY() - initY
            }
            MotionEvent.ACTION_POINTER_DOWN -> mode = Mode.ZOOM
            MotionEvent.ACTION_POINTER_UP -> mode = Mode.NONE // changed from DRAG, was messing up zoom
            MotionEvent.ACTION_UP -> {
                mode = Mode.NONE
                prevDx = translateX
                prevDy = translateY
            }
        }

        if (mode == Mode.DRAG && scaleFactor >= MIN_ZOOM || mode == Mode.ZOOM) {
            parent.requestDisallowInterceptTouchEvent(true)

//            if (scaleFactor < 1 || mode == Mode.DRAG) {
//                var maxDx = child().width * (scaleFactor - 1)  // adjusted for zero pivot
//                var maxDy = child().height * (scaleFactor - 1) // adjusted for zero pivot
//                var minDx = 0f
//                var minDy = 0f
//
//                if (scaleFactor < 1) {
//                    minDx += Math.abs(maxDx / 2)
//                    minDy += Math.abs(maxDy / 2)
//                    maxDx += Math.abs(maxDx / 2)
//                    maxDy += Math.abs(maxDy / 2)
//                }
//                translateX = Math.min(Math.max(translateX, -maxDx), minDx)  // adjusted for zero pivot
//                translateY = Math.min(Math.max(translateY, -maxDy), minDy) // adjusted for zero pivot
//            }
            applyScaleAndTranslation()
        }

        return true
    }

    private fun applyScaleAndTranslation() {
        child().setScaleX(scaleFactor)
        child().setScaleY(scaleFactor)
        child().setPivotX(0f)  // default is to pivot at view center
        child().setPivotY(0f)  // default is to pivot at view center
        child().setTranslationX(translateX)
        child().setTranslationY(translateY)
        expandTouchArea(this, child(), 1000)
//        child().invalidate()
    }

    fun expandTouchArea(bigView: View, smallView: View, extraPadding: Int) {
        bigView.post(Runnable {
            val rect = Rect()
            smallView.getHitRect(rect)
            rect.top -= extraPadding
            rect.left -= extraPadding
            rect.right += extraPadding
            rect.bottom += extraPadding
            bigView.setTouchDelegate(TouchDelegate(rect, smallView))
        })
    }

    private fun child(): ViewGroup {
        return getChildAt(0) as ViewGroup
    }

    private fun setScaleAndTranslation(scaleFactor: Float, focusX: Float, focusY: Float) {
        if (lastScaleFactor == 0f || Math.signum(scaleFactor) == Math.signum(lastScaleFactor)) {
            val prevScale = this@ZoomView.scaleFactor
            this@ZoomView.scaleFactor *= scaleFactor
            this@ZoomView.scaleFactor = Math.max(MIN_ZOOM, Math.min(this@ZoomView.scaleFactor, MAX_ZOOM))
            lastScaleFactor = scaleFactor
            val adjustedScaleFactor = this@ZoomView.scaleFactor / prevScale
            // added logic to adjust translateX and translateY for pinch/zoom pivot point
            translateX += (translateX - focusX) * (adjustedScaleFactor - 1)
            translateY += (translateY - focusY) * (adjustedScaleFactor - 1)
        } else {
            lastScaleFactor = 0f
        }
    }

    inner class ScaleListner : ScaleGestureDetector.SimpleOnScaleGestureListener() {

        override fun onScale(scaleDetector: ScaleGestureDetector?): Boolean {
            val scaleFactor = scaleDetector!!.getScaleFactor()
            setScaleAndTranslation(scaleFactor, scaleDetector.focusX, scaleDetector.focusY)
            return true
        }

    }

    inner class GestureListner : GestureDetector.SimpleOnGestureListener() {
        override fun onScroll(e1: MotionEvent?, e2: MotionEvent?, distanceX: Float, distanceY: Float): Boolean {
            return super.onScroll(e1, e2, distanceX, distanceY)
        }

        override fun onDoubleTap(e: MotionEvent?): Boolean {
            scaleFactor = 1f
            translateX = 0f
            translateY = 0f
            lastScaleFactor = 0f
            initX = 0f
            initY = 0f
            prevDx = 0f
            prevDy = 0f
            applyScaleAndTranslation()
            return super.onDoubleTap(e)
        }

        override fun onFling(e1: MotionEvent?, e2: MotionEvent?, velocityX: Float, velocityY: Float): Boolean {
            return super.onFling(e1, e2, velocityX, velocityY)
        }
    }
}
