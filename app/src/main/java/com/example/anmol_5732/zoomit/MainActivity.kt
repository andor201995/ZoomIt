package com.example.anmol_5732.zoomit

import android.annotation.SuppressLint
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.example.anmol_5732.zoomit.view.ScaleContainer
import com.example.anmol_5732.zoomit.view.SqaureView
import com.example.anmol_5732.zoomit.view.ZoomTextView
import com.example.anmol_5732.zoomit.view.ZoomView
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity(), ScaleCallBack {
    var text: ZoomTextView? = null
    override fun scaleChanged(scale: Float) {
        text!!.onscale(scale)
    }

    @SuppressLint("ShowToast")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        container.clipChildren = false
        container.clipToPadding = false

        val zoomView = ZoomView(this, this)
        val scaleContainer = ScaleContainer(this)
        val sqaureView = SqaureView(this)
        text = ZoomTextView(this)
        text!!.setText("hello there!")
        text!!.clearFocus()
        sqaureView.addView(text)
        sqaureView.translationX = 400f
        sqaureView.translationY = 400f
        scaleContainer.addView(sqaureView)
        zoomView.addView(scaleContainer)
        container.addView(zoomView)

    }


}
