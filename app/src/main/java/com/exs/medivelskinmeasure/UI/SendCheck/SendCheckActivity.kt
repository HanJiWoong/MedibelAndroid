package com.exs.medivelskinmeasure.UI.SendCheck

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.exs.medivelskinmeasure.Constants
import com.exs.medivelskinmeasure.MeasureMode
import com.exs.medivelskinmeasure.R
import com.exs.medivelskinmeasure.UI.CustomerInfo.*
import com.exs.medivelskinmeasure.UI.Member.Join.JoinActivity
import com.exs.medivelskinmeasure.UI.Member.Join.TermDetailActivity
import com.exs.medivelskinmeasure.common.custom_ui.CommonCheckBox
import com.exs.medivelskinmeasure.common.custom_ui.CommonTitleBar
import com.exs.medivelskinmeasure.common.popup.ListAdapter

class SendCheckActivity : AppCompatActivity() {
    private lateinit var mTitleBar: CommonTitleBar

    private lateinit var mRVList: RecyclerView
    private var mListAdapter: SendCheckListAdapter? = null

    private lateinit var mBtnDelete: AppCompatButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_send_check)

        initUI()
        setCommonListener()
    }

    private fun initUI() {
        mTitleBar = findViewById(R.id.ViewSendCheckTitleBar)
        mTitleBar.mStrTitle = getString(R.string.str_ko_send_check_title)

        mRVList = findViewById(R.id.RVSendCheck)
        mRVList.layoutManager = LinearLayoutManager(this)

        mListAdapter = SendCheckListAdapter()
        mListAdapter!!.setList(onGenerationData())
        mListAdapter!!.setOnItemClickListener(object : SendCheckListAdapter.OnItemClickListener {
            override fun onItemClick(v: View, ssid: String, pos: Int) {
                if(Constants.measureMode == MeasureMode.Hospital) {
                    val intent = Intent(this@SendCheckActivity, HospitalCustomerInfoActivity::class.java)
                    startActivity(intent)
                    overridePendingTransition(R.anim.anim_fade_in, R.anim.anim_do_not_move)
                } else if(Constants.measureMode == MeasureMode.BeautyShop) {
                    val intent = Intent(this@SendCheckActivity, BeautyShopCustomerInfoActivity::class.java)
                    startActivity(intent)
                    overridePendingTransition(R.anim.anim_fade_in, R.anim.anim_do_not_move)
                } else if(Constants.measureMode == MeasureMode.HumanClinical) {
                    val intent = Intent(this@SendCheckActivity, HumanClinicalInfoActivity::class.java)
                    startActivity(intent)
                    overridePendingTransition(R.anim.anim_fade_in, R.anim.anim_do_not_move)
                } else if(Constants.measureMode == MeasureMode.AnimalClinical) {
                    val intent = Intent(this@SendCheckActivity, AnimalClinicalInfoActivity::class.java)
                    startActivity(intent)
                    overridePendingTransition(R.anim.anim_fade_in, R.anim.anim_do_not_move)
                } else if(Constants.measureMode == MeasureMode.AnimalHospital) {
                    val intent = Intent(this@SendCheckActivity, AnimalHospitalCustomerInfoActivity::class.java)
                    startActivity(intent)
                    overridePendingTransition(R.anim.anim_fade_in, R.anim.anim_do_not_move)
                }
            }

        })


        mRVList.adapter = mListAdapter


        mBtnDelete = findViewById(R.id.BtnSendCheckDelete)
    }

    private fun setCommonListener() {
        mTitleBar.setOnClickBackListener {
            finish()
        }
    }


    fun onGenerationData(): List<String> {
        var data = mutableListOf<String>()

        data = mutableListOf<String>(
            "2021-02-22 09:00:00",
            "2021-02-22 10:00:00",
            "2021-02-21 11:00:00",
            "2021-02-21 15:00:00",
            "2021-02-21 17:00:00",
            "2021-02-20 05:00:00",
            "2021-02-20 09:00:00",
            "2021-02-19 14:00:00",
            "2021-02-18 19:00:00"
        )


        return data
    }
}

class SendCheckListAdapter : RecyclerView.Adapter<SendCheckListAdapter.SendCheckListViewHolder>() {

    private var mArrListData: List<String>? = null
    private var mClickListener: ((Int, String) -> Unit)? = null
    private var mIntSelect: Int = -1

    interface OnItemClickListener {
        fun onItemClick(v: View, ssid: String, pos: Int)
    }

    private var listener: OnItemClickListener? = null

    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.listener = listener
    }

    fun setList(list: List<String>) {
        mArrListData = list
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SendCheckListViewHolder {
        val view: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_send_check, parent, false)
        return SendCheckListViewHolder(view)
    }

    override fun onBindViewHolder(holder: SendCheckListViewHolder, position: Int) {
        mArrListData?.get(position)?.let { result ->
            holder.bind(result)
        }
    }

    override fun getItemCount(): Int {
        if (mArrListData != null) {
            return mArrListData!!.size
        } else {
            return 0
        }
    }

    inner class SendCheckListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var mTVData: TextView
        var mBtnItem: AppCompatButton

        init {
            mTVData = itemView.findViewById(R.id.TVSendCheckMeasureTime)
            mBtnItem = itemView.findViewById(R.id.BtnSendCheck)
        }

        fun bind(result: String) {
            mTVData.text = result

            val pos = adapterPosition
            if (pos != RecyclerView.NO_POSITION) {
                mBtnItem.setOnClickListener {
                    listener?.onItemClick(itemView, mTVData.text.toString(), pos)

                    mIntSelect = pos
                    notifyDataSetChanged()
                }
            }

        }

    }
}
