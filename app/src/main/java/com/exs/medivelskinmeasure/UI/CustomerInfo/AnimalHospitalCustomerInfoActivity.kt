package com.exs.medivelskinmeasure.UI.CustomerInfo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.widget.AppCompatButton
import com.exs.medivelskinmeasure.R
import com.exs.medivelskinmeasure.UI.AnalysisResult.AnimalHospitalAnalysisResultActivity
import com.exs.medivelskinmeasure.UI.AnalysisResult.HospitalAnalysisResultActivity
import com.exs.medivelskinmeasure.common.custom_ui.CommonDropDownBarView
import com.exs.medivelskinmeasure.common.custom_ui.CommonTitleBar
import com.exs.medivelskinmeasure.common.custom_ui.CustomInputView
import com.exs.medivelskinmeasure.common.popup.CommonListPopup

class AnimalHospitalCustomerInfoActivity : AppCompatActivity() {
    private lateinit var mTitleBar: CommonTitleBar

    private lateinit var mViewCustomerNumber: CustomInputView
    private lateinit var mViewCustomerBirthYear: CommonDropDownBarView
    private lateinit var mViewCustomerGender: CommonDropDownBarView
    private lateinit var mViewCustomerRegion: CommonDropDownBarView

    private lateinit var mBtnAnalysisResult: AppCompatButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_animal_hospital_customer_info)

        initUI()
        setCommonListener()
    }

    private fun initUI() {
        mTitleBar = findViewById(R.id.ViewAnimalHospitalCustomerInfoTitleBar)
        mTitleBar.mStrTitle = getString(R.string.str_ko_customer_info_title)
        mTitleBar.setCloseMode(true)

        mViewCustomerNumber = findViewById(R.id.ViewAnimalHospitalCustomerInfoNumber)
        mViewCustomerNumber.mStrTitle = getString(R.string.str_ko_customer_info_number_title)
        mViewCustomerNumber.mStrHint = getString(R.string.str_ko_customer_info_number_hint)

        mViewCustomerBirthYear = findViewById(R.id.ViewAnimalHospitalCustomerInfoBirthYear)
        mViewCustomerBirthYear.mStrTitle = getString(R.string.str_ko_customer_info_birth_year_title)
        mViewCustomerBirthYear.mStrContent = ""

        mViewCustomerGender = findViewById(R.id.ViewAnimalHospitalCustomerInfoGender)
        mViewCustomerGender.mStrTitle = getString(R.string.str_ko_customer_info_gender_title)
        mViewCustomerGender.mStrContent = ""

        mViewCustomerRegion = findViewById(R.id.ViewAnimalHospitalCustomerInfoRegion)
        mViewCustomerRegion.mStrTitle = getString(R.string.str_ko_customer_info_region_title)
        mViewCustomerRegion.mStrContent = ""


        mBtnAnalysisResult = findViewById(R.id.BtnAnimalHospitalCustomerInfoShowAnalysis)
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
            startActivity(intent)
            overridePendingTransition(R.anim.anim_fade_in, R.anim.anim_do_not_move)
        }

        mViewCustomerGender.setOnClickListener {
            val intent = Intent(this, CommonListPopup::class.java)
            intent.putExtra(
                getString(R.string.intent_key_common_List_popup_type),
                getString(R.string.intent_data_common_list_popup_type_gender)
            )
            startActivity(intent)
            overridePendingTransition(R.anim.anim_fade_in, R.anim.anim_do_not_move)
        }

        mViewCustomerRegion.setOnClickListener {
            val intent = Intent(this, CommonListPopup::class.java)
            intent.putExtra(
                getString(R.string.intent_key_common_List_popup_type),
                getString(R.string.intent_data_common_list_popup_type_region)
            )
            startActivity(intent)
            overridePendingTransition(R.anim.anim_fade_in, R.anim.anim_do_not_move)
        }

        mBtnAnalysisResult.setOnClickListener {
            val intent = Intent(this, AnimalHospitalAnalysisResultActivity::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.anim_fade_in, R.anim.anim_do_not_move)
        }
    }
}