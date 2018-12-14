package com.example.anmol_5732.zoomit.view

import android.content.Context
import android.graphics.Canvas
import android.os.ResultReceiver
import android.support.v7.widget.AppCompatTextView
import android.text.InputFilter
import android.text.Spanned
import android.text.method.ArrowKeyMovementMethod
import android.text.method.MovementMethod
import android.util.AttributeSet
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.TextView

class CustomEditText(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0, private val zoomView: ZoomViewListener) : AppCompatTextView(context, attrs, defStyleAttr), InputFilter, GestureDetector.OnGestureListener {

    override fun filter(source: CharSequence?, start: Int, end: Int, dest: Spanned?, dstart: Int, dend: Int): CharSequence? {
        return null
    }

    private val cursorEditor = CursorEditor(this)

    init {
        makeEditable()
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        cursorEditor.onDetachedFromWindow()
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        cursorEditor.onAttachedToWindow()
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        cursorEditor.onDraw()
    }

    override fun setText(text: CharSequence?, type: TextView.BufferType?) {
        super.setText(text, BufferType.EDITABLE)
    }

    override fun getDefaultEditable(): Boolean {
        return true
    }

    override fun getDefaultMovementMethod(): MovementMethod {
        return ArrowKeyMovementMethod.getInstance()
    }

    override fun onCheckIsTextEditor(): Boolean {
        return true
    }

    fun getEditorScale(): Float {
        return zoomView.getCurrentScaleFactor()
    }

    private fun makeEditable() {
        setTextIsSelectable(true)
        isEnabled = true
        isFocusable = true
        isFocusableInTouchMode = true
        isCursorVisible = true
        requestFocus()
        showKeyboard()
        imeOptions = EditorInfo.IME_FLAG_NO_EXTRACT_UI

    }

    private fun showKeyboard() {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
        imm?.viewClicked(this)
        imm?.showSoftInput(this, 0, object : ResultReceiver(this.handler) {
        })
    }

    override fun onSingleTapUp(event: MotionEvent?): Boolean {
        cursorEditor.onSingeTap(event)
        invalidate()
        imeOptions = EditorInfo.IME_FLAG_NO_EXTRACT_UI
        return true
    }

    override fun onShowPress(e: MotionEvent?) {
    }

    override fun onDown(e: MotionEvent?): Boolean {
        return true
    }

    override fun onFling(e1: MotionEvent?, e2: MotionEvent?, velocityX: Float, velocityY: Float): Boolean {
        return false
    }

    override fun onScroll(e1: MotionEvent?, e2: MotionEvent?, distanceX: Float, distanceY: Float): Boolean {
        return false
    }

    override fun onLongPress(e: MotionEvent?) {
    }

}
