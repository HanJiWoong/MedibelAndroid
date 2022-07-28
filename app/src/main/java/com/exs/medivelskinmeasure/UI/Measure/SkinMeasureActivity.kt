package com.exs.medivelskinmeasure.UI.Measure

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import com.exs.medivelskinmeasure.Device.mqtt.MQTTMeasuredResultData
import com.exs.medivelskinmeasure.Device.mqtt.MQTTMeasuringRequest
import com.exs.medivelskinmeasure.Device.mqtt.MqttClient
import com.exs.medivelskinmeasure.Device.mqtt.MqttDataStep
import com.exs.medivelskinmeasure.R
import com.exs.medivelskinmeasure.common.custom_ui.CommonTitleBar

class SkinMeasureActivity : AppCompatActivity() {

    private lateinit var mTitleBar: CommonTitleBar

    private lateinit var mCLProgressContent: ConstraintLayout
    private lateinit var mTVSkinMeasureInfo: TextView
    private lateinit var mTVSkinMeasureDesc: TextView

    private lateinit var mBtnMeasure: AppCompatButton

    private var mIsComplete: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_skin_measure)

        initUI()
        setCommonListener()

        MqttClient.requestMeasureStart()
        registMQTTCallback()
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
            if (!mIsComplete) {
//                mIsComplete = true
//
//                mBtnMeasure.text = getString(R.string.str_ko_skin_measure_complete_btn_title)
//
//                (mCLProgressContent.layoutParams as ConstraintLayout.LayoutParams)
//                    .matchConstraintPercentWidth = 1.0f
//                mCLProgressContent.requestLayout()
//
//                val gd = GradientDrawable(
//                    GradientDrawable.Orientation.LEFT_RIGHT,
//                    intArrayOf(
//                        Color.parseColor("#ee7700"),
//                        Color.parseColor("#ebcc00")
//                    )
//                )
//                gd.cornerRadius = 9f
//
//                mCLProgressContent.background = gd
//
//                mTVSkinMeasureInfo.text = getString(R.string.str_ko_skin_measure_complete)
//                mTVSkinMeasureDesc.text = getString(R.string.str_ko_skin_measure_complete_desc)
//
//                mTitleBar.setHiddenBackBtn(true)
                MqttClient.publishUserInfoSetting(this)


            } else {
                val intent = Intent(this@SkinMeasureActivity, SkinMeasureResultActivity::class.java)
                startActivity(intent)
                overridePendingTransition(R.anim.anim_fade_in, R.anim.anim_do_not_move)
            }

        }

    }

    private fun registMQTTCallback() {
        MqttClient.listener = object : MqttClient.MQTTClientInterface {
            override fun measuringData(data: MQTTMeasuringRequest) {
                val step = data.parameters.step

                when (step) {
                    MqttDataStep.waitImage.ordinal -> {
                        Toast.makeText(
                            this@SkinMeasureActivity,
                            data.parameters.message,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    MqttDataStep.measureImage.ordinal -> {
                        runOnUiThread {
                            val no = data.parameters.measure_data.image_no
                            val width:Float = (0.0909 * no).toFloat()

                            val params = mCLProgressContent.layoutParams as ConstraintLayout.LayoutParams

                            params.matchConstraintPercentWidth = width
                            mCLProgressContent.requestLayout()

                        }

                    }
                    MqttDataStep.measureTempMoisture.ordinal -> {

                    }
                }
            }

            override fun numericResultData(temp: Float, moisture: Int) {
                Toast.makeText(
                    this@SkinMeasureActivity,
                    "온도 -> ${temp}, 습도 -> ${moisture}",
                    Toast.LENGTH_LONG
                ).show()
            }

            override fun imageReusltData(data: MQTTMeasuredResultData) {
                Log.e("TEST","----------------------------------------------------------------------------------??????????????????????????????????????????????????????")

            }

            override fun measuredFinish() {
                val intent =
                    Intent(this@SkinMeasureActivity, SkinMeasureResultActivity::class.java)
                startActivity(intent)
                overridePendingTransition(R.anim.anim_fade_in, R.anim.anim_do_not_move)
            }
        }
    }
}