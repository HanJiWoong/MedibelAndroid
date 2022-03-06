package com.exs.medivelskinmeasure.UI.AnalysisResult

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.exs.medivelskinmeasure.R
import com.exs.medivelskinmeasure.common.custom_ui.CommonTitleBar

class BeautyShopAnalysisResultActivity : AppCompatActivity() {
    private lateinit var mTitleBar: CommonTitleBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_beauty_shop_analysis_result)

        initUI()
        setCommonListener()
    }

    private fun initUI() {
        mTitleBar = findViewById(R.id.ViewBeautyShopAnalysisResultTitleBar)
        mTitleBar.mStrTitle = getString(R.string.str_ko_customer_info_title)
        mTitleBar.setCloseMode(true)
    }

    private fun setCommonListener() {
        mTitleBar.setOnClickBackListener {
            finish()
        }
    }
}