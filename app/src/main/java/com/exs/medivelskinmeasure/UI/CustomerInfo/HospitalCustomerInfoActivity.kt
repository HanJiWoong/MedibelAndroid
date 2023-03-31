package com.exs.medivelskinmeasure.UI.CustomerInfo

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Base64
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import com.exs.medivelskinmeasure.Device.mqtt.MqttClient
import com.exs.medivelskinmeasure.R
import com.exs.medivelskinmeasure.UI.AnalysisResult.HospitalAnalysisResultActivity
import com.exs.medivelskinmeasure.common.custom_ui.CommonDropDownBarView
import com.exs.medivelskinmeasure.common.custom_ui.CommonTitleBar
import com.exs.medivelskinmeasure.common.custom_ui.CustomInputView
import com.exs.medivelskinmeasure.common.popup.CommonListPopup
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async

class HospitalCustomerInfoActivity : AppCompatActivity() {
    private lateinit var mTitleBar: CommonTitleBar

    private lateinit var mViewCustomerNumber: CustomInputView
    private lateinit var mViewCustomerBirthYear: CommonDropDownBarView
    private lateinit var mViewCustomerGender: CommonDropDownBarView
    private lateinit var mViewCustomerRegion: CommonDropDownBarView
    private lateinit var mViewCustomerDetailRegion: CommonDropDownBarView

    private lateinit var mBtnAnalysisResult: AppCompatButton

    private var mArrResultImg: ArrayList<Bitmap> = ArrayList()

    val mPopupLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        val listType = it.data?.getStringExtra(getString(R.string.intent_key_common_List_popup_type))
        val selectedData = it.data?.getStringExtra(getString(R.string.intent_key_common_list_popup_selected_data))

        if (listType != null) {
            if(listType.equals(getString(R.string.intent_data_common_list_popup_type_birth))) {
                mViewCustomerBirthYear.mStrContent = selectedData.toString()
            } else if(listType.equals(getString(R.string.intent_data_common_list_popup_type_gender))) {
                mViewCustomerGender.mStrContent = selectedData.toString()
            } else if(listType.equals(getString(R.string.intent_data_common_list_popup_type_region))) {
                mViewCustomerRegion.mStrContent= selectedData.toString()

                if(selectedData.toString().equals("몸")) {
                    mViewCustomerDetailRegion.mStrContent = ""
                }
            } else if(listType.equals(getString(R.string.intent_data_common_list_popup_type_detail_region))) {
                mViewCustomerDetailRegion.mStrContent = selectedData.toString()
            }
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hospital_customer_info)

        initUI()
        setCommonListener()

        val dataArr = MqttClient.mResultImgArray

        CoroutineScope(Dispatchers.Default).async {
            if (dataArr != null) {
                for (data in dataArr) {

//                    val imgData: ByteArray = BitmapFactory.decodeStream(.byteInputStream())
                    val imgBytes = Base64.decode(data.image_data, 0)
                    val bitmap = BitmapFactory.decodeByteArray(imgBytes, 0, imgBytes.size)

                    mArrResultImg.add(bitmap)

                }
            }

//            runOnUiThread {
//                showImage()
//            }
        }
    }

    private fun initUI() {
        mTitleBar = findViewById(R.id.ViewHospitalCustomerInfoTitleBar)
        mTitleBar.mStrTitle = getString(R.string.str_ko_customer_info_title)
        mTitleBar.setCloseMode(true)

        mViewCustomerNumber = findViewById(R.id.ViewHospitalCustomerInfoNumber)
        mViewCustomerNumber.mStrTitle = getString(R.string.str_ko_customer_info_number_title)
        mViewCustomerNumber.mStrHint = getString(R.string.str_ko_customer_info_number_hint)

        mViewCustomerBirthYear = findViewById(R.id.ViewHospitalCustomerInfoBirthYear)
        mViewCustomerBirthYear.mStrTitle = getString(R.string.str_ko_customer_info_birth_year_title)
        mViewCustomerBirthYear.mStrContent = ""

        mViewCustomerGender = findViewById(R.id.ViewHospitalCustomerInfoGender)
        mViewCustomerGender.mStrTitle = getString(R.string.str_ko_customer_info_gender_title)
        mViewCustomerGender.mStrContent = ""

        mViewCustomerRegion = findViewById(R.id.ViewHospitalCustomerInfoRegion)
        mViewCustomerRegion.mStrTitle = getString(R.string.str_ko_customer_info_region_title)
        mViewCustomerRegion.mStrContent = ""

        mViewCustomerDetailRegion = findViewById(R.id.ViewHospitalCustomerInfoDetailRegion)
        mViewCustomerDetailRegion.mStrTitle =
            getString(R.string.str_ko_customer_info_detail_region_title)
        mViewCustomerDetailRegion.mStrContent = ""



        mBtnAnalysisResult = findViewById(R.id.BtnHospitalCustomerInfoAnalysisResult)
    }

    private fun setCommonListener() {
        mTitleBar.setOnClickBackListener {
            finish()
        }

        mViewCustomerBirthYear.setOnClickListener {
            val intent = Intent(this, CommonListPopup::class.java)
            intent.putExtra(
                getString(R.string.intent_key_common_List_popup_type),
                getString(R.string.intent_data_common_list_popup_type_birth)
            )
            mPopupLauncher.launch(intent)
            overridePendingTransition(R.anim.anim_fade_in, R.anim.anim_do_not_move)
        }

        mViewCustomerGender.setOnClickListener {
            val intent = Intent(this, CommonListPopup::class.java)
            intent.putExtra(
                getString(R.string.intent_key_common_List_popup_type),
                getString(R.string.intent_data_common_list_popup_type_gender)
            )
            mPopupLauncher.launch(intent)
            overridePendingTransition(R.anim.anim_fade_in, R.anim.anim_do_not_move)
        }

        mViewCustomerRegion.setOnClickListener {
            val intent = Intent(this, CommonListPopup::class.java)
            intent.putExtra(
                getString(R.string.intent_key_common_List_popup_type),
                getString(R.string.intent_data_common_list_popup_type_region)
            )
            mPopupLauncher.launch(intent)
            overridePendingTransition(R.anim.anim_fade_in, R.anim.anim_do_not_move)
        }

        mViewCustomerDetailRegion.setOnClickListener {
            if(mViewCustomerRegion.mStrContent.equals("얼굴")) {
                val intent = Intent(this, CommonListPopup::class.java)
                intent.putExtra(
                    getString(R.string.intent_key_common_List_popup_type),
                    getString(R.string.intent_data_common_list_popup_type_detail_region)
                )
                mPopupLauncher.launch(intent)
                overridePendingTransition(R.anim.anim_fade_in, R.anim.anim_do_not_move)
            } else {
                Toast.makeText(this, "얼굴만 선택 가능합니다.", Toast.LENGTH_SHORT).show()
            }

        }

        mBtnAnalysisResult.setOnClickListener {
            val intent = Intent(this, HospitalAnalysisResultActivity::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.anim_fade_in, R.anim.anim_do_not_move)
        }
    }
}