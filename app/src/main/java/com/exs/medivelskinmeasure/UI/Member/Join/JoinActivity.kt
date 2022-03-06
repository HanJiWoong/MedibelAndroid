package com.exs.medivelskinmeasure.UI.Member.Join

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatEditText
import androidx.constraintlayout.widget.ConstraintLayout
import com.exs.medivelskinmeasure.R
import com.exs.medivelskinmeasure.common.custom_ui.CommonCheckBox
import com.exs.medivelskinmeasure.common.custom_ui.CommonTitleBar
import com.exs.medivelskinmeasure.common.popup.CommonTextPopup

class JoinActivity : AppCompatActivity() {
    private lateinit var mTitleBar: CommonTitleBar

    private lateinit var mETOrganization: AppCompatEditText
    private lateinit var mCBEmail:CommonCheckBox
    private lateinit var mCBMobile:CommonCheckBox

    private lateinit var mBtnJoin: AppCompatButton

    private var mIsShowOrganizationNotice:Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_join)

        initUI()
        setCommonListener()
    }

    private fun initUI() {
        mTitleBar = findViewById(R.id.ViewJoinTitleBar)
        mTitleBar.mStrTitle = getString(R.string.str_ko_join_info_title)

        mETOrganization = findViewById(R.id.ETJoinInfoOrganization)

        mCBEmail = findViewById(R.id.CBJoinInfoSelectorEmail)
        mCBEmail.mStrTitle = getString(R.string.str_ko_join_info_email_text)

        mCBMobile = findViewById(R.id.CBJoinInfoSelectorMobile)
        mCBMobile.mStrTitle = getString(R.string.str_ko_join_info_mobile_text)

        mBtnJoin = findViewById(R.id.BtnJoin)
    }

    private fun setCommonListener() {
        mTitleBar.setOnClickBackListener {
            finish()
        }

        mETOrganization.setOnFocusChangeListener { view, hasFocus ->
            if(hasFocus && !mIsShowOrganizationNotice) {
                mIsShowOrganizationNotice = true

                val intent = Intent(this@JoinActivity, CommonTextPopup::class.java)
                intent.putExtra(getString(R.string.intent_key_common_text_popup_content),getString(R.string.str_ko_Join_organization_notice_content))
                startActivity(intent)
                overridePendingTransition(R.anim.anim_fade_in, R.anim.anim_do_not_move)
            }
        }

        mBtnJoin.setOnClickListener {
            val intent = Intent(this@JoinActivity, JoinCompleteActivity::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.anim_fade_in, R.anim.anim_do_not_move)
        }
    }
}