package com.example.anmol_5732.zoomit.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.view.MotionEvent
import android.view.ViewGroup
import android.widget.FrameLayout


/**
 * Created by anmol-5732 on 05/01/18.
 */
class SqaureView(context: Context) : FrameLayout(context) {
    companion object {
        private val INVALID_POINTER_ID: Int = -1
    }

    private var activePointerID: Int = INVALID_POINTER_ID
    private var paint: Paint
    private var path: Path = Path()
    private var initX: Float = 0.0f
    private var initY: Float = 0.0f


    init {
        setDrawingCacheEnabled(true);
        paint = Paint()
        paint.color = Color.argb(255, 100, 200, 0)
        paint.style = Paint.Style.FILL_AND_STROKE
        paint.isAntiAlias = true
        paint.isDither = true
        path.addRect(0f, 0f, 200f, 200f, Path.Direction.CW)
        setLayerType(LAYER_TYPE_SOFTWARE, paint)
        setBackgroundColor(Color.YELLOW)
        layoutParams = ViewGroup.LayoutParams(200, 200)
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas!!.drawPath(path, paint)
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        when ((event!!.action and MotionEvent.ACTION_MASK)) {
            MotionEvent.ACTION_DOWN -> {
                paint.color = Color.WHITE
                initX = event.getX()
                initY = event.getY()
                activePointerID = event.getPointerId(0)
            }
            MotionEvent.ACTION_MOVE -> {
                val pointerIndex: Int = event.findPointerIndex(activePointerID)
                translationX = translationX + (event.getX(pointerIndex) - initX)
                translationY = translationY + (event.getY(pointerIndex) - initY)
            }
            MotionEvent.ACTION_POINTER_UP -> {
                val pointerIndex: Int = (event.action and MotionEvent.ACTION_POINTER_INDEX_MASK) shr MotionEvent.ACTION_POINTER_INDEX_SHIFT
                val pointerId: Int = event.getPointerId(pointerIndex)
                if (pointerId == activePointerID) {
                    val newPointerIndex = if (pointerIndex == 0) 1 else 0
                    initX = event.getX(newPointerIndex)
                    initY = event.getY(newPointerIndex)
                    activePointerID = event.getPointerId(newPointerIndex)
                }
            }
            MotionEvent.ACTION_UP -> {
                paint.color = Color.GREEN
                activePointerID = INVALID_POINTER_ID
            }
            MotionEvent.ACTION_CANCEL -> activePointerID = INVALID_POINTER_ID

        }
        invalidate()
        return true
    }
}