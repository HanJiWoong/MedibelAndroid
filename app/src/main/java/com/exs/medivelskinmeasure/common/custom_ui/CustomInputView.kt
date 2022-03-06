package com.exs.medivelskinmeasure.common.custom_ui

import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.TextView
import androidx.appcompat.widget.AppCompatEditText
import androidx.appcompat.widget.AppCompatImageView
import androidx.constraintlayout.widget.ConstraintLayout
import com.exs.medivelskinmeasure.R

class CustomInputView : ConstraintLayout {

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

    private lateinit var mETEdit: AppCompatEditText
    private lateinit var mTVTitle: TextView


    var mStrTitle:String
        get() {
            return mTVTitle.text as String
        }
        set(value) {
            mTVTitle.text = value
        }

    var mStrHint:String
    get() {
        return mETEdit.hint as String
    }
    set(value) {
        mETEdit.hint = value
    }

    private fun init(context: Context) {
        val view = LayoutInflater.from(context)
            .inflate(R.layout.view_custom_input, this, false)
        addView(view)

        mETEdit = view.findViewById(R.id.ETCustomInputContent)
        mTVTitle = view.findViewById(R.id.TVCustonInputTitle)
    }


}