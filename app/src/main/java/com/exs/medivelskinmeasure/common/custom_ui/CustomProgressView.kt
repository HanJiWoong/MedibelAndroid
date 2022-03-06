package com.exs.medivelskinmeasure.common.custom_ui

import android.content.Context
import android.content.res.TypedArray
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.TextView
import androidx.appcompat.widget.AppCompatEditText
import androidx.constraintlayout.widget.ConstraintLayout
import com.exs.medivelskinmeasure.R

class CustomProgressView  : ConstraintLayout {

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

    private lateinit var mTVTitle:TextView

    var mStrTitle:String
    get(){
        return mTVTitle.text.toString()
    }
    set(value) {
        mTVTitle.text = value
    }

    private lateinit var mCLProgressContent:ConstraintLayout


    private fun init(context: Context) {
        val view = LayoutInflater.from(context)
            .inflate(R.layout.view_custom_progress, this, false)
        addView(view)

        mCLProgressContent = findViewById(R.id.CLCustomProgressContent)

        val gd = GradientDrawable(
            GradientDrawable.Orientation.LEFT_RIGHT,
            intArrayOf(
                Color.parseColor("#5ec117"),
                Color.parseColor("#ebcc00")

            )
        )
        gd.cornerRadius = 20f

        mCLProgressContent.background = gd



    }


}