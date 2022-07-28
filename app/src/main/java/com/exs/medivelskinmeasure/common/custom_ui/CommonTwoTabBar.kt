package com.exs.medivelskinmeasure.common.custom_ui

import android.content.Context
import android.content.res.TypedArray
import android.graphics.Color
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageView
import androidx.constraintlayout.widget.ConstraintLayout
import com.exs.medivelskinmeasure.R

enum class TwoTabState {
    First,
    Second
}

class CommonTwoTabBar  : ConstraintLayout {

    interface CommonTwoTabBarListener {
        fun selectFirst() { }
        fun selectSecond() { }
    }

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

    private lateinit var mTVFirst:TextView
    private lateinit var mTVSecond:TextView


    private fun init(context: Context) {
        val view = LayoutInflater.from(context)
            .inflate(R.layout.view_common_tow_tab_bar, this, false)
        addView(view)

        mTVFirst = view.findViewById(R.id.TVCommonTwoTabBarFirst)
        mTVSecond = view.findViewById(R.id.TVCommonTwoTabBarSecond)

        setListener()
    }

    fun setText(first:String, second:String) {
        mTVFirst.text = first
        mTVSecond.text = second
    }

    fun setListener() {
        mTVFirst.setOnClickListener {
            if(mTVFirst.isSelected == false) {
                select(TwoTabState.First)
            }
        }

        mTVSecond.setOnClickListener {
            if(mTVSecond.isSelected == false) {
                select(TwoTabState.Second)
            }
        }
    }

    fun select(pos:TwoTabState) {
        if (pos == TwoTabState.First) {
            mTVFirst.isSelected = true
            mTVSecond.isSelected = false

            mTVFirst.setTextColor(Color.WHITE)
            mTVSecond.setTextColor(context.getColor(R.color.text_3))
        } else {
            mTVFirst.isSelected = false
            mTVSecond.isSelected = true

            mTVFirst.setTextColor(context.getColor(R.color.text_3))
            mTVSecond.setTextColor(Color.WHITE)
        }
    }


}