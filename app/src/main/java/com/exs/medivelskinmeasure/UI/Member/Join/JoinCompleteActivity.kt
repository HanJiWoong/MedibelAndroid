package com.exs.medivelskinmeasure.UI.Member.Join

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.widget.AppCompatButton
import com.exs.medivelskinmeasure.UI.Main.MainActivity
import com.exs.medivelskinmeasure.R
import com.exs.medivelskinmeasure.common.custom_ui.CommonTitleBar

class JoinCompleteActivity : AppCompatActivity() {
    private lateinit var mTitleBar: CommonTitleBar

    private lateinit var mBtnJoinComplete: AppCompatButton


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_join_complete)

        initUI()
        setCommonListener()
    }

    private fun initUI() {
        mTitleBar = findViewById(R.id.ViewJoinCompleteTitleBar)
        mTitleBar.mStrTitle = getString(R.string.str_ko_join_complete_title)
        mTitleBar.setHiddenBackBtn(true)

        mBtnJoinComplete = findViewById(R.id.BtnJoinConpleteConfirm)

    }

    private fun setCommonListener() {
        mTitleBar.setOnClickBackListener {
            finish()
        }

        mBtnJoinComplete.setOnClickListener {
            val intent = Intent(this@JoinCompleteActivity, MainActivity::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.anim_fade_in, R.anim.anim_do_not_move)
        }
    }
}