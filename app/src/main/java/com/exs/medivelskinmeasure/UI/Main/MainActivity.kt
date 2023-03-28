package com.exs.medivelskinmeasure.UI.Main

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatImageButton
import com.exs.medivelskinmeasure.Constants
import com.exs.medivelskinmeasure.Device.mqtt.MqttClient
import com.exs.medivelskinmeasure.MeasureMode
import com.exs.medivelskinmeasure.R
import com.exs.medivelskinmeasure.UI.Main.Component.CommonMainTop
import com.exs.medivelskinmeasure.UI.Member.LoginActivity
import com.exs.medivelskinmeasure.UI.MyPage.LogoutRequestCode
import com.exs.medivelskinmeasure.UI.MyPage.MyPageActivity


class MainActivity : AppCompatActivity() {
    private var waitTime = 0L


    private lateinit var mViewMainTop:CommonMainTop
    private lateinit var mIBHospital:AppCompatImageButton
    private lateinit var mIBBeautyShop:AppCompatImageButton
    private lateinit var mIBClinical:AppCompatImageButton
    private lateinit var mIBAnimalHospital:AppCompatImageButton


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initUI()
        setCommonListener()
        registMQTTListener()
    }

    override fun onDestroy() {
        super.onDestroy()
        MqttClient.disconnectMqtt()
    }

    override fun onResume() {
        super.onResume()

        Constants.measureMode = MeasureMode.None
        MqttClient.checkMqttConnection(this, {result ->
            if(result) {

            } else {
                Toast.makeText(this@MainActivity, getString(R.string.str_ko_toast_device_connect_fail), Toast.LENGTH_SHORT).show()
            }
        })
    }

    override fun onBackPressed() {
        if(System.currentTimeMillis() - waitTime > 1500) {
            waitTime = System.currentTimeMillis()
            Toast.makeText(this, "한 번 더 누르시면 앱을 종료합니다.", Toast.LENGTH_SHORT).show()
        } else finish()
    }

    fun initUI() {
        mViewMainTop = findViewById(R.id.ViewMainTop)
        mViewMainTop.setLogoButtonEnabled(false)
        mViewMainTop.showLogo()

        mIBHospital = findViewById(R.id.IBMainHospital)
        mIBBeautyShop = findViewById(R.id.IBMainBeautyShop)
        mIBClinical = findViewById(R.id.IBMainClinical)
        mIBAnimalHospital = findViewById(R.id.IBMainAnimalHospital)
    }

    fun setCommonListener() {
        mIBHospital.setOnClickListener {
            val intent = Intent(this@MainActivity, MainHospitalActivity::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.anim_fade_in, R.anim.anim_do_not_move)
        }

        mIBBeautyShop.setOnClickListener {
            val intent = Intent(this@MainActivity, MainBeautyShopActivity::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.anim_fade_in, R.anim.anim_do_not_move)
        }

        mIBClinical.setOnClickListener {
            val intent = Intent(this@MainActivity, MainClinicalActivity::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.anim_fade_in, R.anim.anim_do_not_move)
        }

        mIBAnimalHospital.setOnClickListener {
            val intent = Intent(this@MainActivity, MainAnimalHospitalActivity::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.anim_fade_in, R.anim.anim_do_not_move)
        }

        mViewMainTop.logoutLauncher = registerForActivityResult(StartActivityForResult()) {
            if(it.resultCode == LogoutRequestCode) {
                finish()
                val intent = Intent(this@MainActivity, LoginActivity::class.java)
                startActivity(intent)
                overridePendingTransition(R.anim.anim_fade_in, R.anim.anim_do_not_move)
            }
        }

    }

    fun registMQTTListener() {
        MqttClient.listener = object : MqttClient.MQTTClientInterface {
            override fun batterState(voltage: Double, level: Double) {
                if(level < 2) {
                    mViewMainTop.setBatteryState(false)
                } else {
                    mViewMainTop.setBatteryState(true)
                }
            }

            override fun responseUserInfo(result: Boolean, msg: String?) {
                super.responseUserInfo(result, msg)
            }
        }
    }


}