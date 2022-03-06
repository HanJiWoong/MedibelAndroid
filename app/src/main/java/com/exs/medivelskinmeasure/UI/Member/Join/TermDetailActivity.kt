package com.exs.medivelskinmeasure.UI.Member.Join

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.widget.AppCompatButton
import androidx.constraintlayout.widget.ConstraintLayout
import com.exs.medivelskinmeasure.R
import com.exs.medivelskinmeasure.common.custom_ui.CommonCheckBox
import com.exs.medivelskinmeasure.common.custom_ui.CommonTitleBar

class TermDetailActivity : AppCompatActivity() {
    private lateinit var mTitleBar: CommonTitleBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_term_detail)

        initUI()
        setCommonListener()
    }

    private fun initUI() {
        mTitleBar = findViewById(R.id.ViewTermDetailTitleBar)

        val typeStr = intent.getStringExtra(getString(R.string.intent_key_term_detail_type))
        typeStr?.let { setTermType(it) }
    }

    private fun setCommonListener() {
        mTitleBar.setOnClickBackListener {
            finish()
        }
    }

    fun setTermType(type:String) {
        if(type == getString(R.string.intent_data_term_service)) {
            mTitleBar.mStrTitle = getString(R.string.str_ko_terms_service_agree_title)
        } else if(type == getString(R.string.intent_data_term_personal)) {
            mTitleBar.mStrTitle = getString(R.string.str_ko_terms_personal_agree_title)
        }
    }
}