package com.exs.medivelskinmeasure.common.popup

import android.net.wifi.ScanResult
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatImageButton
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.exs.medivelskinmeasure.R

class CommonListPopup : AppCompatActivity() {

    private lateinit var mTVTitle: TextView
    private lateinit var mRVList: RecyclerView
    private var mListAdapter: ListAdapter? = null
    private lateinit var mBtnClose: AppCompatImageButton

    private var mVarListType: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_common_list_popup)

        mVarListType = intent.getStringExtra(getString(R.string.intent_key_common_List_popup_type))

        initUI()
        setCommonListener()

    }

    fun initUI() {


        mTVTitle = findViewById(R.id.TVCommonListPopupTitle)
        mTVTitle.text = mVarListType

        mBtnClose = findViewById(R.id.IBCommonListtPopupClose)

        mRVList = findViewById(R.id.RVCommonListPopup)
        mRVList.layoutManager = LinearLayoutManager(this)

        mListAdapter = ListAdapter()
        mListAdapter!!.setList(onGenerationData())

        mRVList.adapter = mListAdapter




    }

    fun setCommonListener() {
        mBtnClose.setOnClickListener {
            finish()
        }
    }

    fun onGenerationData(): List<String> {
        var data = mutableListOf<String>()

        if (mVarListType == getString(R.string.intent_data_common_list_popup_type_birth)) {
            for(i in 1900..2020) {
                data.add("${i}")
            }
        } else if (mVarListType == getString(R.string.intent_data_common_list_popup_type_gender)) {
            data = mutableListOf<String>("남","여","모름")
        } else if (mVarListType == getString(R.string.intent_data_common_list_popup_type_region)) {
            data = mutableListOf<String>("얼굴","몸")

        } else if (mVarListType == getString(R.string.intent_data_common_list_popup_type_detail_region)) {
            data = mutableListOf<String>("두피","T존","왼볼","오른볼")
        } else if (mVarListType == getString(R.string.intent_data_common_list_popup_type_classification_of_disease)) {
            data = mutableListOf<String>("Hypersensitivity(allergic) dermatoses","Immune-mediated dermatoses","Autoimmune dermatoses")
        } else if (mVarListType == getString(R.string.intent_data_common_list_popup_type_diagnostic_name)) {
            data = mutableListOf<String>("Sarcoptic mange            ","Notoedric mange ( Celine scabies) ","Cheyletiella dermatitis ")
        }


        return data
    }
}

class ListAdapter : RecyclerView.Adapter<ListAdapter.ViewHolder>() {

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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_common_list_popup, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
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

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var mTVData: TextView
        var mBtnItem: AppCompatButton

        init {
            mTVData = itemView.findViewById(R.id.TVCommonListPopupContent)
            mBtnItem = itemView.findViewById(R.id.BtnCommonListPopupContent)
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