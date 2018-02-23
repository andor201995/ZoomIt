package com.example.anmol_5732.zoomit.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Matrix
import android.view.MotionEvent
import android.widget.FrameLayout

/**
 * Created by anmol-5732 on 23/02/18.
 */
class ScaleContainer(context: Context) : FrameLayout(context) {
    init {
        setDrawingCacheEnabled(true);
        layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
    }

    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        ev.transform(getScaleMatrix())
        return super.dispatchTouchEvent(ev)
    }

    override fun dispatchDraw(canvas: Canvas) {
        canvas.scale(ZoomView.scale, ZoomView.scale, ZoomView.focusX, ZoomView.focusY)
        super.dispatchDraw(canvas)
    }

    private fun getScaleMatrix(): Matrix {
        val matrix = Matrix()
        val invertMatrix = Matrix()
        matrix.postScale(ZoomView.scale, ZoomView.scale, ZoomView.focusX, ZoomView.focusY)
        matrix.invert(invertMatrix)
        return invertMatrix
    }
}