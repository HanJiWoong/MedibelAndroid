package com.exs.medivelskinmeasure.UI.Main.Component

import android.content.Context
import android.content.Intent
import android.content.res.TypedArray
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatImageButton
import androidx.constraintlayout.widget.ConstraintLayout
import com.exs.medivelskinmeasure.R
import com.exs.medivelskinmeasure.UI.Main.ConnectStateActivity
import com.exs.medivelskinmeasure.UI.Member.Join.TermDetailActivity
import com.exs.medivelskinmeasure.UI.MyPage.MyPageActivity

class CommonMainTop : ConstraintLayout {

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

    private lateinit var mContext:Context

    private lateinit var mIBLogo:AppCompatImageButton
    private lateinit var mIBConnectState:AppCompatImageButton
    private lateinit var mIBBattery:AppCompatImageButton
    private lateinit var mIBMyPage:AppCompatImageButton


    private fun init(context: Context) {
        val view = LayoutInflater.from(context)
            .inflate(R.layout.view_common_main_top, this, false)
        addView(view)

        mContext = context

        initUI()

        setCommonListener()
    }

    fun initUI() {
        mIBLogo = findViewById(R.id.IBMainTopLogo)
        mIBConnectState = findViewById(R.id.IBMainTopConnectState)
        mIBBattery = findViewById(R.id.IBMainTopBatteryState)
        mIBMyPage = findViewById(R.id.IBMainTopMyPage)
    }

    fun setCommonListener() {
        mIBLogo.setOnClickListener {
            (mContext as AppCompatActivity).finish()
        }

        mIBConnectState.setOnClickListener {
            val intent = Intent(mContext, ConnectStateActivity::class.java)
            (mContext as AppCompatActivity).startActivity(intent)
            (mContext as AppCompatActivity).overridePendingTransition(R.anim.anim_fade_in, R.anim.anim_do_not_move)
        }

        mIBMyPage.setOnClickListener {
            val intent = Intent(mContext, MyPageActivity::class.java)
            (mContext as AppCompatActivity).startActivity(intent)
            (mContext as AppCompatActivity).overridePendingTransition(R.anim.anim_fade_in, R.anim.anim_do_not_move)
        }

    }

    fun setLogoButtonEnabled(isEnable:Boolean) {
        mIBLogo.isEnabled = isEnable
    }

    fun setBatteryState(isFull:Boolean) {

        if(isFull) {
            mIBBattery.background = mContext.getDrawable(R.drawable.ic_main_top_battery_full)
        } else {
            mIBBattery.background = mContext.getDrawable(R.drawable.ic_main_top_battery_low)
        }
    }
}