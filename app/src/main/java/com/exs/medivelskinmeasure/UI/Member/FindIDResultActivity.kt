package com.exs.medivelskinmeasure.UI.Member

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import com.exs.medivelskinmeasure.R
import com.exs.medivelskinmeasure.common.custom_ui.CommonTitleBar

class FindIDResultActivity : AppCompatActivity() {

    private lateinit var mTitleBar: CommonTitleBar
    private lateinit var mTVResultName: TextView
    private lateinit var mTVResultId: TextView

    private lateinit var mBtnConfirm: AppCompatButton


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_find_id_result)

        mTitleBar = findViewById(R.id.ViewFindIdResultTitleBar)
        mTVResultName = findViewById(R.id.TVFindIdResultName)
        mTVResultId = findViewById(R.id.TVFindIdResultId)
        mBtnConfirm = findViewById(R.id.BtnFindIdResultConfirm)

        mTitleBar.setHiddenBackBtn(true)
        mTitleBar.mStrTitle = getString(R.string.str_ko_id_find_result_title)

        val memberName = intent.getStringExtra(getString(R.string.intent_key_find_id_name))
        val memberID = intent.getStringExtra(getString(R.string.intent_key_find_id_id))

        mTVResultName.text = memberName
        mTVResultId.text = memberID

        mBtnConfirm.setOnClickListener {
            finish()
        }

    }

}