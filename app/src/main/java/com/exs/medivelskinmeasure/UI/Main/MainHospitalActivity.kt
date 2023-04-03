package com.exs.medivelskinmeasure.UI.Main

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import androidx.constraintlayout.widget.ConstraintLayout
import com.exs.medivelskinmeasure.Constants
import com.exs.medivelskinmeasure.Device.mqtt.MQTTMeasuringRequest
import com.exs.medivelskinmeasure.Device.mqtt.MqttClient
import com.exs.medivelskinmeasure.Device.mqtt.MqttDataStep
import com.exs.medivelskinmeasure.MeasureMode
import com.exs.medivelskinmeasure.R
import com.exs.medivelskinmeasure.UI.Main.Component.CommonMainTop
import com.exs.medivelskinmeasure.UI.Measure.SkinMeasureActivity
import com.exs.medivelskinmeasure.UI.SendCheck.SendCheckActivity

class MainHospitalActivity : AppCompatActivity() {
    private lateinit var mViewMainTop: CommonMainTop

    private lateinit var mBtnMesureSkin:AppCompatButton
    private lateinit var mCLSendList:ConstraintLayout

    private var isConnected = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_hospital)

        initUI()
        setCommonListener()
        registMQTTCallback()
    }

    override fun onResume() {
        super.onResume()

        Constants.measureMode = MeasureMode.Hospital
        MqttClient.checkMqttConnection(this, {result ->
            if(result) {
                isConnected = true
                mBtnMesureSkin.isEnabled = true
                mBtnMesureSkin.text = getString(R.string.str_ko_skin_measure)
            } else {
                Toast.makeText(this, getString(R.string.str_ko_toast_device_connect_fail), Toast.LENGTH_SHORT).show()

                isConnected = false
                mBtnMesureSkin.isEnabled = true
                mBtnMesureSkin.text = getString(R.string.str_ko_skin_measure_prepare)
            }
        })
    }

    fun initUI() {
        mViewMainTop = findViewById(R.id.ViewMainHospitalTop)
        mViewMainTop.showBack()

        mBtnMesureSkin = findViewById(R.id.BtnMainHospitalMeasureSkin)
        mBtnMesureSkin.isEnabled = false
        mCLSendList = findViewById(R.id.CLMainHospitalShowSendList)
    }

    fun setCommonListener() {
        mBtnMesureSkin.setOnClickListener {
            if(isConnected) {
                MqttClient.publishUserInfoSetting(this@MainHospitalActivity)
                mBtnMesureSkin.isEnabled = false
            } else {
                val intent = Intent(this@MainHospitalActivity, ConnectStateActivity::class.java)
                startActivity(intent)
                overridePendingTransition(R.anim.anim_fade_in, R.anim.anim_do_not_move)
            }
        }

        mCLSendList.setOnClickListener {
            val intent = Intent(this@MainHospitalActivity, SendCheckActivity::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.anim_fade_in, R.anim.anim_do_not_move)
        }
    }

    private fun registMQTTCallback() {
        MqttClient.listener = object : MqttClient.MQTTClientInterface {
            override fun batterState(voltage: Double, level: Double) {
                if(level < 2) {
                    mViewMainTop.setBatteryState(false)
                } else {
                    mViewMainTop.setBatteryState(true)
                }
            }

            override fun responseUserInfo(result: Boolean, msg: String?) {
                if(result) {

                } else {
                    mBtnMesureSkin.isEnabled = true
                    Toast.makeText(this@MainHospitalActivity, msg, Toast.LENGTH_SHORT).show()
                }
            }

            override fun measuringData(data: MQTTMeasuringRequest) {
                val step = data.parameters.step

                when (step) {
                    MqttDataStep.waitImage.ordinal -> {
                        val intent = Intent(this@MainHospitalActivity, SkinMeasureActivity::class.java)
                        startActivity(intent)
                        overridePendingTransition(R.anim.anim_fade_in, R.anim.anim_do_not_move)
                    }
                }
            }

        }
    }
}