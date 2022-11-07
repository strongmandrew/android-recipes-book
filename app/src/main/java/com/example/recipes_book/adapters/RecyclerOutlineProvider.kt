package com.example.recipes_book.adapters

import android.graphics.Outline
import android.graphics.Rect
import android.util.TypedValue
import android.view.View
import android.view.ViewOutlineProvider
import com.example.recipes_book.R

class RecyclerOutlineProvider: ViewOutlineProvider() {
    override fun getOutline(p0: View?, p1: Outline?) {
        val cornerRadiusDP = 40f

        val cornerRadius = TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                cornerRadiusDP,
                p0?.resources?.displayMetrics)

        p1?.setRoundRect(Rect(
            0,
            0,
            p0?.width ?:0,
            ((p0?.height?:0) + cornerRadius).toInt()),
            cornerRadius)
    }
}