package com.exs.medivelskinmeasure.UI.Main

import android.content.Intent
import android.net.wifi.ScanResult
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatImageButton
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.exs.medivelskinmeasure.Device.BluetoothObject
import com.exs.medivelskinmeasure.R
import com.exs.medivelskinmeasure.UI.Member.Join.JoinActivity
import com.exs.medivelskinmeasure.UI.Member.Join.TermDetailActivity
import com.exs.medivelskinmeasure.common.custom_ui.CommonCheckBox
import com.exs.medivelskinmeasure.common.custom_ui.CommonTitleBar
import org.w3c.dom.Text

class ConnectStateActivity : AppCompatActivity() {
    private val REQUEST_BT_CONNECTION = 100

    private lateinit var mTitleBar: CommonTitleBar

    private lateinit var mRVList: RecyclerView
    private lateinit var mListAdapter: ConnectStateAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_connect_state)


        if(BluetoothObject.checkBtState() == false) {
            BluetoothObject.requestBtEnable(this,REQUEST_BT_CONNECTION)
        }

        initUI()
        setCommonListener()
    }


    private fun initUI() {
        mTitleBar = findViewById(R.id.ViewConnectStateTitleBar)
        mTitleBar.mStrTitle = getString(R.string.str_ko_connect_state_title)
        mTitleBar.setCloseMode(true)

        mRVList = findViewById(R.id.RVConnectState)
        mRVList.layoutManager = LinearLayoutManager(this)

        var list = ArrayList<String>()
        list.add("tempDevice000001")
        list.add("tempDevice000002")
        list.add("tempDevice000003")
        list.add("tempDevice000004")
        list.add("tempDevice000005")
        list.add("tempDevice000006")

        mListAdapter = ConnectStateAdapter()
        mListAdapter.setDeviceList(list)
        mRVList.adapter = mListAdapter
        mListAdapter.notifyDataSetChanged()
    }

    private fun setCommonListener() {
        mTitleBar.setOnClickBackListener {
            finish()
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == REQUEST_BT_CONNECTION) {
            if(resultCode == RESULT_OK) {
                Toast.makeText(this, "블루투스가 활성화 되었습니다.",Toast.LENGTH_SHORT)
            } else if(resultCode == RESULT_CANCELED) {
                Toast.makeText(this, "블루투스가 비활성화 되었습니다.",Toast.LENGTH_SHORT)

                finish()
            }
        }

    }
}

class ConnectStateAdapter : RecyclerView.Adapter<ConnectStateAdapter.ConnectStateViewHolder>() {
    private var mArrListDevice: ArrayList<String> = ArrayList()


    fun setDeviceList(list: ArrayList<String>) {
        mArrListDevice = list
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ConnectStateViewHolder {
        val view: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_connect_state, parent, false)
        return ConnectStateViewHolder(view)
    }

    override fun onBindViewHolder(holder: ConnectStateViewHolder, position: Int) {
        mArrListDevice?.get(position)?.let { result ->
            holder.bind(result)
        }
    }

    override fun getItemCount(): Int {
        return mArrListDevice.size
    }

    inner class ConnectStateViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var mTVOriDeviceName:TextView
        var mTVChangeDeviceName:TextView
        var mBtnChangeDeviceName:AppCompatImageButton

        init {
            mTVOriDeviceName = itemView.findViewById(R.id.TVConnectStateDeviceOriName)
            mTVChangeDeviceName = itemView.findViewById(R.id.TVConnectStateDeviceChangeName)
            mBtnChangeDeviceName = itemView.findViewById(R.id.IBConnectStateChangeName)
        }

        fun bind(result: String) {
            mTVOriDeviceName.text = result
            mTVChangeDeviceName.text = result
        }

    }
}