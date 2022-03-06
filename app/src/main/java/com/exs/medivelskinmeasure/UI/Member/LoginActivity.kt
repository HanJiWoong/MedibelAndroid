package com.exs.medivelskinmeasure.UI.Member

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import com.exs.medivelskinmeasure.R
import com.exs.medivelskinmeasure.UI.Main.MainActivity
import com.exs.medivelskinmeasure.UI.Member.Join.TermsActivity
import com.exs.medivelskinmeasure.common.custom_ui.CommonCheckBox
import org.w3c.dom.Text

class LoginActivity : AppCompatActivity() {

    private lateinit var mCBMaintain:CommonCheckBox

    private lateinit var mTVFindID:TextView
    private lateinit var mTVFindPW:TextView
    private lateinit var mTVJoin:TextView

    private lateinit var mBtnLogin:AppCompatButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        initUI()
        setCommonListener()
    }

    fun initUI() {
        mCBMaintain = findViewById(R.id.CBLoginMaintain)
        mCBMaintain.mStrTitle = getString(R.string.str_ko_login_state_maintain)

        mTVFindID = findViewById(R.id.TVLoginFindID)
        mTVFindPW = findViewById(R.id.TVLoginFindPW)
        mTVJoin = findViewById(R.id.TVLoginJoin)

        mBtnLogin = findViewById(R.id.BtnLogin)
    }

    fun setCommonListener() {
        mTVFindID.setOnClickListener {
            val intent = Intent(this@LoginActivity, FindIDActivity::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.anim_fade_in, R.anim.anim_do_not_move)
        }

        mTVFindPW.setOnClickListener {
            val intent = Intent(this@LoginActivity, FindPWActivity::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.anim_fade_in, R.anim.anim_do_not_move)
        }

        mTVJoin.setOnClickListener {
            val intent = Intent(this@LoginActivity, TermsActivity::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.anim_fade_in, R.anim.anim_do_not_move)
        }

        mBtnLogin.setOnClickListener {
            val intent = Intent(this@LoginActivity, MainActivity::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.anim_fade_in, R.anim.anim_do_not_move)
        }
    }
}
