package com.exs.medivelskinmeasure.UI.CustomerInfo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.widget.AppCompatButton
import com.exs.medivelskinmeasure.R
import com.exs.medivelskinmeasure.common.custom_ui.CommonDropDownBarView
import com.exs.medivelskinmeasure.common.custom_ui.CommonTitleBar
import com.exs.medivelskinmeasure.common.custom_ui.CustomInputView
import com.exs.medivelskinmeasure.common.popup.CommonListPopup

class AnimalClinicalInfoActivity : AppCompatActivity() {
    private lateinit var mTitleBar: CommonTitleBar

    private lateinit var mViewAnimalNumber: CustomInputView
    private lateinit var mViewAnimalBirthYear: CommonDropDownBarView
    private lateinit var mViewAnimalGender: CommonDropDownBarView
    private lateinit var mViewAnimalRegion: CommonDropDownBarView
    private lateinit var mViewAnimalSymptom: CustomInputView
    private lateinit var mViewAnimalDisease: CommonDropDownBarView
    private lateinit var mViewAnimalDiagnosis: CommonDropDownBarView
    private lateinit var mViewAnimalMemo: CustomInputView

    private lateinit var mBtnSend: AppCompatButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_animal_clinical_info)

        initUI()
        setCommonListener()
    }

    private fun initUI() {
        mTitleBar = findViewById(R.id.ViewAnimalClinicalCustomerInfoTitleBar)
        mTitleBar.mStrTitle = getString(R.string.str_ko_human_clinical_title)
        mTitleBar.setCloseMode(true)

        mViewAnimalNumber = findViewById(R.id.ViewAnimalClinicalNumber)
        mViewAnimalNumber.mStrTitle = getString(R.string.str_ko_human_clinical_number_title)
        mViewAnimalNumber.mStrHint = getString(R.string.str_ko_human_clinical_number_hint)

        mViewAnimalBirthYear = findViewById(R.id.ViewAnimalClinicalBirthYear)
        mViewAnimalBirthYear.mStrTitle = getString(R.string.str_ko_human_clinical_birth_year_title)
        mViewAnimalBirthYear.mStrContent = ""

        mViewAnimalGender = findViewById(R.id.ViewAnimalClinicalGender)
        mViewAnimalGender.mStrTitle = getString(R.string.str_ko_human_clinical_gender_title)
        mViewAnimalGender.mStrContent = ""

        mViewAnimalRegion = findViewById(R.id.ViewAnimalClinicalRegion)
        mViewAnimalRegion.mStrTitle = getString(R.string.str_ko_human_clinical_region_title)
        mViewAnimalRegion.mStrContent = ""

        mViewAnimalSymptom = findViewById(R.id.ViewAnimalClinicalSymptom)
        mViewAnimalSymptom.mStrTitle = getString(R.string.str_ko_human_clinical_symton_title)
        mViewAnimalSymptom.mStrHint = getString(R.string.str_ko_human_clinical_input_hint_1)


        mViewAnimalDisease = findViewById(R.id.ViewAnimalClinicalDieases)
        mViewAnimalDisease.mStrTitle = getString(R.string.str_ko_human_clinical_disease_title)
        mViewAnimalDisease.mStrContent = ""


        mViewAnimalDiagnosis = findViewById(R.id.ViewAnimalClinicalDiagnosis)
        mViewAnimalDiagnosis.mStrTitle = getString(R.string.str_ko_human_clinical_diagnosistitle)
        mViewAnimalDiagnosis.mStrContent = ""


        mViewAnimalMemo = findViewById(R.id.ViewAnimalClinicalMemo)
        mViewAnimalMemo.mStrTitle = getString(R.string.str_ko_human_clinical_memo_title)
        mViewAnimalMemo.mStrHint = getString(R.string.str_ko_human_clinical_input_hint_2)



        mBtnSend = findViewById(R.id.BtnAnimalClinicalSend)
    }

    private fun setCommonListener() {
        mTitleBar.setOnClickBackListener {
            finish()
        }

        mViewAnimalBirthYear.setOnClickListener {
            val intent = Intent(this, CommonListPopup::class.java)
            intent.putExtra(
                getString(R.string.intent_key_common_List_popup_type),
                getString(R.string.intent_data_common_list_popup_type_birth)
            )
            startActivity(intent)
            overridePendingTransition(R.anim.anim_fade_in, R.anim.anim_do_not_move)
        }

        mViewAnimalGender.setOnClickListener {
            val intent = Intent(this, CommonListPopup::class.java)
            intent.putExtra(
                getString(R.string.intent_key_common_List_popup_type),
                getString(R.string.intent_data_common_list_popup_type_gender)
            )
            startActivity(intent)
            overridePendingTransition(R.anim.anim_fade_in, R.anim.anim_do_not_move)
        }

        mViewAnimalRegion.setOnClickListener {
            val intent = Intent(this, CommonListPopup::class.java)
            intent.putExtra(
                getString(R.string.intent_key_common_List_popup_type),
                getString(R.string.intent_data_common_list_popup_type_region)
            )
            startActivity(intent)
            overridePendingTransition(R.anim.anim_fade_in, R.anim.anim_do_not_move)
        }

        mBtnSend.setOnClickListener {
        }
    }
}