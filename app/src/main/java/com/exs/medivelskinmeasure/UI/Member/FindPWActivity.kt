package com.exs.medivelskinmeasure.UI.Member

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatEditText
import com.exs.medivelskinmeasure.Connection.ConnectionService
import com.exs.medivelskinmeasure.Connection.dto.request.FindPWRequestDTO
import com.exs.medivelskinmeasure.R
import com.exs.medivelskinmeasure.common.custom_ui.CommonTitleBar
import com.exs.medivelskinmeasure.common.custom_ui.CommonTwoTabBar
import com.exs.medivelskinmeasure.common.custom_ui.TwoTabState

class FindPWActivity : AppCompatActivity() {

    private lateinit var mTitleBar: CommonTitleBar
    private lateinit var mTabBar: CommonTwoTabBar

    private lateinit var mETId: AppCompatEditText
    private lateinit var mETData: AppCompatEditText

    private lateinit var mBtnFind: AppCompatButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_find_pw)

        initUI()
        setCommonListener()
    }

    private fun initUI() {
        mTitleBar = findViewById(R.id.ViewFindPWTitleBar)
        mTitleBar.mStrTitle = getString(R.string.str_ko_pw_find_title)

        mTabBar = findViewById(R.id.ViewFindPWTabBar)
        mTabBar.setText(getString(R.string.str_ko_find_tab_first),getString(R.string.str_ko_find_tab_second))
        mTabBar.select(TwoTabState.First)

        mETId = findViewById(R.id.ETFindPWID)
        mETData = findViewById(R.id.ETFindPWData)

        mBtnFind = findViewById(R.id.BtnFindPW)
    }

    private fun setCommonListener() {
        mTitleBar.setOnClickBackListener {
            finish()
        }

        mTabBar.tabBarListener = object : CommonTwoTabBar.CommonTwoTabBarListener {
            override fun selectFirst() {
                mETData.setHint(getString(R.string.str_ko_find_input_email))
            }

            override fun selectSecond() {
                mETData.setHint(getString(R.string.str_ko_find_input_mobile))
            }
        }

        mBtnFind.setOnClickListener {
            if (mETData.text?.isEmpty() == false && mETId.text?.isEmpty() == false) {
                val findPWParams = FindPWRequestDTO(
                    memberId = mETId.text.toString(),
                    memberEmail = null,
                    memberMobile = null
                )

                if (mTabBar.currentState() == TwoTabState.First) {
                    findPWParams.memberEmail = mETData.text.toString()
                } else {
                    findPWParams.memberMobile = mETData.text.toString()
                }

                ConnectionService.findPW(findPWParams, { result, data ->
                    runOnUiThread {
                        if(result) {
                            data?.data?.let {
                                val memberData = it.content

                                val toast = Toast.makeText(
                                    this,
                                    "임시 비밀번호가 전달되었습니다. 메일을 확인해주세요",
                                    Toast.LENGTH_LONG
                                )
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                                    toast.addCallback(object : Toast.Callback() {
                                        override fun onToastHidden() {
                                            super.onToastHidden()
                                            finish()
                                        }
                                    })
                                }
                                toast.show()
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
                Toast.makeText(
                    this,
                    getString(R.string.str_ko_wrong_input),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
}