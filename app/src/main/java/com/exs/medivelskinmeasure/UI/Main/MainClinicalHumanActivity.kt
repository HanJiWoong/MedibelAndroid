package com.exs.medivelskinmeasure.UI.Main

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import androidx.constraintlayout.widget.ConstraintLayout
import com.exs.medivelskinmeasure.Constants
import com.exs.medivelskinmeasure.Device.mqtt.MqttClient
import com.exs.medivelskinmeasure.MeasureMode
import com.exs.medivelskinmeasure.R
import com.exs.medivelskinmeasure.UI.Main.Component.CommonMainTop
import com.exs.medivelskinmeasure.UI.Measure.SkinMeasureActivity
import com.exs.medivelskinmeasure.UI.SendCheck.SendCheckActivity

class MainClinicalHumanActivity : AppCompatActivity() {
    private lateinit var mViewMainTop: CommonMainTop

    private lateinit var mIBMesureSkin: AppCompatButton
    private lateinit var mCLSendList: ConstraintLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_clinical_human)

        initUI()
        setCommonListener()
    }

    override fun onResume() {
        super.onResume()

        Constants.measureMode = MeasureMode.HumanClinical
        val checkResult = MqttClient.checkMqttConnection(this)

        if(checkResult) {
            MqttClient.listener = object : MqttClient.MQTTClientInterface {
                override fun batterState(voltage: Double, level: Double) {
                    if(level < 2) {
                        mViewMainTop.setBatteryState(false)
                    } else {
                        mViewMainTop.setBatteryState(true)
                    }
                }

            }
        } else {
            Toast.makeText(this, getString(R.string.str_ko_toast_device_connect_fail), Toast.LENGTH_SHORT).show()
        }
    }

    fun initUI() {
        mViewMainTop = findViewById(R.id.ViewMainClinicalHumanTop)

        mIBMesureSkin = findViewById(R.id.BtnMainClinicalHumanMeasureSkin)
        mCLSendList = findViewById(R.id.CLMainClinicalHumanShowSendList)
    }

    fun setCommonListener() {
        mIBMesureSkin.setOnClickListener {
            val intent = Intent(this@MainClinicalHumanActivity, SkinMeasureActivity::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.anim_fade_in, R.anim.anim_do_not_move)
        }

        mCLSendList.setOnClickListener {
            val intent = Intent(this@MainClinicalHumanActivity, SendCheckActivity::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.anim_fade_in, R.anim.anim_do_not_move)
        }
    }
}