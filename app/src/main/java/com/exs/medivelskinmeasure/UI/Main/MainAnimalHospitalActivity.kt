package com.exs.medivelskinmeasure.UI.Main

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.widget.AppCompatButton
import androidx.constraintlayout.widget.ConstraintLayout
import com.exs.medivelskinmeasure.Constants
import com.exs.medivelskinmeasure.MeasureMode
import com.exs.medivelskinmeasure.R
import com.exs.medivelskinmeasure.UI.Main.Component.CommonMainTop
import com.exs.medivelskinmeasure.UI.Measure.SkinMeasureActivity
import com.exs.medivelskinmeasure.UI.SendCheck.SendCheckActivity

class MainAnimalHospitalActivity : AppCompatActivity() {
    private lateinit var mViewMainTop: CommonMainTop

    private lateinit var mIBMesureSkin: AppCompatButton
    private lateinit var mCLSendList: ConstraintLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_animal_hospital)

        initUI()
        setCommonListener()
    }

    override fun onResume() {
        super.onResume()

        Constants.measureMode = MeasureMode.AnimalHospital
    }

    fun initUI() {
        mViewMainTop = findViewById(R.id.ViewMainHumanTop)

        mIBMesureSkin = findViewById(R.id.BtnMainHumanMeasureSkin)
        mCLSendList = findViewById(R.id.CLMainHumanShowSendList)
    }

    fun setCommonListener() {
        mIBMesureSkin.setOnClickListener {
            val intent = Intent(this@MainAnimalHospitalActivity, SkinMeasureActivity::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.anim_fade_in, R.anim.anim_do_not_move)
        }

        mCLSendList.setOnClickListener {
            val intent = Intent(this@MainAnimalHospitalActivity, SendCheckActivity::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.anim_fade_in, R.anim.anim_do_not_move)
        }
    }
}