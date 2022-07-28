package com.exs.medivelskinmeasure.UI.Main

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import com.exs.medivelskinmeasure.Constants
import com.exs.medivelskinmeasure.Device.mqtt.MqttClient
import com.exs.medivelskinmeasure.MeasureMode
import com.exs.medivelskinmeasure.R
import com.exs.medivelskinmeasure.UI.Main.Component.CommonMainTop

class MainClinicalActivity : AppCompatActivity() {
    private lateinit var mViewMainTop: CommonMainTop

    private lateinit var mIBHuman: AppCompatButton
    private lateinit var mIBAnimal: AppCompatButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_clinical)

        initUI()
        setCommonListener()
    }

    override fun onResume() {
        super.onResume()

        Constants.measureMode = MeasureMode.Clinical
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
        mViewMainTop = findViewById(R.id.ViewMainClinicalTop)

        mIBHuman = findViewById(R.id.BtnMainClinicalHumanMeasureSkin)
        mIBAnimal = findViewById(R.id.BtnMainClinicalAnimalMeasureSkin)
    }

    fun setCommonListener() {
        mIBHuman.setOnClickListener {
            val intent = Intent(this@MainClinicalActivity, MainClinicalHumanActivity::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.anim_fade_in, R.anim.anim_do_not_move)
        }

        mIBAnimal.setOnClickListener {
            val intent = Intent(this@MainClinicalActivity, MainClinicalAnimalActivity::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.anim_fade_in, R.anim.anim_do_not_move)
        }
    }
}