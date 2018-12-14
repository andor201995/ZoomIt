package com.example.anmol_5732.zoomit.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Matrix
import android.graphics.RectF
import android.graphics.drawable.Drawable
import android.text.Selection
import android.view.Gravity
import android.view.MotionEvent
import android.view.View
import android.view.ViewTreeObserver
import android.widget.PopupWindow
import com.example.anmol_5732.zoomit.R


class CursorEditor(private val shapeEditText: CustomEditText) {

    private val insertCursorController = InsertCursorController()

    fun getTextLayout() = shapeEditText.layout!!

    fun getContext() = shapeEditText.context!!

    fun getEditorScale() = shapeEditText.getEditorScale()

    private var runnable: Runnable = Runnable { insertCursorController.hide() }

    private var screenLocation: IntArray = IntArray(2)

    private val preDrawListener: ViewTreeObserver.OnPreDrawListener

    companion object {
        private const val ANCHOR_IDLE_TIME: Long = 5000
    }

    init {
        preDrawListener = ViewTreeObserver.OnPreDrawListener {
            shapeEditText.getLocationInWindow(screenLocation)
            insertCursorController.show()
            true
        }
    }

    internal fun getVerticalOffset(): Float {
        var vOffset = 0f
        val gravity = shapeEditText.gravity and Gravity.VERTICAL_GRAVITY_MASK
        if (gravity != Gravity.TOP) {
            val boxHeight = shapeEditText.measuredHeight - (shapeEditText.extendedPaddingTop + shapeEditText.extendedPaddingBottom)
            val layoutHeight = getTextLayout().height

            if (layoutHeight < boxHeight) {
                vOffset = if (gravity == Gravity.BOTTOM) {
                    (boxHeight - layoutHeight).toFloat()
                } else {
                    (boxHeight - layoutHeight shr 1).toFloat()
                }
            }
        }
        return vOffset
    }

    fun onDraw() {
        if (!shapeEditText.isFocusable) {
            insertCursorController.hide()
            return
        }

        if (insertCursorController.isShowing()) {
            insertCursorController.show()
        }

    }

    internal fun onSingeTap(event: MotionEvent?) {
        val eventX = event?.x ?: 0f
        val eventY = event?.y ?: 0f

        val position = shapeEditText.getOffsetForPosition(eventX, eventY)
        Selection.setSelection(shapeEditText.editableText, position)

        insertCursorController.show()

        shapeEditText.removeCallbacks(runnable)
        shapeEditText.postDelayed(runnable, ANCHOR_IDLE_TIME)
    }

    fun onAttachedToWindow() {
        shapeEditText.viewTreeObserver.addOnPreDrawListener(preDrawListener)
    }

    fun onDetachedFromWindow() {
        shapeEditText.viewTreeObserver.removeOnPreDrawListener(preDrawListener)
    }

    abstract class CursorController {
        abstract fun show()
        abstract fun hide()
        abstract fun isShowing(): Boolean
    }

    inner class InsertCursorController : CursorController() {
        private val middleHandleView = MiddleHandleView(getContext())

        override fun isShowing(): Boolean {
            return middleHandleView.isShowing()
        }

        override fun show() {
            middleHandleView.show()
        }

        override fun hide() {
            middleHandleView.hide()
        }
    }

    abstract inner class HandleView(context: Context, drawableID: Int) : View(context) {

        private val container: PopupWindow = PopupWindow(context)
        protected val drawable: Drawable = context.getDrawable(drawableID)!!
        private var popUpWindowRect: RectF

        init {
            setupContainer()
            popUpWindowRect = RectF(0f, 0f, drawable.intrinsicWidth.toFloat(), drawable.intrinsicHeight.toFloat())
        }

        private fun setupContainer() {
            container.isSplitTouchEnabled = true
            container.isClippingEnabled = false

            container.setBackgroundDrawable(null)
            container.width = drawable.intrinsicWidth
            container.height = drawable.intrinsicHeight
            container.contentView = this@HandleView
        }

        fun getHandleLeftTop(): FloatArray {
            val lineForOffset = getTextLayout().getLineForOffset(getOffset())
            val leftTop = floatArrayOf(
                    (getTextLayout().getPrimaryHorizontal(getOffset()).toInt() * getEditorScale()),
                    ((getTextLayout().getLineBottom(lineForOffset) + getVerticalOffset()) * getEditorScale())
            )
            getTransformMatrix(0f, 0f).mapPoints(leftTop)
            return leftTop
        }

        abstract fun getOffset(): Int

        abstract fun show()

        fun hide() {
            if (container.isShowing) {
                container.dismiss()
            }
        }

        abstract fun updateSelection(offset: Int)

        override fun onDraw(canvas: Canvas?) {
            val drawWidth = drawable.intrinsicWidth
            val left = 0
            val top = 0
            drawable.setBounds(left, top, left + drawWidth, top + drawable.intrinsicHeight)
            drawable.draw(canvas)
        }

        override fun onTouchEvent(event: MotionEvent?): Boolean {
            super.onTouchEvent(event!!)
            when {
                event.action == MotionEvent.ACTION_MOVE -> {
                    // calculating the height component when view is rotated
                    val heightComponent = floatArrayOf(0f, height.toFloat())
                    getTransformMatrix(0f, 0f).mapPoints(heightComponent)

                    val xy = floatArrayOf(
                            event.rawX - screenLocation[0] - heightComponent[0],
                            event.rawY - screenLocation[1] - heightComponent[1]
                    )

                    //De Rotating the the view to get exact view event's
                    val matrix = Matrix()
                    matrix.setRotate(-getShapeRotation())
                    matrix.mapPoints(xy)
                    //scaling the event
                    xy[0] /= getEditorScale()
                    xy[1] /= getEditorScale()

                    val position = shapeEditText.getOffsetForPosition(xy[0], xy[1])
                    updateSelection(position)
                    show()

                    //scroll on focus move
                }
            }
            return true
        }

        /*
        Getting X and Y position of the text cursor and setting the PopUp Window around it by calculation the
        rotated X ans Y position of the cursor.
        NOTE: these calculation are needed as PopUp Window has no method to rotate it
         */
        fun rotateTranslateUpdateContainer(xPos: Float, yPos: Float) {
            //getting the rotated area for PpoUp Window
            popUpWindowRect = RectF(xPos, yPos, xPos + drawable.intrinsicWidth, yPos + drawable.intrinsicHeight)
            getTransformMatrix(xPos, yPos).mapRect(popUpWindowRect)

            //setting pivot for view for rotating the  handle
            pivotX = 0f
            pivotY = 0f
            rotation = getShapeRotation()
            // translating the view to exact position of the pointer by calculating the difference b/w exact point and rotated point of PopUp Window
            translationX = Math.abs(xPos - popUpWindowRect.left)
            translationY = Math.abs(yPos - popUpWindowRect.top)

            if (container.isShowing) {
                container.update(popUpWindowRect.left.toInt(), popUpWindowRect.top.toInt(), popUpWindowRect.width().toInt(), popUpWindowRect.height().toInt())
            } else {
                container.showAtLocation(shapeEditText, Gravity.NO_GRAVITY, popUpWindowRect.left.toInt(), popUpWindowRect.top.toInt())
            }
        }

        fun isShowing(): Boolean = container.isShowing

    }

    private fun getTransformMatrix(x: Float, y: Float): Matrix {
        val matrix = Matrix()
        matrix.setRotate(getShapeRotation(), x, y)
        return matrix
    }

    private fun getShapeRotation(): Float {
        return 0f
    }

    inner class MiddleHandleView(context: Context) : HandleView(context, R.drawable.insertion_anchor) {

        override fun getOffset(): Int {
            return shapeEditText.selectionStart
        }

        override fun updateSelection(offset: Int) {
            Selection.setSelection(shapeEditText.editableText, offset)
        }

        override fun show() {
            // adding the drawable point location and rotated value
            val padding = floatArrayOf(-drawable.intrinsicWidth / 2f, 0f)
            getTransformMatrix(0f, 0f).mapPoints(padding)

            val xPos = (getHandleLeftTop()[0] + screenLocation[0]) + padding[0]
            val yPos = (screenLocation[1] + getHandleLeftTop()[1]) + padding[1]

            super.rotateTranslateUpdateContainer(xPos, yPos)
        }

        override fun onTouchEvent(event: MotionEvent?): Boolean {
            when (event?.actionMasked) {
                MotionEvent.ACTION_MOVE -> {
                    shapeEditText.removeCallbacks(runnable)
                }
                MotionEvent.ACTION_UP -> {
                    shapeEditText.removeCallbacks(runnable)
                    shapeEditText.postDelayed(runnable, ANCHOR_IDLE_TIME)
                }
            }
            return super.onTouchEvent(event)
        }
    }


}