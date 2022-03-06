package com.exs.medivelskinmeasure.common.popup

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatImageButton
import com.exs.medivelskinmeasure.R

class CommonTextPopup : AppCompatActivity() {

    private lateinit var mTVContent: TextView
    private lateinit var mBtnConfirm: AppCompatButton
    private lateinit var mIBClose:AppCompatImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.view_common_text_popup)

        initUI()
        setCommonListener()

    }

    fun initUI() {
        mBtnConfirm = findViewById(R.id.BtnCommonTextPopupConfirm)
        mTVContent = findViewById(R.id.TVCommonTextPopupContent)

        val content = intent.getStringExtra(getString(R.string.intent_key_common_text_popup_content))
        mTVContent.text = content

        mIBClose = findViewById(R.id.IBCommonTextPopupClose)
    }

    fun setCommonListener() {
        mBtnConfirm.setOnClickListener {
            setResult(0)
            finish()
        }

        mIBClose.setOnClickListener {
            finish()
        }
    }
}

