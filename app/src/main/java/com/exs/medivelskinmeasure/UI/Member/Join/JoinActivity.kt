package com.exs.medivelskinmeasure.UI.Member.Join

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract
import android.util.Log
import android.view.KeyEvent
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatEditText
import androidx.constraintlayout.widget.ConstraintLayout
import com.exs.medivelskinmeasure.Connection.ConnectionService
import com.exs.medivelskinmeasure.Connection.dto.request.SignupRequestDTO
import com.exs.medivelskinmeasure.R
import com.exs.medivelskinmeasure.common.custom_ui.CommonCheckBox
import com.exs.medivelskinmeasure.common.custom_ui.CommonTitleBar
import com.exs.medivelskinmeasure.common.popup.CommonTextPopup

class JoinActivity : AppCompatActivity() {
    private val TAG: String = "JoinActivity"

    private lateinit var mTitleBar: CommonTitleBar

    private lateinit var mETName: AppCompatEditText
    private lateinit var mETOrganization: AppCompatEditText
    private lateinit var mETID: AppCompatEditText
    private lateinit var mETPW: AppCompatEditText
    private lateinit var mETPWConfirm: AppCompatEditText
    private lateinit var mETEmail: AppCompatEditText
    private lateinit var mETMobile: AppCompatEditText


    private lateinit var mBtnJoin: AppCompatButton

    private var mIsShowOrganizationNotice: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_join)

        initUI()
        setCommonListener()
    }

    private fun initUI() {
        mTitleBar = findViewById(R.id.ViewJoinTitleBar)
        mTitleBar.mStrTitle = getString(R.string.str_ko_join_info_title)

        mETName = findViewById(R.id.ETJoinInfoName)
        mETOrganization = findViewById(R.id.ETJoinInfoOrganization)
        mETID = findViewById(R.id.ETJoinInfoID)
        mETPW = findViewById(R.id.ETJoinInfoPW)
        mETPWConfirm = findViewById(R.id.ETJoinInfoPWMatch)
        mETEmail = findViewById(R.id.ETJoinInfoEmail)
        mETMobile = findViewById(R.id.ETJoinInfoMobile)

        mBtnJoin = findViewById(R.id.BtnJoin)
    }

    private fun setCommonListener() {
        mTitleBar.setOnClickBackListener {
            finish()
        }

        mETOrganization.setOnFocusChangeListener { view, hasFocus ->
            if (hasFocus && !mIsShowOrganizationNotice) {
                mIsShowOrganizationNotice = true

                val intent = Intent(this@JoinActivity, CommonTextPopup::class.java)
                intent.putExtra(
                    getString(R.string.intent_key_common_text_popup_content),
                    getString(R.string.str_ko_Join_organization_notice_content)
                )
                startActivity(intent)
                overridePendingTransition(R.anim.anim_fade_in, R.anim.anim_do_not_move)
            }
        }

        mBtnJoin.setOnClickListener {

            if (!mETName.text!!.isEmpty() &&
                !mETOrganization.text!!.isEmpty() &&
                !mETID.text!!.isEmpty() &&
                !mETPW.text!!.isEmpty() &&
                !mETPWConfirm.text!!.isEmpty() &&
                !mETEmail.text!!.isEmpty() &&
                !mETMobile.text!!.isEmpty()
            ) {

                if (mETPW.text!!.equals(mETPWConfirm.text)) {
                    Toast.makeText(
                        this,
                        getString(R.string.str_ko_join_info_not_match),
                        Toast.LENGTH_SHORT
                    ).show()
                } else {

                    val signupParams = SignupRequestDTO(
                        userId = mETID.text.toString(),
                        password = mETPW.text.toString(),
                        userName = mETName.text.toString(),
                        company = mETOrganization.text.toString(),
                        email = mETEmail.text.toString(),
                        mobile = mETMobile.text.toString()
                    )

                    ConnectionService.signUp(signupParams,{ result, data ->
                        runOnUiThread {
                            if(result) {

                                finish()

                                val intent = Intent(this@JoinActivity, JoinCompleteActivity::class.java)
                                startActivity(intent)
                                overridePendingTransition(R.anim.anim_fade_in, R.anim.anim_do_not_move)
                            } else {
                                Toast.makeText(this, getString(R.string.str_ko_join_info_empty), Toast.LENGTH_SHORT)
                                    .show()
                            }
                        }
                    })


                }
            } else {
                Toast.makeText(this, getString(R.string.str_ko_join_info_empty), Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }


}