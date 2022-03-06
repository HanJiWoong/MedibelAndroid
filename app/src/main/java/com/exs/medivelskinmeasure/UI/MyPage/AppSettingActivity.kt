package com.exs.medivelskinmeasure.UI.MyPage

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.widget.AppCompatButton
import com.exs.medivelskinmeasure.R
import com.exs.medivelskinmeasure.common.custom_ui.CommonDropDownBarView
import com.exs.medivelskinmeasure.common.custom_ui.CommonTitleBar

class AppSettingActivity : AppCompatActivity() {
    private lateinit var mTitleBar: CommonTitleBar

    private lateinit var mViewLanguageSetting:CommonDropDownBarView

    private lateinit var mBtnConfirm: AppCompatButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_app_setting)

        initUI()
        setCommonListener()
    }

    private fun initUI() {
        mTitleBar = findViewById(R.id.ViewAppSettingTitleBar)
        mTitleBar.mStrTitle = getString(R.string.str_ko_my_page_app_setting)

        mViewLanguageSetting = findViewById(R.id.ViewAppSettingLanguage)
        mViewLanguageSetting.mStrTitle = getString(R.string.str_ko_app_setting_language_title)
        mViewLanguageSetting.mStrContent = getString(R.string.str_ko_lang_kor)


    }

    private fun setCommonListener() {
        mTitleBar.setOnClickBackListener {
            finish()
        }
    }
}