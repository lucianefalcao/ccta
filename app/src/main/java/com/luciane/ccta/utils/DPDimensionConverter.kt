package com.luciane.ccta.utils

import android.util.DisplayMetrics
import android.util.TypedValue
import kotlin.math.roundToInt

class DPDimensionConverter {
    companion object{
        fun dpToPx(displayMetrics: DisplayMetrics, dp: Float): Int {
            return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, displayMetrics).roundToInt()
        }
    }
}