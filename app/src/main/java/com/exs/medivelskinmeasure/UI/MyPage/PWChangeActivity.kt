package com.exs.medivelskinmeasure.UI.MyPage

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatEditText
import com.exs.medivelskinmeasure.Connection.ConnectionService
import com.exs.medivelskinmeasure.Connection.dto.request.ChangePWRequestDTO
import com.exs.medivelskinmeasure.R
import com.exs.medivelskinmeasure.common.CommonUtil
import com.exs.medivelskinmeasure.common.custom_ui.CommonTitleBar

class PWChangeActivity : AppCompatActivity() {
    private lateinit var mTitleBar: CommonTitleBar

    private lateinit var mETChangePW: AppCompatEditText
    private lateinit var mETConfimPW: AppCompatEditText

    private lateinit var mBtnConfirm: AppCompatButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pwchange)

        initUI()
        setCommonListener()
    }

    private fun initUI() {
        mTitleBar = findViewById(R.id.ViewPWChangeTitleBar)
        mTitleBar.mStrTitle = getString(R.string.str_ko_my_page_pw_change)

        mETChangePW = findViewById(R.id.ETPWChangeNewPW)
        mETConfimPW = findViewById(R.id.ETPWChangeNewPWMatch)

        mBtnConfirm = findViewById(R.id.BtnPWChangeConfirm)
    }

    private fun setCommonListener() {
        mTitleBar.setOnClickBackListener {
            finish()
        }

        mBtnConfirm.setOnClickListener {
            val new = mETChangePW.text.toString()
            val match = mETChangePW.text.toString()

            if (new.isEmpty() || match.isEmpty()) {
                Toast.makeText(this, "변경하실 비빌번호를 입력해주세요", Toast.LENGTH_SHORT).show()
            } else if (!new.equals(match)) {
                Toast.makeText(this, "입력한 비밀번호가 맞지 않습니다", Toast.LENGTH_SHORT).show()
            } else {
                val token = CommonUtil.getPreferenceString(
                    this,
                    getString(R.string.pref_key_auto_login_token)
                )
                token?.let {
                    val params = ChangePWRequestDTO(
                        token = it,
                        password = new
                    )

                    ConnectionService.changePW(params, { result, data ->
                        runOnUiThread {
                            if (result) {
                                val token = data?.data?.content?.token

                                CommonUtil.setPreferenceString(
                                    this@PWChangeActivity,
                                    getString(R.string.pref_key_auto_login_token),
                                    data?.data?.content?.token ?: ""
                                )

                                val toast = Toast.makeText(
                                    this,
                                    "비밀번호가 변경되었습니다.",
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
                            } else {
                                Toast.makeText(this, "비밀번호 변경에 실패하였습니다.", Toast.LENGTH_SHORT).show()
                            }
                        }
                    })
                }

            }
        }
    }
}