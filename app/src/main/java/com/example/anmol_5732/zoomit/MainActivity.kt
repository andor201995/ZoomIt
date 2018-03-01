package com.example.anmol_5732.zoomit

import android.annotation.SuppressLint
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.EditText
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
        val sqaureView = SqaureView(this)
        val text = EditText(this)
        text.setText("hello there!")
        text.clearFocus()
        sqaureView.addView(text)
        sqaureView.translationX = 400f
        sqaureView.translationY = 400f
        scaleContainer.addView(sqaureView)
        zoomView.addView(scaleContainer)
        container.addView(zoomView)

    }

}
