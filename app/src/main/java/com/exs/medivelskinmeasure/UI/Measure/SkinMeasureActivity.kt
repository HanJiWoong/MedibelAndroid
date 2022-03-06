package com.exs.medivelskinmeasure.UI.Measure

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import androidx.constraintlayout.widget.ConstraintLayout
import com.exs.medivelskinmeasure.R
import com.exs.medivelskinmeasure.common.custom_ui.CommonTitleBar

class SkinMeasureActivity : AppCompatActivity() {

    private lateinit var mTitleBar: CommonTitleBar

    private lateinit var mCLProgressContent: ConstraintLayout
    private lateinit var mTVSkinMeasureInfo: TextView
    private lateinit var mTVSkinMeasureDesc: TextView

    private lateinit var mBtnMeasure: AppCompatButton

    private var mIsComplete:Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_skin_measure)

        initUI()
        setCommonListener()
    }

    private fun initUI() {
        mTitleBar = findViewById(R.id.ViewSkinMeasureTitleBar)
        mTitleBar.mStrTitle = getString(R.string.str_ko_skin_measure)

        mCLProgressContent = findViewById(R.id.CLSkinMeasureProgressContent)

        val gd = GradientDrawable(
            GradientDrawable.Orientation.LEFT_RIGHT,
            intArrayOf(
                Color.parseColor("#ee7700"),
                Color.parseColor("#ebcc00")

            )
        )
        gd.cornerRadius = 9f

        mCLProgressContent.background = gd

        mTVSkinMeasureInfo = findViewById(R.id.TVSkinMeasureInfo)
        mTVSkinMeasureDesc = findViewById(R.id.TVSkinMeasureInfoDesc)

        mBtnMeasure = findViewById(R.id.BtnSkinMeasure)

    }

    private fun setCommonListener() {
        mTitleBar.setOnClickBackListener {
            finish()
        }

        mBtnMeasure.setOnClickListener {
            if(!mIsComplete) {
                mIsComplete = true

                mBtnMeasure.text = getString(R.string.str_ko_skin_measure_complete_btn_title)

                (mCLProgressContent.layoutParams as ConstraintLayout.LayoutParams)
                    .matchConstraintPercentWidth = 1.0f
                mCLProgressContent.requestLayout()

                val gd = GradientDrawable(
                    GradientDrawable.Orientation.LEFT_RIGHT,
                    intArrayOf(
                        Color.parseColor("#ee7700"),
                        Color.parseColor("#ebcc00")
                    )
                )
                gd.cornerRadius = 9f

                mCLProgressContent.background = gd

                mTVSkinMeasureInfo.text = getString(R.string.str_ko_skin_measure_complete)
                mTVSkinMeasureDesc.text = getString(R.string.str_ko_skin_measure_complete_desc)

                mTitleBar.setHiddenBackBtn(true)

            } else {
                val intent = Intent(this@SkinMeasureActivity, SkinMeasureResultActivity::class.java)
                startActivity(intent)
                overridePendingTransition(R.anim.anim_fade_in, R.anim.anim_do_not_move)
            }

        }

    }
}