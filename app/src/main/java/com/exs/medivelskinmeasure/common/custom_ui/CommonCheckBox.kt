package com.exs.medivelskinmeasure.common.custom_ui

import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageView
import androidx.constraintlayout.widget.ConstraintLayout
import com.exs.medivelskinmeasure.R

class CommonCheckBox : ConstraintLayout {

    interface CommonCheckBoxEvent {
        fun onClickCheckBox(isCheck: Boolean) {}
        fun onClickShowTerms() {}
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

    var OnCheckBoxEvent:CommonCheckBoxEvent? = null

    /**
     * UI
     */

    private lateinit var mIVIcon: AppCompatImageView
    private lateinit var mTVTitle: TextView


    var mStrTitle: String
        get() {
            return mTVTitle.text as String
        }
        set(value) {
            mTVTitle.text = value
        }

    var mIsCheck: Boolean
        get() {
            return mIVIcon.isSelected
        }
        set(value) {
            mIVIcon.isSelected = value
        }

    private fun init(context: Context) {
        val view = LayoutInflater.from(context)
            .inflate(R.layout.view_common_check_box, this, false)
        addView(view)

        mIVIcon = view.findViewById(R.id.IVCommonCheckBoxIcon)
        mTVTitle = view.findViewById(R.id.TVCommonCheckBoxTitle)

        mIVIcon.setOnClickListener {
            OnCheckBoxEvent?.let {
                mIsCheck = !mIsCheck
                it.onClickCheckBox(mIsCheck)
            }
        }

        mTVTitle.setOnClickListener {
            OnCheckBoxEvent?.let {
                it.onClickShowTerms()
            }
        }
    }


}