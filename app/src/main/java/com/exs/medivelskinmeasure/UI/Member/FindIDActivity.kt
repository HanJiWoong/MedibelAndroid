package com.exs.medivelskinmeasure.UI.Member

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatEditText
import androidx.constraintlayout.widget.ConstraintLayout
import com.exs.medivelskinmeasure.Connection.ConnectionService
import com.exs.medivelskinmeasure.Connection.dto.request.FindIDRequestDTO
import com.exs.medivelskinmeasure.Connection.dto.request.SignupRequestDTO
import com.exs.medivelskinmeasure.R
import com.exs.medivelskinmeasure.UI.Member.Join.JoinCompleteActivity
import com.exs.medivelskinmeasure.common.custom_ui.CommonTitleBar
import com.exs.medivelskinmeasure.common.custom_ui.CommonTwoTabBar
import com.exs.medivelskinmeasure.common.custom_ui.TwoTabState

class FindIDActivity : AppCompatActivity() {


    private lateinit var mTitleBar: CommonTitleBar
    private lateinit var mTabBar: CommonTwoTabBar

    private lateinit var mETName: AppCompatEditText
    private lateinit var mETContent: AppCompatEditText

    private lateinit var mBtnFind: AppCompatButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_find_id)

        initUI()
        setCommonListener()
    }

    private fun initUI() {
        mTitleBar = findViewById(R.id.ViewFindIdTitleBar)
        mTitleBar.mStrTitle = getString(R.string.str_ko_id_find_title)

        mTabBar = findViewById(R.id.ViewFindIdTabBar)
        mTabBar.setText(
            getString(R.string.str_ko_find_tab_first),
            getString(R.string.str_ko_find_tab_second)
        )
        mTabBar.select(TwoTabState.First)

        mETName = findViewById(R.id.ETFindIdName)
        mETContent = findViewById(R.id.ETFindIdInputContent)

        mBtnFind = findViewById(R.id.BtnFindID)
    }

    private fun setCommonListener() {
        mTitleBar.setOnClickBackListener {
            finish()
        }

        mTabBar.tabBarListener = object : CommonTwoTabBar.CommonTwoTabBarListener {
            override fun selectFirst() {
                mETContent.setHint(getString(R.string.str_ko_find_input_email))
            }

            override fun selectSecond() {
                mETContent.setHint(getString(R.string.str_ko_find_input_mobile))
            }
        }

        mBtnFind.setOnClickListener {
            if (mETContent.text?.isEmpty() == false) {
                val findIDParams = FindIDRequestDTO(
                    memberName = mETName.text.toString(),
                    memberEmail = null,
                    memberMobile = null
                )

                if (mTabBar.currentState() == TwoTabState.First) {
                    findIDParams.memberEmail = mETContent.text.toString()
                } else {
                    findIDParams.memberMobile = mETContent.text.toString()
                }

                ConnectionService.findID(findIDParams, { result, data ->
                    runOnUiThread {
                        if (result) {
                            data?.data?.let {
                                val memberData = it.content

                                val intent = Intent(this@FindIDActivity, FindIDResultActivity::class.java)
                                intent.putExtra(getString(R.string.intent_key_find_id_name),memberData.memberName)
                                intent.putExtra(getString(R.string.intent_key_find_id_id),memberData.memberId)
                                startActivity(intent)
                                overridePendingTransition(R.anim.anim_fade_in, R.anim.anim_do_not_move)

                                finish()
//                                val toast = Toast.makeText(
//                                    this,
//                                    "아이디 ${memberData.memberId}가 확인되었습니다.",
//                                    Toast.LENGTH_LONG
//                                )
//                                toast.addCallback(object : Toast.Callback() {
//                                    override fun onToastHidden() {
//                                        super.onToastHidden()
//                                        finish()
//                                    }
//                                })
//                                toast.show()
                            }
                        } else {
                            Toast.makeText(
                                this,
                                getString(R.string.str_ko_wrong_input),
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                })

            } else {
                Toast.makeText(this, getString(R.string.str_ko_wrong_input), Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }
}