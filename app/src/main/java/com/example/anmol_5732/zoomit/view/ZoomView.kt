package com.example.anmol_5732.zoomit.view


import android.content.Context
import android.graphics.Color
import android.graphics.PointF
import android.graphics.RectF
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import android.widget.FrameLayout


/**
 * Created by anmol-5732 on 20/02/18.
 */

class ZoomView(context: Context) : FrameLayout(context),ZoomViewListener {
    override fun getCurrentScaleFactor(): Float {
        return scaleFactor
    }

    private var scaleFactor = 1f
    private var lastScaleFactor: Float = 0f

    private val MIN_ZOOM = 0.5f
    private val MAX_ZOOM = 5f

    companion object {
        var translateX: Float = 0.0f
        var translateY: Float = 0.0f
    }

    private var initX: Float = 0.0f
    private var initY: Float = 0.0f
    private var prevDx = 0f
    private var prevDy = 0f

    private var childHeight: Int = 0
    private var childWidth: Int = 0
    private val childLeftTop = PointF()

    private var mScaledetector: ScaleGestureDetector

    private var mGestureDetector: GestureDetector

    private var isSingleTapConfirmed = false

    init {
        setBackgroundColor(Color.CYAN)
        layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
        mGestureDetector = GestureDetector(context, GestureListner())
        mScaledetector = ScaleGestureDetector(context, ScaleListner())
        clipChildren = false
        clipToPadding = false
        post {
            childWidth = child().width
            childHeight = child().height
            childLeftTop.x = 0f
            childLeftTop.y = 0f
        }
    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        mGestureDetector.onTouchEvent(ev)
        mScaledetector.onTouchEvent(ev)
        applyScaleAndTranslationToChild()
        if (ev!!.action == MotionEvent.ACTION_UP) {
            setLayoutParamOfChild()
        }
        return super.dispatchTouchEvent(ev)

    }

    //TODO: this method is to setLayoutParam dynamically based on the area that is visible onScreen
    /* private fun setLayoutParamOfChild() {
         val scaleContainer = child()
         if (translateX.sign > 0) {
             scaleContainer.layoutParams.width = ((childWidth / scaleFactor)).toInt()
             scaleContainer.translationX -= translateX
             for (i in 0..scaleContainer.childCount - 1) {
                 scaleContainer.getChildAt(i).translationX += translateX / scaleFactor
             }
             childLeftTop.offset(translateX / scaleFactor, 0f)
             translateX = scaleContainer.translationX
         } else {
             if (childLeftTop.x > 0) {
                 if (childLeftTop.x - Math.abs(translateX) >= 0) {
                     scaleContainer.translationX += Math.abs(translateX)
                     for (i in 0..scaleContainer.childCount - 1) {
                         scaleContainer.getChildAt(i).translationX -= Math.abs(translateX / scaleFactor)
                     }
                     childLeftTop.offset(translateX / scaleFactor, 0f)
                     translateX = scaleContainer.translationX
                 } else {
                     scaleContainer.translationX += Math.abs(childLeftTop.x)
                     for (i in 0..scaleContainer.childCount - 1) {
                         scaleContainer.getChildAt(i).translationX -= Math.abs(childLeftTop.x / scaleFactor)
                     }
                     childLeftTop.offset(-childLeftTop.x / scaleFactor, 0f)
                     translateX = scaleContainer.translationX
                     scaleContainer.layoutParams.width = ((childWidth + Math.abs(translateX)) / scaleFactor).toInt()
                 }
             } else {
                 scaleContainer.layoutParams.width = ((childWidth + Math.abs(translateX)) / scaleFactor).toInt()
             }
         }

         if (translateY.sign > 0) {
             scaleContainer.layoutParams.height = ((childHeight / scaleFactor)).toInt()
             scaleContainer.translationY -= translateY
             for (i in 0..scaleContainer.childCount - 1) {
                 scaleContainer.getChildAt(i).translationY += translateY / scaleFactor
             }
             childLeftTop.offset(0f, translateY / scaleFactor)
             translateY = scaleContainer.translationY
         } else {
             if (childLeftTop.y > 0) {
                 if (childLeftTop.y - Math.abs(translateY) >= 0) {
                     scaleContainer.translationY += Math.abs(translateY)
                     for (i in 0..scaleContainer.childCount - 1) {
                         scaleContainer.getChildAt(i).translationY -= Math.abs(translateY / scaleFactor)
                     }
                     childLeftTop.offset(0f, translateY / scaleFactor)
                     translateY = scaleContainer.translationY
                 } else {
                     scaleContainer.translationY += Math.abs(childLeftTop.y)
                     for (i in 0..scaleContainer.childCount - 1) {
                         scaleContainer.getChildAt(i).translationY -= Math.abs(childLeftTop.y / scaleFactor)
                     }
                     childLeftTop.offset(0f, -childLeftTop.y / scaleFactor)
                     translateY = scaleContainer.translationY
                     scaleContainer.layoutParams.height = ((childHeight + Math.abs(translateY)) / scaleFactor).toInt()
                 }
             } else {
                 scaleContainer.layoutParams.height = ((childHeight + Math.abs(translateY)) / scaleFactor).toInt()
             }
         }
         scaleContainer.rectFrame.offsetTo(childLeftTop.x, childLeftTop.y)
         scaleContainer.requestLayout()
     }
 */

    private fun setLayoutParamOfChild() {
        val container = child()
        val rectF = RectF(childLeftTop.x, childLeftTop.y, childWidth.toFloat(), childHeight.toFloat())

        for (containerChildIndex in 0 until container.childCount) {
            val baseShapeView = container.getChildAt(containerChildIndex)
            if (baseShapeView.translationX < rectF.left) {
                rectF.left = baseShapeView.translationX
            }
            if (baseShapeView.translationY < rectF.top) {
                rectF.top = baseShapeView.translationY
            }
            val shapeRight = baseShapeView.translationX + baseShapeView.width
            val shapeBottom = baseShapeView.translationY + baseShapeView.height
            if (shapeRight > rectF.right) {
                rectF.right = shapeRight
            }
            if (shapeBottom > rectF.bottom) {
                rectF.bottom = shapeBottom
            }
        }
        rectF.union(RectF(childLeftTop.x, childLeftTop.y, childLeftTop.x + childWidth, childLeftTop.y + childHeight))
        container.layoutParams.width = rectF.width().toInt()
        container.layoutParams.height = rectF.height().toInt()
        if (rectF.left < 0) {
            container.translationX -= Math.abs(rectF.left * scaleFactor)
            childLeftTop.offset(Math.abs(rectF.left), 0f)
            for (containerChildIndex in 0 until container.childCount) {
                container.getChildAt(containerChildIndex).translationX += Math.abs(rectF.left)
            }
        } else {
            container.translationX += Math.abs(rectF.left * scaleFactor)
            childLeftTop.offset(-Math.abs(rectF.left), 0f)
            for (containerChildIndex in 0 until container.childCount) {
                container.getChildAt(containerChildIndex).translationX -= Math.abs(rectF.left)
            }
        }
        if (rectF.top < 0) {
            container.translationY -= Math.abs(rectF.top * scaleFactor)
            childLeftTop.offset(0f, Math.abs(rectF.top))
            for (containerChildIndex in 0 until container.childCount) {
                container.getChildAt(containerChildIndex).translationY += Math.abs(rectF.top)
            }
        } else {
            container.translationY += Math.abs(rectF.top * scaleFactor)
            childLeftTop.offset(0f, -Math.abs(rectF.top))
            for (containerChildIndex in 0 until container.childCount) {
                container.getChildAt(containerChildIndex).translationY -= Math.abs(rectF.top)
            }
        }
        translateX = container.translationX
        translateY = container.translationY
        child().rectFrame.offsetTo(childLeftTop.x, childLeftTop.y)
        container.requestLayout()
    }

    override fun onTouchEvent(motionEvent: MotionEvent?): Boolean {
//        var maxDx = child().width * (scaleFactor - 1)  // adjusted for zero pivot
//        var maxDy = child().height * (scaleFactor - 1)// adjusted for zero pivot
//        var minDx = 0f
//        var minDy = 0f
//        translateX = Math.min(Math.max(translateX, -maxDx), minDx)  // adjusted for zero pivot
//        translateY = Math.min(Math.max(translateY, -maxDy), minDy) // adjusted for zero pivot
        return true
    }

    private fun applyScaleAndTranslationToChild() {
        child().scaleX = scaleFactor
        child().scaleY = scaleFactor
        child().pivotX = 0f  // default is to pivot at view center
        child().pivotY = 0f  // default is to pivot at view center
        child().translationX = translateX
        child().translationY = translateY
    }

    private fun child(): ScaleContainer {
        return getChildAt(0) as ScaleContainer
    }

    inner class ScaleListner : ScaleGestureDetector.SimpleOnScaleGestureListener() {

        override fun onScale(scaleDetector: ScaleGestureDetector?): Boolean {
            val scaleFactor = scaleDetector!!.scaleFactor
            setScaleAndTranslation(scaleFactor, scaleDetector.focusX, scaleDetector.focusY)
            return true
        }

        override fun onScaleEnd(detector: ScaleGestureDetector?) {
            super.onScaleEnd(detector)
            setLayoutParamOfChild()
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
            isSingleTapConfirmed = false
        }
    }

    inner class GestureListner : GestureDetector.SimpleOnGestureListener() {

        override fun onScroll(e1: MotionEvent?, e2: MotionEvent?, distanceX: Float, distanceY: Float): Boolean {
            translateX += -distanceX
            translateY += -distanceY
            isSingleTapConfirmed = false
            return super.onScroll(e1, e2, distanceX, distanceY)
        }

        override fun onSingleTapConfirmed(e: MotionEvent?): Boolean {
            isSingleTapConfirmed = true
            return super.onSingleTapConfirmed(e)
        }

        override fun onDoubleTap(e: MotionEvent?): Boolean {
            scaleFactor = 1f
            val scaleContainer = child()
            scaleContainer.layoutParams.width = childWidth
            scaleContainer.layoutParams.height = childHeight
            translateX = -childLeftTop.x
            translateY = -childLeftTop.y
            lastScaleFactor = 0f
            initX = 0f
            initY = 0f
            prevDx = 0f
            prevDy = 0f
            isSingleTapConfirmed = false
            return super.onDoubleTap(e)
        }

        override fun onFling(e1: MotionEvent?, e2: MotionEvent?, velocityX: Float, velocityY: Float): Boolean {
            isSingleTapConfirmed = false
            return super.onFling(e1, e2, velocityX, velocityY)
        }
    }
}
