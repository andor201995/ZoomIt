package com.example.anmol_5732.zoomit.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.view.MotionEvent
import android.widget.FrameLayout

/**
 * Created by anmol-5732 on 23/02/18.
 */
class ScaleContainer(context: Context) : FrameLayout(context) {
    private var paint: Paint
    private var rectFrame = Rect(0, 0, 0, 0)
    private var color = Color.YELLOW

    init {
        paint = Paint()
        paint.color = Color.BLACK
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = 10f
        paint.isAntiAlias = true
        paint.isDither = true
        layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
        clipChildren = false
        clipToPadding = false
        post({
            rectFrame.left = 0
            rectFrame.top = 0
            rectFrame.right = width
            rectFrame.bottom = height
            invalidate()

        })
    }

    //    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
//        ev.transform(getScaleMatrix())
//        return super.dispatchTouchEvent(ev)
//    }
//
    override fun dispatchDraw(canvas: Canvas) {
//        canvas.scale(ZoomView.scaleFactor, ZoomView.scaleFactor)
//        canvas.translate(ZoomView.translateX, ZoomView.translateY)
        canvas.drawRect(rectFrame, paint)
        super.dispatchDraw(canvas)
    }
//
//    private fun getScaleMatrix(): Matrix {
//        val matrix = Matrix()
//        val invertMatrix = Matrix()
//        matrix.postScale(ZoomView.scaleFactor, ZoomView.scaleFactor)
//        matrix.setTranslate(ZoomView.translateX, ZoomView.translateY)
//        matrix.invert(matrix)
//        return matrix
//    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (color != Color.YELLOW) {
            color = Color.YELLOW
            setBackgroundColor(Color.YELLOW)
        } else {
            color = Color.BLACK
            setBackgroundColor(Color.BLACK)
        }

        return super.onTouchEvent(event)
    }
}