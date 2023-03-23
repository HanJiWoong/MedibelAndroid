package com.exs.medivelskinmeasure.UI.MyPage

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import androidx.constraintlayout.widget.ConstraintLayout
import com.exs.medivelskinmeasure.Connection.ConnectionService
import com.exs.medivelskinmeasure.R
import com.exs.medivelskinmeasure.UI.Member.Join.JoinActivity
import com.exs.medivelskinmeasure.UI.Member.Join.TermDetailActivity
import com.exs.medivelskinmeasure.common.CommonUtil
import com.exs.medivelskinmeasure.common.custom_ui.CommonCheckBox
import com.exs.medivelskinmeasure.common.custom_ui.CommonTextListView
import com.exs.medivelskinmeasure.common.custom_ui.CommonTitleBar

val LogoutRequestCode = 200

class MyPageActivity : AppCompatActivity() {



    private lateinit var mTitleBar: CommonTitleBar

    private lateinit var mTVLogoutID: TextView
    private lateinit var mBtnLogout:AppCompatButton

    private lateinit var mViewMemberInfoModify: CommonTextListView
    private lateinit var mViewSignOut: CommonTextListView
    private lateinit var mViewPWChange: CommonTextListView
    private lateinit var mViewAppSetting: CommonTextListView
    private lateinit var mViewTerms:CommonTextListView
    private lateinit var mViewPersonalInfo:CommonTextListView
    private lateinit var mViewHomePage:CommonTextListView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_page)

        initUI()
        setCommonListener()
    }

    override fun onResume() {
        super.onResume()

        val token = CommonUtil.getPreferenceString(this, getString(R.string.pref_key_auto_login_token))
        token?.let {
            ConnectionService.getUserInfo(it, { result, data ->
                runOnUiThread {
                    if(result) {
                         mTVLogoutID.text = data!!.data.content!!.memberId
                    }
                }
            })
        }
    }

    private fun initUI() {
        mTitleBar = findViewById(R.id.ViewMyPageTitleBar)
        mTitleBar.mStrTitle = getString(R.string.str_ko_my_page_title)

        mViewMemberInfoModify = findViewById(R.id.ViewMyPageMemberInfoModify)
        mViewMemberInfoModify.mStrTitle = getString(R.string.str_ko_my_page_member_info_modify)

        mViewSignOut = findViewById(R.id.ViewMyPageSignOut)
        mViewSignOut.mStrTitle = getString(R.string.str_ko_my_page_sign_out)

        mViewPWChange = findViewById(R.id.ViewMyPagePWChange)
        mViewPWChange.mStrTitle = getString(R.string.str_ko_my_page_pw_change)

        mViewAppSetting = findViewById(R.id.ViewMyPageAppSetting)
        mViewAppSetting.mStrTitle = getString(R.string.str_ko_my_page_app_setting)

        mViewTerms = findViewById(R.id.ViewMyPageTerms)
        mViewTerms.mStrTitle = getString(R.string.str_ko_my_page_terms)

        mViewPersonalInfo = findViewById(R.id.ViewMyPagePersonalInfo)
        mViewPersonalInfo.mStrTitle = getString(R.string.str_ko_my_page_personal_info)

        mViewHomePage = findViewById(R.id.ViewMyPageHomePage)
        mViewHomePage.mStrTitle = getString(R.string.str_ko_my_page_home_page)

        mTVLogoutID = findViewById(R.id.TVMyPageLogoutID)
        mBtnLogout = findViewById(R.id.BtnMyPageLogout)
    }

    private fun setCommonListener() {
        mTitleBar.setOnClickBackListener {
            finish()
        }

        mViewMemberInfoModify.setOnClickListener {
            val intent = Intent(this@MyPageActivity,ModifyMemberInfoActivity::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.anim_fade_in, R.anim.anim_do_not_move)
        }

        mViewSignOut.setOnClickListener {
            val intent = Intent(this@MyPageActivity,SignOutActivity::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.anim_fade_in, R.anim.anim_do_not_move)
        }

        mViewPWChange.setOnClickListener {
            val intent = Intent(this@MyPageActivity,PWChangeActivity::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.anim_fade_in, R.anim.anim_do_not_move)
        }

        mViewAppSetting.setOnClickListener {
            val intent = Intent(this@MyPageActivity,AppSettingActivity::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.anim_fade_in, R.anim.anim_do_not_move)
        }

        mViewTerms.setOnClickListener {
            val intent = Intent(this@MyPageActivity, TermDetailActivity::class.java)
            intent.putExtra(getString(R.string.intent_key_term_detail_type),getString(R.string.intent_data_term_service))
            startActivity(intent)
            overridePendingTransition(R.anim.anim_fade_in, R.anim.anim_do_not_move)
        }

        mViewPersonalInfo.setOnClickListener {
            val intent = Intent(this@MyPageActivity, TermDetailActivity::class.java)
            intent.putExtra(getString(R.string.intent_key_term_detail_type),getString(R.string.intent_data_term_personal))
            startActivity(intent)
            overridePendingTransition(R.anim.anim_fade_in, R.anim.anim_do_not_move)
        }

        mViewHomePage.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.url_home_page_medibel)))
            startActivity(intent)
        }

        mBtnLogout.setOnClickListener {
            CommonUtil.setPreferenceString(this@MyPageActivity, getString(R.string.pref_key_is_auto_login), "false")
            CommonUtil.setPreferenceString(this@MyPageActivity, getString(R.string.pref_key_auto_login_token), "")

            setResult(LogoutRequestCode)
            finish()

        }
    }
}