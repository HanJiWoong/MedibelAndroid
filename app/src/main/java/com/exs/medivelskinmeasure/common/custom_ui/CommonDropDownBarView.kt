package com.exs.medivelskinmeasure.common.custom_ui

import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.TextView
import androidx.appcompat.widget.AppCompatEditText
import androidx.constraintlayout.widget.ConstraintLayout
import com.exs.medivelskinmeasure.R

class CommonDropDownBarView : ConstraintLayout {

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

    private lateinit var mTVTitle: TextView
    private lateinit var mTVContent: TextView


    var mStrTitle:String
        get() {
            return mTVTitle.text as String
        }
        set(value) {
            mTVTitle.text = value
        }

    var mStrContent:String
        get() {
            return mTVContent.text as String
        }
        set(value) {
            mTVContent.text = value
        }

    private fun init(context: Context) {
        val view = LayoutInflater.from(context)
            .inflate(R.layout.view_common_drop_down, this, false)
        addView(view)

        mTVContent = view.findViewById(R.id.TVCommonDropDownContent)
        mTVTitle = view.findViewById(R.id.TVCommonDropDownTitle)
    }


}