package com.example.anmol_5732.zoomit.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Matrix
import android.graphics.Paint
import android.view.MotionEvent
import android.widget.FrameLayout

/**
 * Created by anmol-5732 on 23/02/18.
 */
class ScaleContainer(context: Context) : FrameLayout(context) {
    private var paint: Paint

    init {
        setDrawingCacheEnabled(true)
        paint = Paint()
        paint.color = Color.BLACK
        paint.style = Paint.Style.FILL
        paint.isAntiAlias = true
        paint.isDither = true
        layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
    }

    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        ev.transform(getScaleMatrix())
        return super.dispatchTouchEvent(ev)
    }

    override fun dispatchDraw(canvas: Canvas) {
//        canvas.scale(ZoomView.scale, ZoomView.scale, ZoomView.focusX, ZoomView.focusY)
//        canvas.translate(ZoomView.dx, ZoomView.dy)
        canvas.drawCircle(200f, 200f, 100f, paint)
        super.dispatchDraw(canvas)
    }

    private fun getScaleMatrix(): Matrix {
        val matrix = Matrix()
//        val invertMatrix = Matrix()
//        matrix.postScale(ZoomView.scale, ZoomView.scale, ZoomView.focusX, ZoomView.focusY)
//        matrix.setTranslate(ZoomView.dx, ZoomView.dy)
        matrix.invert(matrix)
        return matrix
    }
}