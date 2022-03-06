package com.exs.medivelskinmeasure.UI.Measure

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.widget.AppCompatButton
import com.exs.medivelskinmeasure.Constants
import com.exs.medivelskinmeasure.MeasureMode
import com.exs.medivelskinmeasure.R
import com.exs.medivelskinmeasure.UI.CustomerInfo.*
import com.exs.medivelskinmeasure.UI.Member.Join.JoinActivity
import com.exs.medivelskinmeasure.UI.Member.Join.TermDetailActivity
import com.exs.medivelskinmeasure.common.custom_ui.CommonTitleBar
import com.exs.medivelskinmeasure.UI.Main.MainActivity

class SkinMeasureResultActivity : AppCompatActivity() {

    private lateinit var mTitleBar: CommonTitleBar

    private lateinit var mBtnRetry:AppCompatButton
    private lateinit var mBtnNext:AppCompatButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_skin_measure_result)

        initUI()
        setCommonListener()
    }

    private fun initUI() {
        mTitleBar = findViewById(R.id.ViewSkinMeasureResultTitleBar)
        mTitleBar.mStrTitle = getString(R.string.str_ko_skin_measure)

        mBtnRetry = findViewById(R.id.BtnSkinMeasureRetry)
        mBtnNext = findViewById(R.id.BtnSkinMeasureNext)
    }

    private fun setCommonListener() {
        mBtnRetry.setOnClickListener {
            val intent = Intent(this, SkinMeasureActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP) // Removes other Activities from stack
            startActivity(intent)
            overridePendingTransition(R.anim.anim_fade_in, R.anim.anim_do_not_move)
        }

        mBtnNext.setOnClickListener {
            if(Constants.measureMode == MeasureMode.Hospital) {
                val intent = Intent(this, HospitalCustomerInfoActivity::class.java)
                startActivity(intent)
                overridePendingTransition(R.anim.anim_fade_in, R.anim.anim_do_not_move)
            } else if(Constants.measureMode == MeasureMode.BeautyShop) {
                val intent = Intent(this, BeautyShopCustomerInfoActivity::class.java)
                startActivity(intent)
                overridePendingTransition(R.anim.anim_fade_in, R.anim.anim_do_not_move)
            } else if(Constants.measureMode == MeasureMode.HumanClinical) {
                val intent = Intent(this, HumanClinicalInfoActivity::class.java)
                startActivity(intent)
                overridePendingTransition(R.anim.anim_fade_in, R.anim.anim_do_not_move)
            } else if(Constants.measureMode == MeasureMode.AnimalClinical) {
                val intent = Intent(this, AnimalClinicalInfoActivity::class.java)
                startActivity(intent)
                overridePendingTransition(R.anim.anim_fade_in, R.anim.anim_do_not_move)
            } else if(Constants.measureMode == MeasureMode.AnimalHospital) {
                val intent = Intent(this, AnimalHospitalCustomerInfoActivity::class.java)
                startActivity(intent)
                overridePendingTransition(R.anim.anim_fade_in, R.anim.anim_do_not_move)
            }
        }
    }
}