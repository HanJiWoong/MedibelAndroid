package com.exs.medivelskinmeasure.UI.CustomerInfo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.widget.AppCompatButton
import com.exs.medivelskinmeasure.R
import com.exs.medivelskinmeasure.UI.AnalysisResult.HospitalAnalysisResultActivity
import com.exs.medivelskinmeasure.common.custom_ui.CommonDropDownBarView
import com.exs.medivelskinmeasure.common.custom_ui.CommonTitleBar
import com.exs.medivelskinmeasure.common.custom_ui.CustomInputView
import com.exs.medivelskinmeasure.common.popup.CommonListPopup

class HumanClinicalInfoActivity : AppCompatActivity() {
    private lateinit var mTitleBar: CommonTitleBar

    private lateinit var mViewHumanNumber: CustomInputView
    private lateinit var mViewHumanBirthYear: CommonDropDownBarView
    private lateinit var mViewHumanGender: CommonDropDownBarView
    private lateinit var mViewHumanRegion: CommonDropDownBarView
    private lateinit var mViewHumanDetailRegion: CommonDropDownBarView
    private lateinit var mViewHumanSymptom: CustomInputView
    private lateinit var mViewHumanDisease: CustomInputView
    private lateinit var mViewHumanDiagnosis: CustomInputView
    private lateinit var mViewHumanMemo: CustomInputView

    private lateinit var mBtnSend: AppCompatButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_human_clinical_info)

        initUI()
        setCommonListener()
    }

    private fun initUI() {
        mTitleBar = findViewById(R.id.ViewHumanClinicalTitleBar)
        mTitleBar.mStrTitle = getString(R.string.str_ko_human_clinical_title)
        mTitleBar.setCloseMode(true)

        mViewHumanNumber = findViewById(R.id.ViewHumanClinicalNumber)
        mViewHumanNumber.mStrTitle = getString(R.string.str_ko_human_clinical_number_title)
        mViewHumanNumber.mStrHint = getString(R.string.str_ko_human_clinical_number_hint)

        mViewHumanBirthYear = findViewById(R.id.ViewHumanClinicalBirthYear)
        mViewHumanBirthYear.mStrTitle = getString(R.string.str_ko_human_clinical_birth_year_title)
        mViewHumanBirthYear.mStrContent = ""

        mViewHumanGender = findViewById(R.id.ViewHumanClinicalGender)
        mViewHumanGender.mStrTitle = getString(R.string.str_ko_human_clinical_gender_title)
        mViewHumanGender.mStrContent = ""

        mViewHumanRegion = findViewById(R.id.ViewHumanClinicalRegion)
        mViewHumanRegion.mStrTitle = getString(R.string.str_ko_human_clinical_region_title)
        mViewHumanRegion.mStrContent = ""

        mViewHumanDetailRegion = findViewById(R.id.ViewHospitalCustomerInfoDetailRegion)
        mViewHumanDetailRegion.mStrTitle =
            getString(R.string.str_ko_human_clinical_detail_region_title)
        mViewHumanDetailRegion.mStrContent = ""

        mViewHumanSymptom = findViewById(R.id.ViewHumanClinicalSymptom)
        mViewHumanSymptom.mStrTitle = getString(R.string.str_ko_human_clinical_symton_title)
        mViewHumanSymptom.mStrHint = getString(R.string.str_ko_human_clinical_input_hint_1)


        mViewHumanDisease = findViewById(R.id.ViewHumanClinicalDisease)
        mViewHumanDisease.mStrTitle = getString(R.string.str_ko_human_clinical_disease_title)
        mViewHumanDisease.mStrHint = getString(R.string.str_ko_human_clinical_input_hint_1)


        mViewHumanDiagnosis = findViewById(R.id.ViewHumanClinicalDiagnosis)
        mViewHumanDiagnosis.mStrTitle = getString(R.string.str_ko_human_clinical_diagnosistitle)
        mViewHumanDiagnosis.mStrHint = getString(R.string.str_ko_human_clinical_input_hint_1)


        mViewHumanMemo = findViewById(R.id.ViewHumanClinicalMemo)
        mViewHumanMemo.mStrTitle = getString(R.string.str_ko_human_clinical_memo_title)
        mViewHumanMemo.mStrHint = getString(R.string.str_ko_human_clinical_input_hint_2)


        mBtnSend = findViewById(R.id.BtnHumanClinicalSend)
    }

    private fun setCommonListener() {
        mTitleBar.setOnClickBackListener {
            finish()
        }

        mViewHumanBirthYear.setOnClickListener {
            val intent = Intent(this, CommonListPopup::class.java)
            intent.putExtra(
                getString(R.string.intent_key_common_List_popup_type),
                getString(R.string.intent_data_common_list_popup_type_birth)
            )
            startActivity(intent)
            overridePendingTransition(R.anim.anim_fade_in, R.anim.anim_do_not_move)
        }

        mViewHumanGender.setOnClickListener {
            val intent = Intent(this, CommonListPopup::class.java)
            intent.putExtra(
                getString(R.string.intent_key_common_List_popup_type),
                getString(R.string.intent_data_common_list_popup_type_gender)
            )
            startActivity(intent)
            overridePendingTransition(R.anim.anim_fade_in, R.anim.anim_do_not_move)
        }

        mViewHumanRegion.setOnClickListener {
            val intent = Intent(this, CommonListPopup::class.java)
            intent.putExtra(
                getString(R.string.intent_key_common_List_popup_type),
                getString(R.string.intent_data_common_list_popup_type_region)
            )
            startActivity(intent)
            overridePendingTransition(R.anim.anim_fade_in, R.anim.anim_do_not_move)
        }

        mViewHumanDetailRegion.setOnClickListener {

            val intent = Intent(this, CommonListPopup::class.java)
            intent.putExtra(
                getString(R.string.intent_key_common_List_popup_type),
                getString(R.string.intent_data_common_list_popup_type_detail_region)
            )
            startActivity(intent)
            overridePendingTransition(R.anim.anim_fade_in, R.anim.anim_do_not_move)
        }

        mBtnSend.setOnClickListener {
        }
    }
}