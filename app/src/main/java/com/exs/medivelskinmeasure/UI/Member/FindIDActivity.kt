package com.exs.medivelskinmeasure.UI.Member

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.constraintlayout.widget.ConstraintLayout
import com.exs.medivelskinmeasure.R
import com.exs.medivelskinmeasure.common.custom_ui.CommonTitleBar
import com.exs.medivelskinmeasure.common.custom_ui.CommonTwoTabBar
import com.exs.medivelskinmeasure.common.custom_ui.TwoTabState

class FindIDActivity : AppCompatActivity() {


    private lateinit var mTitleBar:CommonTitleBar
    private lateinit var mTabBar:CommonTwoTabBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_find_id)

        initUI()
        setCommonListener()
    }

    private fun initUI() {
        mTitleBar = findViewById(R.id.ViewFindIdTitleBar)
        mTitleBar.mStrTitle = getString(R.string.str_ko_id_find_title)

        mTabBar = findViewById(R.id.ViewFindIdTabBar)
        mTabBar.setText(getString(R.string.str_ko_find_tab_first),getString(R.string.str_ko_find_tab_second))
        mTabBar.select(TwoTabState.First)
    }

    private fun setCommonListener() {
        mTitleBar.setOnClickBackListener {
            finish()
        }
    }
}