package com.exs.medivelskinmeasure.UI.Main

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.widget.AppCompatImageButton
import com.exs.medivelskinmeasure.Constants
import com.exs.medivelskinmeasure.MeasureMode

import com.exs.medivelskinmeasure.R
import com.exs.medivelskinmeasure.UI.Main.Component.CommonMainTop


class MainActivity : AppCompatActivity() {

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
    }

    override fun onResume() {
        super.onResume()

        Constants.measureMode = MeasureMode.None
    }

    fun initUI() {
        mViewMainTop = findViewById(R.id.ViewMainTop)
        mViewMainTop.setLogoButtonEnabled(false)

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


    }
}