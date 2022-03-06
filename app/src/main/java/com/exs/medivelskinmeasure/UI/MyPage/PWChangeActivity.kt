package com.exs.medivelskinmeasure.UI.MyPage

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.widget.AppCompatButton
import com.exs.medivelskinmeasure.R
import com.exs.medivelskinmeasure.common.custom_ui.CommonTitleBar

class PWChangeActivity : AppCompatActivity() {
    private lateinit var mTitleBar: CommonTitleBar

    private lateinit var mBtnConfirm: AppCompatButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pwchange)

        initUI()
        setCommonListener()
    }

    private fun initUI() {
        mTitleBar = findViewById(R.id.ViewPWChangeTitleBar)
        mTitleBar.mStrTitle = getString(R.string.str_ko_my_page_pw_change)

    }

    private fun setCommonListener() {
        mTitleBar.setOnClickBackListener {
            finish()
        }
    }
}