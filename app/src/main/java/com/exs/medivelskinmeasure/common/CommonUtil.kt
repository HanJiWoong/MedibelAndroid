package com.exs.medivelskinmeasure.common

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityCompat.getSystemService
import androidx.core.app.ActivityCompat.shouldShowRequestPermissionRationale
import androidx.core.content.ContextCompat


object CommonUtil {
    fun requestPermissions(
        context: Context,
        permission: String,
        requestCode: Int,
        performAction: () -> Unit
    ) {
        when {
            ContextCompat.checkSelfPermission(
                context,
                permission
            ) == PackageManager.PERMISSION_GRANTED -> {
                // You can use the API that requires the permission.
                performAction()
            }
            shouldShowRequestPermissionRationale(context as Activity, permission) -> {
                ActivityCompat.requestPermissions(
                    context,
                    arrayOf(permission),
                    requestCode
                )
            }
            else -> {
                ActivityCompat.requestPermissions(
                    context,
                    arrayOf(permission),
                    requestCode
                )
            }
        }
    }

    fun setPreferenceString(context: Context, key: String, value: String) {
        val pref: SharedPreferences =
            context.getSharedPreferences(context.packageName, Activity.MODE_PRIVATE)
        val editor: SharedPreferences.Editor = pref.edit()

        editor.putString(key, value)
        editor.apply()
    }

    fun getPreferenceString(context: Context, key: String): String? {
        val pref: SharedPreferences =
            context.getSharedPreferences(context.packageName, Activity.MODE_PRIVATE)
        return pref.getString(key, "")
    }

    fun showKeyboard(context:Context, input: AppCompatEditText) {
        val inputMethodManager = context.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.showSoftInput(input, 0)
    }

    fun hideKeyboard(context:Context, input: AppCompatEditText) {
        val inputMethodManager = context.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(input.windowToken, 0)
    }

}