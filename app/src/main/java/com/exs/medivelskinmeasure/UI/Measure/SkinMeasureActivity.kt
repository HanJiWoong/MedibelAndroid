package com.exs.medivelskinmeasure.UI.Measure

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatImageView
import androidx.constraintlayout.widget.ConstraintLayout
import com.exs.medivelskinmeasure.R
import com.exs.medivelskinmeasure.common.custom_ui.CommonTitleBar

class SkinMeasureActivity : AppCompatActivity() {

    private lateinit var mTitleBar: CommonTitleBar

    private lateinit var mImgTemp: AppCompatImageView

    private lateinit var mCLProgressContent: ConstraintLayout
    private lateinit var mTVSkinMeasureInfo: TextView
    private lateinit var mTVSkinMeasureDesc: TextView

    private lateinit var mBtnMeasure: AppCompatButton

    private var mIsComplete: Boolean = false

    private var mMeasureStep: Int = 1
    private var mProgressUnit: Float = 1.0f / 11

    private lateinit var mArrTempImg: ArrayList<Drawable?>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_skin_measure)

        mArrTempImg = arrayListOf(
            getDrawable(R.drawable.capture1),
            getDrawable(R.drawable.capture2),
            getDrawable(R.drawable.capture3),
            getDrawable(R.drawable.capture4),
            getDrawable(R.drawable.capture5),
            getDrawable(R.drawable.capture6),
            getDrawable(R.drawable.capture7),
            getDrawable(R.drawable.capture8),
            getDrawable(R.drawable.capture1),
            getDrawable(R.drawable.capture10),
            getDrawable(R.drawable.capture11)
        )

        initUI()
        setCommonListener()
    }

    private fun initUI() {
        mTitleBar = findViewById(R.id.ViewSkinMeasureTitleBar)
        mTitleBar.mStrTitle = getString(R.string.str_ko_skin_measure)

        mImgTemp = findViewById(R.id.IVSkinMeasureExample)

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

            if (!mIsComplete) {

                if (mMeasureStep <= 11) {
                    it.isEnabled = false

                    Thread {

                        while (mMeasureStep <= 11) {

                            runOnUiThread {
                                mImgTemp.setImageDrawable(mArrTempImg.get(mMeasureStep - 1))

                                (mCLProgressContent.layoutParams as ConstraintLayout.LayoutParams)
                                    .matchConstraintPercentWidth = mMeasureStep * mProgressUnit
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

                            }

                            Thread.sleep(1000)

                            mMeasureStep++

                            if (mMeasureStep > 11) {
                                runOnUiThread { it.isEnabled = true }

                            }
                        }
                    }.start()

                } else {
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
                }

            } else {
                val intent = Intent(this@SkinMeasureActivity, SkinMeasureResultActivity::class.java)
                startActivity(intent)
                overridePendingTransition(R.anim.anim_fade_in, R.anim.anim_do_not_move)
            }

        }

    }
}