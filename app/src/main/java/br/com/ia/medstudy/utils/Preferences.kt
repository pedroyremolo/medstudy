package br.com.ia.medstudy.utils

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import br.com.ia.medstudy.domain.model.User

/**
 * Created on 07/11/19 - By Group E - O-MasE
 */

object Preferences {

    fun defaultPrefs(context: Context): SharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)

    inline fun SharedPreferences.edit(operation: (SharedPreferences.Editor) -> Unit) {
        val editor = this.edit()
        operation(editor)
        editor.apply()
    }

    operator fun SharedPreferences.set(key: String, value: Any?) {
        when (value) {
            is String? -> edit({ it.putString(key, value) })
            is Int -> edit({ it.putInt(key, value) })
            is Boolean -> edit({ it.putBoolean(key, value) })
            is Float -> edit({ it.putFloat(key, value) })
            is Long -> edit({ it.putLong(key, value) })
            else -> throw UnsupportedOperationException("Not yet implemented")
        }
    }

    operator inline fun <reified T : Any> SharedPreferences.get(key: String, defaultValue: T? = null): T? {
        return when (T::class) {
            String::class -> getString(key, defaultValue as? String) as T?
            Int::class -> getInt(key, defaultValue as? Int ?: -1) as T?
            Boolean::class -> getBoolean(key, defaultValue as? Boolean ?: false) as T?
            Float::class -> getFloat(key, defaultValue as? Float ?: -1f) as T?
            Long::class -> getLong(key, defaultValue as? Long ?: -1) as T?
            else -> throw UnsupportedOperationException("Not yet implemented")
        }
    }

    fun setUser(context: Context, user: User) {
        defaultPrefs(context)[Const.Preferences.CURRENT_LEVEL] = user.level.name
        defaultPrefs(context)[Const.Preferences.IS_REINFORCEMENT] = user.isReinforcement
    }

    fun getUser(context: Context): User {
        val currentLevel: String? = defaultPrefs(context)[Const.Preferences.CURRENT_LEVEL, User.Level.LEVELING.name]
        val isReinforcement: Boolean? = defaultPrefs(context)[Const.Preferences.IS_REINFORCEMENT, false]
        return User(User.Level.valueOf(currentLevel!!), isReinforcement!!)
    }

}