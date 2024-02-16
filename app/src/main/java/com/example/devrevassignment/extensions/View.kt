package com.example.devrevassignment.extensions

import android.content.res.Resources
import android.view.Gravity
import android.view.View
import android.widget.TextView
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.google.android.material.snackbar.Snackbar

fun View.gone(){
    this.visibility=View.GONE
}

fun View.visible(){
    this.visibility=View.VISIBLE
}

fun TextView.visible(){
    this.visibility= View.VISIBLE
}

fun TextView.gone(){
    this.visibility= View.GONE
}

val Int.dp:Int
    get() = (this * Resources.getSystem().displayMetrics.density).toInt()

fun Snackbar.setAnchorId(anchorId: Int): Snackbar {
    view.apply {
        val layoutParams = view.layoutParams as CoordinatorLayout.LayoutParams
        layoutParams.anchorId = anchorId
        layoutParams.gravity = Gravity.TOP or Gravity.CENTER_HORIZONTAL
        view.translationY = -8.dp.toFloat()
        view.layoutParams = layoutParams
    }

    return this
}