package com.exs.medivelskinmeasure.common.custom_ui

import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageButton
import androidx.appcompat.widget.AppCompatImageView
import androidx.constraintlayout.widget.ConstraintLayout
import com.exs.medivelskinmeasure.R

class CommonTitleBar : ConstraintLayout {

    private fun getAttrs(attrs: AttributeSet?) {
    }

    private fun getAttrs(attrs: AttributeSet?, defStyle: Int) {
    }

    //디폴트 설정
    private fun setTypeArray(typedArray: TypedArray) {
    }

    constructor(context: Context) : super(context) {
        init(context)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(context)
        getAttrs(attrs)
    }

    /**
     * UI
     */

    private lateinit var mIBBack: AppCompatImageButton
    private lateinit var mIBClose: AppCompatImageButton
    private lateinit var mTVTitle: TextView


    var mStrTitle: String
        get() {
            return mTVTitle.text as String
        }
        set(value) {
            mTVTitle.text = value
        }

    private fun init(context: Context) {
        val view = LayoutInflater.from(context)
            .inflate(R.layout.view_common_title_bar, this, false)
        addView(view)

        mIBBack = view.findViewById(R.id.BtnCommonTitleBarBack)
        mTVTitle = view.findViewById(R.id.TVCommonTitleBarTitle)
        mIBClose = view.findViewById(R.id.BtnCommonTitleBarClose)
    }

    fun setOnClickBackListener(listener:View.OnClickListener) {
        mIBBack.setOnClickListener(listener)
        mIBClose.setOnClickListener(listener)
    }

    fun setHiddenBackBtn(isHidden:Boolean) {
        if (isHidden) {
            mIBBack.visibility = View.GONE
        } else {
            mIBBack.visibility = View.VISIBLE
        }
    }

    fun setCloseMode(isSet:Boolean) {
        if(isSet) {
            mIBBack.visibility = View.GONE
            mIBClose.visibility = View.VISIBLE
        }
    }

}