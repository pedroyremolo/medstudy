package br.com.ia.medstudy.utils

import android.util.Log
import br.com.ia.medstudy.R

/**
 * Created on 07/11/19 - By Group E - O-MasE
 */

object ResourceUtils {

    fun getResourceDrawableIdByName(resourceName: String): Int {
        try {
            val res = R.drawable::class.java
            return res.getField(resourceName).getInt(null)
        } catch (e: Exception) {
            Log.e("MyTag", "Failure to get drawable id.", e)
            return 0
        }

    }

}