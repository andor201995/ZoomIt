package com.example.anmol_5732.zoomit.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.view.MotionEvent
import android.widget.FrameLayout


/**
 * Created by anmol-5732 on 23/02/18.
 */
class ScaleContainer(context: Context) : FrameLayout(context) {
    private var paint: Paint
    var rectFrame = RectF(0f, 0f, 0f, 0f)
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
            rectFrame.left = 0f
            rectFrame.top = 0f
            rectFrame.right = width.toFloat()
            rectFrame.bottom = height.toFloat()
        })
    }

    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        return super.dispatchTouchEvent(ev)
    }

    override fun dispatchDraw(canvas: Canvas) {
        canvas.drawRect(rectFrame, paint)
        super.dispatchDraw(canvas)
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (color != Color.YELLOW) {
            color = Color.YELLOW
            setBackgroundColor(Color.YELLOW)
        } else {
            color = Color.BLACK
            setBackgroundColor(Color.BLUE)
        }
        return super.onTouchEvent(event)
    }
}