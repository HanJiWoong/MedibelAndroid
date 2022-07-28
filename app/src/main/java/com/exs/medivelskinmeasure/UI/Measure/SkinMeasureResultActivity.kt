package com.exs.medivelskinmeasure.UI.Measure

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Base64
import android.view.View
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatImageButton
import androidx.appcompat.widget.AppCompatImageView
import com.exs.medivelskinmeasure.Constants
import com.exs.medivelskinmeasure.Device.mqtt.MQTTMeasuredResultData
import com.exs.medivelskinmeasure.Device.mqtt.MqttClient
import com.exs.medivelskinmeasure.MeasureMode
import com.exs.medivelskinmeasure.R
import com.exs.medivelskinmeasure.UI.CustomerInfo.*
import com.exs.medivelskinmeasure.UI.Member.Join.JoinActivity
import com.exs.medivelskinmeasure.UI.Member.Join.TermDetailActivity
import com.exs.medivelskinmeasure.common.custom_ui.CommonTitleBar
import com.exs.medivelskinmeasure.UI.Main.MainActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async

class SkinMeasureResultActivity : AppCompatActivity() {

    private lateinit var mTitleBar: CommonTitleBar

    private lateinit var mImageView: AppCompatImageView
    private lateinit var mTVCount: TextView

    private lateinit var mBtnRetry: AppCompatButton
    private lateinit var mBtnNext: AppCompatButton

    private lateinit var mBtnImgPrev: AppCompatImageButton
    private lateinit var mBtnImgNext: AppCompatImageButton

    private var mArrResultImg: ArrayList<Bitmap> = ArrayList()
    private var mArrIter: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_skin_measure_result)

        initUI()
        setCommonListener()

        val dataArr = MqttClient.mResultImgArray

        CoroutineScope(Dispatchers.Default).async {
            if (dataArr != null) {
                for (data in dataArr) {

//                    val imgData: ByteArray = BitmapFactory.decodeStream(.byteInputStream())
                    val imgBytes = Base64.decode(data.image_data,0)
                    val bitmap = BitmapFactory.decodeByteArray(imgBytes, 0, imgBytes.size)

                    mArrResultImg.add(bitmap)
                }
            }

            runOnUiThread {
                showImage()
            }
        }


    }

    private fun initUI() {
        mTitleBar = findViewById(R.id.ViewSkinMeasureResultTitleBar)
        mTitleBar.mStrTitle = getString(R.string.str_ko_skin_measure)

        mImageView = findViewById(R.id.IVSkinMeasureResultExample)
        mTVCount = findViewById(R.id.TVSkinMeasureResultControlCount)

        mBtnImgPrev = findViewById(R.id.IBSkinMeasureResultControlPrev)
        mBtnImgPrev.setOnClickListener(mOnClickPrev)
        mBtnImgNext = findViewById(R.id.IBSkinMeasureResultControlNext)
        mBtnImgNext.setOnClickListener(mOnClickNext)

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
            if (Constants.measureMode == MeasureMode.Hospital) {
                val intent = Intent(this, HospitalCustomerInfoActivity::class.java)
                startActivity(intent)
                overridePendingTransition(R.anim.anim_fade_in, R.anim.anim_do_not_move)
            } else if (Constants.measureMode == MeasureMode.BeautyShop) {
                val intent = Intent(this, BeautyShopCustomerInfoActivity::class.java)
                startActivity(intent)
                overridePendingTransition(R.anim.anim_fade_in, R.anim.anim_do_not_move)
            } else if (Constants.measureMode == MeasureMode.HumanClinical) {
                val intent = Intent(this, HumanClinicalInfoActivity::class.java)
                startActivity(intent)
                overridePendingTransition(R.anim.anim_fade_in, R.anim.anim_do_not_move)
            } else if (Constants.measureMode == MeasureMode.AnimalClinical) {
                val intent = Intent(this, AnimalClinicalInfoActivity::class.java)
                startActivity(intent)
                overridePendingTransition(R.anim.anim_fade_in, R.anim.anim_do_not_move)
            } else if (Constants.measureMode == MeasureMode.AnimalHospital) {
                val intent = Intent(this, AnimalHospitalCustomerInfoActivity::class.java)
                startActivity(intent)
                overridePendingTransition(R.anim.anim_fade_in, R.anim.anim_do_not_move)
            }
        }
    }

    var mOnClickNext = object : View.OnClickListener {
        override fun onClick(v: View?) {
            mArrIter++

            if (mArrIter == mArrResultImg.size - 1) {
                mBtnImgNext.isEnabled = false
            } else {
                mBtnImgPrev.isEnabled = true
            }

            showImage()
        }
    }

    var mOnClickPrev = object : View.OnClickListener {
        override fun onClick(v: View?) {
            mArrIter--

            if (mArrIter == 0) {
                mBtnImgPrev.isEnabled = false
            } else {
                mBtnImgNext.isEnabled = true
            }

            showImage()

        }
    }

    private fun showImage() {
        if (mArrResultImg.size > 0) {
            mImageView.setImageBitmap(mArrResultImg.get(mArrIter))
            mTVCount.setText("(${mArrIter+1}/${mArrResultImg.count()})")
        }
    }
}