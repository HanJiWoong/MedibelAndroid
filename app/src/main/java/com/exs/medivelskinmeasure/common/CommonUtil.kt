package com.exs.medivelskinmeasure.common

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.net.wifi.WifiManager
import android.text.format.Formatter.formatIpAddress
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityCompat.getSystemService
import androidx.core.app.ActivityCompat.shouldShowRequestPermissionRationale
import androidx.core.content.ContextCompat
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.conn.util.InetAddressUtils
import java.math.BigInteger
import java.net.InetAddress
import java.net.NetworkInterface
import java.security.MessageDigest
import java.util.*


object CommonUtil {
    fun requestPermissions(
        context: Context,
        permission: Array<String>,
        requestCode: Int,
        performAction: () -> Unit
    ) {
        when {

            ContextCompat.checkSelfPermission(
                context,
                permission.get(0)
            ) == PackageManager.PERMISSION_GRANTED -> {
                performAction()
            }

            shouldShowRequestPermissionRationale(context as Activity, permission.get(0)) -> {
                ActivityCompat.requestPermissions(
                    context,
                    permission,
                    requestCode
                )
            }


            else -> {
                ActivityCompat.requestPermissions(
                    context,
                    permission,
                    requestCode
                )
            }
        }
    }

    fun hasPermissions(context: Context, vararg permissions: String): Boolean = permissions.all {
        ActivityCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_GRANTED
    }

    fun md5(input: String): String {
        val md = MessageDigest.getInstance("MD5")
        return BigInteger(1, md.digest(input.toByteArray())).toString(16).padStart(32, '0')
    }

    fun getIPAddress(useIPv4: Boolean): String {
        try {
            val interfaces: List<NetworkInterface> = Collections.list(NetworkInterface.getNetworkInterfaces())
            for (intf in interfaces) {
                val addrs: List<InetAddress> = Collections.list(intf.inetAddresses)
                for (addr in addrs) {
                    if (!addr.isLoopbackAddress) {
                        val sAddr = addr.hostAddress.toUpperCase()
                        val isIPv4 = InetAddressUtils.isIPv4Address(sAddr)
                        if (useIPv4) {
                            if (isIPv4) return sAddr
                        } else {
                            if (!isIPv4) {
                                val delim = sAddr.indexOf('%') // drop ip6 port suffix
                                return if (delim < 0) sAddr else sAddr.substring(0, delim)
                            }
                        }
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return ""
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

    fun showKeyboard(context: Context, input: AppCompatEditText) {
        val inputMethodManager =
            context.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.showSoftInput(input, 0)
    }

    fun hideKeyboard(context: Context, input: AppCompatEditText) {
        val inputMethodManager =
            context.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(input.windowToken, 0)
    }

    /**
     * UUID 처리
     */
    fun initUUID(context:Context) {
        val pref: SharedPreferences =
            context.getSharedPreferences("KEY_PREF", Activity.MODE_PRIVATE)
        var uuid:String? = pref.getString("KEY_UUID",null)

        if(uuid == null) {
            val editor: SharedPreferences.Editor = pref.edit()

            uuid = UUID.randomUUID().toString()
            editor.putString("KEY_UUID",uuid).apply()
        }
    }

    fun getUUID(context:Context):String {
        val pref: SharedPreferences =
            context.getSharedPreferences("KEY_PREF", Activity.MODE_PRIVATE)
        return pref.getString("KEY_UUID",null).toString()
    }


}