package com.exs.medivelskinmeasure.UI.Member.Join

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.widget.AppCompatButton
import androidx.constraintlayout.widget.ConstraintLayout
import com.exs.medivelskinmeasure.R
import com.exs.medivelskinmeasure.common.custom_ui.CommonCheckBox
import com.exs.medivelskinmeasure.common.custom_ui.CommonTitleBar
import com.exs.medivelskinmeasure.common.custom_ui.TwoTabState

class TermsActivity : AppCompatActivity() {

    private lateinit var mTitleBar: CommonTitleBar

    private lateinit var mCLAllAgree:ConstraintLayout
    private lateinit var mCLServiceAgree:ConstraintLayout
    private lateinit var mCLPersonalAgree:ConstraintLayout

    private lateinit var mCBAllAgree:CommonCheckBox
    private lateinit var mCBServiceAgree:CommonCheckBox
    private lateinit var mCBPersonalAgree:CommonCheckBox

    private lateinit var mBtnNext:AppCompatButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_terms)

        initUI()
        setCommonListener()
    }

    private fun initUI() {
        mTitleBar = findViewById(R.id.ViewTermsTitleBar)
        mTitleBar.mStrTitle = getString(R.string.str_ko_terms)

        mCLAllAgree = findViewById(R.id.CLTermsAllAgree)
        mCBAllAgree = findViewById(R.id.CBTermsAllAgree)
        mCBAllAgree.mStrTitle = getString(R.string.str_ko_terms_all_agree)

        mCLServiceAgree = findViewById(R.id.CLTermsServiceAgree)
        mCBServiceAgree = findViewById(R.id.CBTermsServiceAgree)
        mCBServiceAgree.mStrTitle = getString(R.string.str_ko_terms_service_agree)

        mCLPersonalAgree = findViewById(R.id.CLTermsPersonalAgree)
        mCBPersonalAgree = findViewById(R.id.CBTermsPersonalAgree)
        mCBPersonalAgree.mStrTitle = getString(R.string.str_ko_terms_personal_agree)

        mBtnNext = findViewById(R.id.BtnTermsNext)
    }

    private fun setCommonListener() {
        mTitleBar.setOnClickBackListener {
            finish()
        }

        mCLServiceAgree.setOnClickListener {
            val intent = Intent(this@TermsActivity, TermDetailActivity::class.java)
            intent.putExtra(getString(R.string.intent_key_term_detail_type),getString(R.string.intent_data_term_service))
            startActivity(intent)
            overridePendingTransition(R.anim.anim_fade_in, R.anim.anim_do_not_move)
        }

        mCLPersonalAgree.setOnClickListener {
            val intent = Intent(this@TermsActivity, TermDetailActivity::class.java)
            intent.putExtra(getString(R.string.intent_key_term_detail_type),getString(R.string.intent_data_term_personal))
            startActivity(intent)
            overridePendingTransition(R.anim.anim_fade_in, R.anim.anim_do_not_move)
        }

        mBtnNext.setOnClickListener {
            val intent = Intent(this@TermsActivity, JoinActivity::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.anim_fade_in, R.anim.anim_do_not_move)
        }
    }
}