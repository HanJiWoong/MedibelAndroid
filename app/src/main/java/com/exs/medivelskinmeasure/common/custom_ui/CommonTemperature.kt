package com.exs.medivelskinmeasure.common.custom_ui

import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.exs.medivelskinmeasure.R

class CommonTemperature : ConstraintLayout {

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

    private lateinit var mTVTemperature: TextView


    var mStrTemperature:String
        get() {
            return mTVTemperature.text as String
        }
        set(value) {
            mTVTemperature.text = value
        }


    private fun init(context: Context) {
        val view = LayoutInflater.from(context)
            .inflate(R.layout.view_common_temperature, this, false)
        addView(view)

        mTVTemperature = view.findViewById(R.id.TVTemperatureValue)
    }


}