package com.example.anmol_5732.zoomit.view

import android.content.Context
import android.graphics.*
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import android.widget.RelativeLayout


/**
 * Created by anmol-5732 on 20/02/18.
 */

class DemoView(context: Context) : RelativeLayout(context) {
    companion object {
        var scale = 1f
    }

    private var mGestureDetector: GestureDetector
    private val MIN_ZOOM = 1f
    private val MAX_ZOOM = 5f
    private var mScaledetector: ScaleGestureDetector
    private var focusX: Float = 0.0f;
    private var focusY: Float = 0.0f;

    private var paint: Paint

    init {
        this.setDrawingCacheEnabled(true);
        paint = Paint()
        paint.color = Color.argb(255, 100, 200, 0)
        paint.style = Paint.Style.FILL_AND_STROKE
        paint.isAntiAlias = true
        paint.isDither = true
        setBackgroundColor(Color.CYAN)
        layoutParams = RelativeLayout.LayoutParams(2000, 2000)
        mGestureDetector = GestureDetector(context, GestureListner())
        mScaledetector = ScaleGestureDetector(context, ScaleListner())
    }

    override fun dispatchDraw(canvas: Canvas) {
        canvas.scale(scale, scale, focusX, focusY)
        canvas.drawBitmap(get(), 500f, 500f, paint)
        super.dispatchDraw(canvas)
    }

    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        ev.transform(getScaleMatrix())
        return super.dispatchTouchEvent(ev)
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        mGestureDetector.onTouchEvent(event)
        mScaledetector.onTouchEvent(event)
        return true
    }

    private fun getScaleMatrix(): Matrix {
        val matrix = Matrix()
        val invertMatrix = Matrix()
        matrix.postScale(scale, scale, focusX, focusY)
        matrix.invert(invertMatrix)
        return invertMatrix
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
            invalidate()
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
