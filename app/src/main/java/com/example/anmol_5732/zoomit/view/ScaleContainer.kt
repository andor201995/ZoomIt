package com.example.anmol_5732.zoomit.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.widget.FrameLayout

/**
 * Created by anmol-5732 on 23/02/18.
 */
class ScaleContainer(context: Context) : FrameLayout(context) {
    private var paint: Paint

    init {
        paint = Paint()
        paint.color = Color.BLACK
        paint.style = Paint.Style.FILL
        paint.isAntiAlias = true
        paint.isDither = true
        layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
        clipChildren = false
        clipToPadding = false
    }

    //    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
//        ev.transform(getScaleMatrix())
//        return super.dispatchTouchEvent(ev)
//    }
//
    override fun dispatchDraw(canvas: Canvas) {
//        canvas.scale(ZoomView.scaleFactor, ZoomView.scaleFactor)
//        canvas.translate(ZoomView.translateX, ZoomView.translateY)
        canvas.drawCircle(0f, 0f, 100f, paint)
        canvas.drawCircle(0f, height.toFloat(), 100f, paint)
        canvas.drawCircle(width.toFloat(), 0f, 100f, paint)
        canvas.drawCircle(width.toFloat(), height.toFloat(), 100f, paint)
        paint.textSize = 50f
        canvas.drawText("hello their", 0, 11, 100f, 100f, paint)
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
}