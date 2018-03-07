package com.example.anmol_5732.zoomit

import android.annotation.SuppressLint
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import com.example.anmol_5732.zoomit.view.ScaleContainer
import com.example.anmol_5732.zoomit.view.SqaureView
import com.example.anmol_5732.zoomit.view.ZoomView
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    @SuppressLint("ShowToast")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        container.clipChildren = false
        container.clipToPadding = false

        val zoomView = ZoomView(this)
        val scaleContainer = ScaleContainer(this)
        val sqaureView1 = SqaureView(this)
        val editText = EditText(this)
        editText.setText("EditText try writing something and translating through it after zooming in/out")
        editText.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        editText.clearFocus()
        sqaureView1.addView(editText)
        sqaureView1.translationX = 400f
        sqaureView1.translationY = 400f
        val sqaureView2 = SqaureView(this)
        val text = TextView(this)
        text.setText("MoveMe")
        sqaureView2.addView(text)
        scaleContainer.addView(sqaureView1)
        scaleContainer.addView(sqaureView2)
        zoomView.addView(scaleContainer)
        container.addView(zoomView)

    }

}
