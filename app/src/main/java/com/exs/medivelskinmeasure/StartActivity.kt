package com.exs.medivelskinmeasure

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatDelegate
import com.exs.medivelskinmeasure.Connection.ConnectionService
import com.exs.medivelskinmeasure.UI.Main.MainActivity
import com.exs.medivelskinmeasure.UI.Member.LoginActivity
import com.exs.medivelskinmeasure.common.CommonUtil
import kotlinx.coroutines.*
import org.eclipse.paho.client.mqttv3.MqttClient
import java.util.concurrent.TimeUnit

class StartActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        CommonUtil.initUUID(this)

        val clientId =
            CommonUtil.getPreferenceString(this, getString(R.string.pref_key_device_clientId))

        if (clientId == null) {
            CommonUtil.setPreferenceString(
                this,
                getString(R.string.pref_key_device_clientId),
                MqttClient.generateClientId()
            )
        }

        GlobalScope.launch() {
            delay(1000)


            val autoLogin = CommonUtil.getPreferenceString(
                this@StartActivity,
                getString(R.string.pref_key_is_auto_login)
            )

            if (autoLogin.equals("false")) {
                val intent = Intent(this@StartActivity, LoginActivity::class.java)
                startActivity(intent)
                overridePendingTransition(R.anim.anim_fade_in, R.anim.anim_do_not_move)
                finish()
            } else {

                val token = CommonUtil.getPreferenceString(
                    this@StartActivity,
                    getString(R.string.pref_key_auto_login_token)
                )

                if (token == null) {

                    val intent = Intent(this@StartActivity, LoginActivity::class.java)
                    startActivity(intent)
                    overridePendingTransition(R.anim.anim_fade_in, R.anim.anim_do_not_move)
                    finish()
                } else {
                    if (token.isEmpty()) {
                        val intent = Intent(this@StartActivity, LoginActivity::class.java)
                        startActivity(intent)
                        overridePendingTransition(R.anim.anim_fade_in, R.anim.anim_do_not_move)
                        finish()
                    } else {
                        ConnectionService.autoLogin(token, { result, data ->
                            if (result) {


                                CommonUtil.setPreferenceString(
                                    this@StartActivity,
                                    getString(R.string.pref_key_auto_login_token),
                                    data?.data?.content?.token ?: ""
                                )


                                val intent = Intent(this@StartActivity, MainActivity::class.java)
                                startActivity(intent)
                                overridePendingTransition(
                                    R.anim.anim_fade_in,
                                    R.anim.anim_do_not_move
                                )
                                finish()
                            } else {

                                val intent = Intent(this@StartActivity, LoginActivity::class.java)
                                startActivity(intent)
                                overridePendingTransition(
                                    R.anim.anim_fade_in,
                                    R.anim.anim_do_not_move
                                )
                                finish()
                            }
                        })
                    }
                }
            }


        }

    }
}