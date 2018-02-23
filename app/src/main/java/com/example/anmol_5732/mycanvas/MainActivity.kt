package com.example.anmol_5732.mycanvas

import android.annotation.SuppressLint
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.example.anmol_5732.mycanvas.view.CanvasView
import com.example.anmol_5732.mycanvas.view.DemoView
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    @SuppressLint("ShowToast")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val demoView = DemoView(this)
        val canvasView = CanvasView(this)
        demoView.addView(canvasView)
        canvasContainer.setDrawingCacheEnabled(true);
        canvasContainer.addView(demoView)
    }

}
