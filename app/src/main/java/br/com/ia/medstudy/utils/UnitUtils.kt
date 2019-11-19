package br.com.ia.medstudy.utils

import android.content.Context
import android.util.DisplayMetrics


/**
 * Created on 07/11/19 - By Group E - O-MasE
 */

class UnitUtils {

    companion object {
        fun convertDpToPx(context: Context, dp: Int): Int {
            val displayMetrics = context.resources.displayMetrics
            return Math.round((dp * (displayMetrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT)).toFloat())
        }

        fun convertPxToDp(context: Context, px: Int): Int {
            val displayMetrics = context.resources.displayMetrics
            return Math.round((px / (displayMetrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT)).toFloat())
        }
    }

}