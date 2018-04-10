package com.sjkj.wanandroid.utils

import android.content.Context
import android.content.SharedPreferences
import com.sjkj.wanandroid.bean.UserBean
import com.sjkj.wanandroid.common.Common
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

/**
 * @author by dingl on 2018/3/2.
 * @desc PreferenceUtils
 */
@Suppress("UNCHECKED_CAST")
class PreferenceUtils<T>(private val name: String = "", private val defaultValue: T) : ReadWriteProperty<Any?, T> {

    companion object {

        lateinit var preferences: SharedPreferences

        fun setContext(context: Context) {
            preferences = context.getSharedPreferences(
                    context.packageName + Common.SHARED_NAME,
                    Context.MODE_PRIVATE
            )
        }

        fun clear() {
            preferences.edit().clear().apply()
        }
    }

    override fun getValue(thisRef: Any?, property: KProperty<*>): T = findPreference(name, defaultValue)

    override fun setValue(thisRef: Any?, property: KProperty<*>, value: T) {
        with(preferences.edit()) {
            when (value) {
                is Long -> putLong(name, value)
                is String -> putString(name, value)
                is Int -> putInt(name, value)
                is Boolean -> putBoolean(name, value)
                is Float -> putFloat(name, value)
                is UserBean -> {
                    putString(Common.USERNAME_KEY, value.username)
                    putString(Common.PASSWORD_KEY, value.password)
                    putString(Common.COLLECTION_IDS, value.collectIds.joinToString(","))
                    putBoolean(Common.LOGIN_KEY, value.isLogin)
                }
                else -> throw IllegalArgumentException("This type can be saved into Preferences")
            }.apply()
        }
    }

    private fun <U> findPreference(name: String, default: U): U = with(preferences) {
        val res: Any = when (default) {
            is Long -> getLong(name, default)
            is String -> getString(name, default)
            is Int -> getInt(name, default)
            is Boolean -> getBoolean(name, default)
            is Float -> getFloat(name, default)
            is UserBean -> {
                when (name) {
                    Common.USERNAME_KEY -> {
                        getString(name, default.username)
                    }
                    Common.PASSWORD_KEY -> {
                        getString(name, default.password)
                    }
                    Common.COLLECTION_IDS -> {
                        getString(name, default.collectIds?.joinToString(","))
                    }
                    Common.LOGIN_KEY -> {
                        getBoolean(name, default.isLogin)
                    }
                    else -> throw IllegalArgumentException("This type can be saved into Preferences")
                }
            }
            else -> throw IllegalArgumentException("This type can be saved into Preferences")
        }
        res as U
    }
}