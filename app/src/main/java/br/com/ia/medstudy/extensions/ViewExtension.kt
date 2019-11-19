package br.com.ia.medstudy.extensions

import android.support.annotation.IdRes
import android.view.View

/**
 * Created on 07/11/19 - By Group E - O-MasE
 */

fun <T : View> View.bind(@IdRes idRes: Int): Lazy<T> {
    return unsafeLazy { findViewById<T>(idRes) }
}

private fun <T> unsafeLazy(initializer: () -> T) = lazy(LazyThreadSafetyMode.NONE, initializer)