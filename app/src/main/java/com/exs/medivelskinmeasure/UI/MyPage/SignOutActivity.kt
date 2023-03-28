package com.exs.medivelskinmeasure.UI.MyPage

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import com.exs.medivelskinmeasure.Connection.ConnectionService
import com.exs.medivelskinmeasure.R
import com.exs.medivelskinmeasure.common.CommonUtil
import com.exs.medivelskinmeasure.common.custom_ui.CommonTitleBar

class SignOutActivity : AppCompatActivity() {
    private lateinit var mTitleBar: CommonTitleBar

    private lateinit var mBtnConfirm: AppCompatButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_out)

        initUI()
        setCommonListener()
    }

    private fun initUI() {
        mTitleBar = findViewById(R.id.ViewSignOutTitleBar)
        mTitleBar.mStrTitle = getString(R.string.str_ko_my_page_sign_out)

    }

    private fun setCommonListener() {
        mTitleBar.setOnClickBackListener {
            finish()
        }

        mBtnConfirm.setOnClickListener {
            val token = CommonUtil.getPreferenceString(
                this,
                getString(R.string.pref_key_auto_login_token)
            )
            token?.let {
                ConnectionService.withDrawal(token, { result, data ->
                    if(result) {
                        CommonUtil.setPreferenceString(this@SignOutActivity, getString(R.string.pref_key_is_auto_login), "false")
                        CommonUtil.setPreferenceString(this@SignOutActivity, getString(R.string.pref_key_auto_login_token), "")

                        setResult(LogoutRequestCode)
                        finish()

                    } else {
                        Toast.makeText(this, "회원 탈퇴에 실패하였습니다.", Toast.LENGTH_SHORT).show()
                    }
                })
            }
        }
    }
}
