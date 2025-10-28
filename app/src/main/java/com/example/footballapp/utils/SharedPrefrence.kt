package com.example.footballapp.utils


import android.content.Context
import android.content.SharedPreferences
import com.example.footballapp.Helper.sharedPreferenceName

class SharedPrefrence (private  var context: Context){


    fun putingsBoolean(key: String, value: Boolean) {
        val sharedPref: SharedPreferences =
            context.getSharedPreferences(
                sharedPreferenceName,
                Context.MODE_PRIVATE
            )
        val editor = sharedPref.edit()
        editor.putBoolean(key, value)
        editor.apply()
    }

    fun putingInt( key: String, value: Int) {
        val sharedPref: SharedPreferences =
            context.getSharedPreferences(
                sharedPreferenceName,
                Context.MODE_PRIVATE
            )
        val editor = sharedPref.edit()
        editor.putInt(key, value)
        editor.apply()
    }

    fun putingString( key: String, value: String) {
        val sharedPref: SharedPreferences =
            context.getSharedPreferences(
                sharedPreferenceName,
                Context.MODE_PRIVATE
            )
        val editor = sharedPref.edit()
        editor.putString(key, value)
        editor.apply()
    }

    fun putingFloat( key: String, value: Float) {
        val sharedPref: SharedPreferences =
            context.getSharedPreferences(
                sharedPreferenceName,
                Context.MODE_PRIVATE
            )
        val editor = sharedPref.edit()
        editor.putFloat(key, value)
        editor.apply()
    }

    fun putingSet( key: String, value: Set<String>) {
        val sharedPref: SharedPreferences =
            context.getSharedPreferences(
                sharedPreferenceName,
                Context.MODE_PRIVATE
            )
        val editor = sharedPref.edit()
        editor.putStringSet(key, value)
        editor.apply()
    }

    fun getingFloat( key: String, default: Float = 0f): Float {
        val sharedPref: SharedPreferences =
            context.getSharedPreferences(
                sharedPreferenceName,
                Context.MODE_PRIVATE
            )
        return sharedPref.getFloat(key, default)
    }

    fun getingBoolean( key: String, default: Boolean = false): Boolean {
        val sharedPref: SharedPreferences =
            context.getSharedPreferences(
                sharedPreferenceName,
                Context.MODE_PRIVATE
            )
        return sharedPref.getBoolean(key, default)
    }

    fun getingInt( key: String, default: Int = 0): Int {
        val sharedPref: SharedPreferences =
            context.getSharedPreferences(
                sharedPreferenceName,
                Context.MODE_PRIVATE
            )
        return sharedPref.getInt(key, default)
    }

    fun getingString( key: String, default: String ): String {
        val sharedPref: SharedPreferences =
            context.getSharedPreferences(
                sharedPreferenceName,
                Context.MODE_PRIVATE
            )
        return sharedPref.getString(key, default)?:default
    }

    fun getSetString(key: String,
                     default: Set<String> = HashSet()
    ): Set<String> {
        val sharedPref: SharedPreferences =
            context.getSharedPreferences(
                sharedPreferenceName,
                Context.MODE_PRIVATE
            )
        return sharedPref.getStringSet(key, default) ?: default
    }


    fun getingLanguage(key: String, default: Boolean): Boolean {
        val sharedPr: SharedPreferences =
            context.getSharedPreferences(
                sharedPreferenceName,
                Context.MODE_PRIVATE
            )
        return sharedPr.getBoolean(key, false)
    }
    fun putingLanguage(key: String, value: Boolean) {
        val sharedPr: SharedPreferences =
            context.getSharedPreferences(
                sharedPreferenceName,
                Context.MODE_PRIVATE
            )
        val editor = sharedPr.edit()
        editor.putBoolean(key, value)
        editor.apply()
    }
    fun putingDefaultLanguage(key: String, value: String) {
        val sharedPr: SharedPreferences =
            context.getSharedPreferences(
                sharedPreferenceName,
                Context.MODE_PRIVATE
            )
        val editor = sharedPr.edit()
        editor.putString(key, value)
        editor.apply()
    }
    fun putingPosition(key: String, value: Int) {
        val sharedPr: SharedPreferences =
            context.getSharedPreferences(
                sharedPreferenceName,
                Context.MODE_PRIVATE
            )
        val editor = sharedPr.edit()
        editor.putInt(key, value)
        editor.apply()
    }
    fun getingPosition( key: String, default: Int): Int {
        val sharedPr: SharedPreferences =
            context.getSharedPreferences(
                sharedPreferenceName,
                Context.MODE_PRIVATE
            )
        return sharedPr.getInt(key, default)
    }


}