package com.example.anmol_5732.zoomit.view

import android.graphics.Rect
import android.view.TouchDelegate
import android.view.View

/**
 * Created by anmol-5732 on 26/02/18.
 */
class ZoomTouchDeligate(bounds: Rect, delegateView: View) : TouchDelegate(bounds, delegateView) {

//    private val mBounds: Rect
//    private val viewBounds = Rect()
//    private var mDelegateView: View
//    private var mDelegateTargeted: Boolean = false


    init {
//        mBounds = bounds
//        mDelegateView = delegateView
//        delegateView.getHitRect(viewBounds)

    }


//    override fun onTouchEvent(event: MotionEvent): Boolean {
//        var x = event.x.toInt()
//        var y = event.y.toInt()
//        var sendToDelegate = false
//        when (event.actionMasked) {
//            MotionEvent.ACTION_DOWN -> if (mBounds.contains(x, y)) {
//                mDelegateTargeted = true
//                sendToDelegate = true
//            }
//            MotionEvent.ACTION_POINTER_DOWN -> {
//                x = event.getX(event.pointerCount - 1).toInt()
//                y = event.getY(event.pointerCount - 1).toInt()
//                if (mBounds.contains(x, y)) {
//                    mDelegateTargeted = true
//                    sendToDelegate = true
//                }
//            }
//            MotionEvent.ACTION_UP, MotionEvent.ACTION_POINTER_UP, MotionEvent.ACTION_MOVE -> sendToDelegate = mDelegateTargeted
//            MotionEvent.ACTION_CANCEL -> {
//                sendToDelegate = mDelegateTargeted
//                mDelegateTargeted = false
//            }
//        }
//        if (sendToDelegate) {
//            val targetEvent = MotionEvent.obtain(event)
//            targetEvent.offsetLocation((-viewBounds.left).toFloat(), (-viewBounds.top).toFloat())
//            if (mDelegateView.dispatchTouchEvent(targetEvent)) {
//                return true
//            }
//        }
//        return false
//    }

}