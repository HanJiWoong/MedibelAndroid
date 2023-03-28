package com.exs.medivelskinmeasure.UI.MyPage

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatEditText
import com.exs.medivelskinmeasure.Connection.ConnectionService
import com.exs.medivelskinmeasure.Connection.dto.request.UserInfoUpdateRequestDTO
import com.exs.medivelskinmeasure.R
import com.exs.medivelskinmeasure.UI.Member.Join.JoinCompleteActivity
import com.exs.medivelskinmeasure.common.CommonUtil
import com.exs.medivelskinmeasure.common.custom_ui.CommonCheckBox
import com.exs.medivelskinmeasure.common.custom_ui.CommonTitleBar
import com.exs.medivelskinmeasure.common.popup.CommonTextPopup

class ModifyMemberInfoActivity : AppCompatActivity() {
    private lateinit var mTitleBar: CommonTitleBar

    private lateinit var mBtnConfirm: AppCompatButton

    private lateinit var mETName:AppCompatEditText
    private lateinit var mETOrganization:AppCompatEditText
    private lateinit var mETEmail:AppCompatEditText
    private lateinit var mETMobile:AppCompatEditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_modify_member_info)

        initUI()
        setCommonListener()
    }

    override fun onResume() {
        super.onResume()

        val token = CommonUtil.getPreferenceString(this, getString(R.string.pref_key_auto_login_token))
        token?.let {
            ConnectionService.getUserInfo(it, { result, data ->
                runOnUiThread {
                    if(result) {

                        mETName.setText(data?.data?.content?.memberName ?: "")
                        mETOrganization.setText(data?.data?.content?.company ?: "")
                        mETEmail.setText(data?.data?.content?.email ?: "")
                        mETMobile.setText(data?.data?.content?.mobile ?: "")

                    }
                }
            })
        }
    }

    private fun initUI() {
        mTitleBar = findViewById(R.id.ViewModifyMemberInfoTitleBar)
        mTitleBar.mStrTitle = getString(R.string.str_ko_my_page_member_info_modify)

        mBtnConfirm = findViewById(R.id.BtnModifyMemberInfo)

        mETName = findViewById(R.id.ETModifyMemberInfoName)
        mETName.setText("")

        mETOrganization = findViewById(R.id.ETModifyMemberInfoOrganization)
        mETOrganization.setText("")

        mETEmail = findViewById(R.id.ETModifyMemberInfoEmail)
        mETEmail.setText("")

        mETMobile = findViewById(R.id.ETModifyMemberInfoMobile)
        mETMobile.setText("")

    }

    private fun setCommonListener() {
        mTitleBar.setOnClickBackListener {
            finish()
        }

        mBtnConfirm.setOnClickListener {
            val token = CommonUtil.getPreferenceString(this, getString(R.string.pref_key_auto_login_token))
            token.let {
                val params = UserInfoUpdateRequestDTO(
                    token = it!!,
                    name = mETName.text.toString(),
                    company = mETOrganization.text.toString(),
                    email = mETEmail.text.toString(),
                    mobile = mETMobile.text.toString()
                )

                ConnectionService.updateUserInfo(params, { result, data ->
                    if(result) {
                        ConnectionService.getUserInfo(it, { result, data ->
                            runOnUiThread {
                                if(result) {

                                    mETName.setText(data?.data?.content?.memberName ?: "")
                                    mETOrganization.setText(data?.data?.content?.company ?: "")
                                    mETEmail.setText(data?.data?.content?.email ?: "")
                                    mETMobile.setText(data?.data?.content?.mobile ?: "")

                                }
                            }
                        })
                    } else {
                        Toast.makeText(this, getString(R.string.str_ko_server_error), Toast.LENGTH_SHORT).show()
                    }
                })
            }
        }
    }
}