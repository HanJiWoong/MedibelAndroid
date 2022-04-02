package com.exs.medivelskinmeasure.UI.Main

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.bluetooth.BluetoothDevice
import android.content.Context
import android.content.Intent
import android.net.wifi.ScanResult
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.MainThread
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatEditText
import androidx.appcompat.widget.AppCompatImageButton
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.exs.medivelskinmeasure.Device.BluetoothClient
import com.exs.medivelskinmeasure.Device.BluetoothObject
import com.exs.medivelskinmeasure.Device.SocketListener
import com.exs.medivelskinmeasure.R
import com.exs.medivelskinmeasure.UI.Member.Join.JoinActivity
import com.exs.medivelskinmeasure.UI.Member.Join.TermDetailActivity
import com.exs.medivelskinmeasure.common.CommonUtil
import com.exs.medivelskinmeasure.common.custom_ui.CommonCheckBox
import com.exs.medivelskinmeasure.common.custom_ui.CommonTitleBar
import org.w3c.dom.Text

class ConnectStateActivity : AppCompatActivity() {

    private val TAG = "ConnectStateActivity"

    private val REQUEST_BT_ENABLE = 100
    private val REQUEST_BT_CONNECTION = 200

    private lateinit var mTitleBar: CommonTitleBar

    private lateinit var mRVList: RecyclerView
    private lateinit var mListAdapter: ConnectStateAdapter

    private lateinit var mBtClient: BluetoothClient

    private val mOnSocketListener: SocketListener = object : SocketListener {
        override fun onConnect() {
//            isConnected = true
            showToast("블루투스 기기 연결됨")
        }

        override fun onDisconnect() {
//            isConnected = false
            showToast("블루투스 기기 연결 끊어짐")
        }

        override fun onError(e: Exception?) {
            e?.let {
                showToast(it.toString())
            }

        }

        override fun onReceive(msg: String?) {
            msg?.let {
                showToast("Receive : $it\n")
            }
        }

        override fun onSend(msg: String?) {
            msg?.let {
                showToast("Send : $it\n")
            }
        }

        override fun onLogPrint(msg: String?) {
            msg?.let { Log.e(TAG, "$it\n") }
        }
    }

    fun showToast(msg: String) {
        runOnUiThread {
            Log.e(TAG, msg)
            Toast.makeText(this@ConnectStateActivity, msg, Toast.LENGTH_SHORT)
                .show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_connect_state)

        initUI()
        setCommonListener()

        if (BluetoothObject.checkBtState() == false) {
            BluetoothObject.requestBtEnable(this, REQUEST_BT_ENABLE)
        } else {
            CommonUtil.requestPermissions(
                this,
                Manifest.permission.BLUETOOTH_CONNECT,
                REQUEST_BT_CONNECTION
            ) {
                mBtClient = BluetoothClient(this)
                mBtClient.setOnSocketListener(mOnSocketListener)

                setListData()
            }
        }

    }


    private fun initUI() {
        mTitleBar = findViewById(R.id.ViewConnectStateTitleBar)
        mTitleBar.mStrTitle = getString(R.string.str_ko_connect_state_title)
        mTitleBar.setCloseMode(true)

        mRVList = findViewById(R.id.RVConnectState)
        mRVList.layoutManager = LinearLayoutManager(this)

        mListAdapter = ConnectStateAdapter()
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

        if (requestCode == REQUEST_BT_ENABLE) {
            if (resultCode == RESULT_OK) {
                Toast.makeText(this, "블루투스가 활성화 되었습니다.", Toast.LENGTH_SHORT).show()

                CommonUtil.requestPermissions(
                    this,
                    Manifest.permission.BLUETOOTH_CONNECT,
                    REQUEST_BT_CONNECTION
                ) {
                    mBtClient = BluetoothClient(this)
                    mBtClient.setOnSocketListener(mOnSocketListener)

                    setListData()
                }
            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(this, "블루투스가 비활성화 되었습니다.", Toast.LENGTH_SHORT).show()

                finish()
            }
        }
    }

    @SuppressLint("MissingPermission")
    private fun setListData() {

        val devices: Set<BluetoothDevice>? = BluetoothObject.requestPairedDevices()
        var list = ArrayList<BluetoothDevice>()

        var isExist: Boolean = false

        devices?.let { devicelist ->
            devicelist.forEach { device ->
                if (device.name.contains("WAVU")) {
                    isExist = true
                    list.add(device)
//                    Toast.makeText(
//                        this@ConnectStateActivity,
//                        "${device.name}이 검색 됨.",
//                        Toast.LENGTH_LONG
//                    ).show()
                }
            }
        }

        if (!isExist) {
            Toast.makeText(this@ConnectStateActivity, "검색된 장비가 없습니다.", Toast.LENGTH_LONG).show()
        }

        mListAdapter.setDeviceList(list, mBtClient)
        mListAdapter.notifyDataSetChanged()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when (requestCode) {
            REQUEST_BT_ENABLE -> {
                Log.e(TAG, "RQUEST_BT_ENABLED")

//                CommonUtil.requestPermissions(
//                    this,
//                    Manifest.permission.BLUETOOTH_CONNECT,
//                    REQUEST_BT_CONNECTION
//                ) {
//                    mBtClient = BluetoothClient(this)
//                    mBtClient.setOnSocketListener(mOnSocketListener)
//
//                    setListData()
//                }
            }
            REQUEST_BT_CONNECTION -> {
                Log.e(TAG, "RQUEST_BT_CONNECTION")

                mBtClient = BluetoothClient(this)
                mBtClient.setOnSocketListener(mOnSocketListener)

                setListData()
            }
        }
    }
}

class ConnectStateAdapter : RecyclerView.Adapter<ConnectStateAdapter.ConnectStateViewHolder>() {
    private var mArrListDevice: ArrayList<BluetoothDevice> = ArrayList()
    private lateinit var mContext: Context
    private var mBtClient: BluetoothClient? = null

    fun setDeviceList(list: ArrayList<BluetoothDevice>, btClient: BluetoothClient) {
        mArrListDevice = list
        mBtClient = btClient
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ConnectStateViewHolder {
        val view: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_connect_state, parent, false)
        mContext = parent.context
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

        var mResult: BluetoothDevice? = null

        var mCLItem: ConstraintLayout
        var mTVOriDeviceName: TextView
        var mTVChangeDeviceName: TextView
        var mBtnChangeDeviceName: AppCompatImageButton
        var mCLChangeDeviceName: ConstraintLayout
        var mETChangeDeviceName: AppCompatEditText
        var mBtnChangeDeviceNameCancel: AppCompatImageButton

        init {
            mCLItem = itemView.findViewById(R.id.CLConnectStateItem)
            mTVOriDeviceName = itemView.findViewById(R.id.TVConnectStateDeviceOriName)
            mTVChangeDeviceName = itemView.findViewById(R.id.TVConnectStateDeviceChangeName)
            mBtnChangeDeviceName = itemView.findViewById(R.id.IBConnectStateChangeName)
            mCLChangeDeviceName = itemView.findViewById(R.id.CLConnectStateDeviceChangeName)
            mETChangeDeviceName = itemView.findViewById(R.id.ETConnectStateDeviceChangeName)
            mBtnChangeDeviceNameCancel =
                itemView.findViewById(R.id.IBConnectStateDeviceChangeNameCancel)

            mBtnChangeDeviceName.isSelected = false
            mCLChangeDeviceName.visibility = View.GONE
        }

        @SuppressLint("MissingPermission")
        fun bind(result: BluetoothDevice) {
            mResult = result

            mTVOriDeviceName.text = result.name
            mTVChangeDeviceName.text = result.name

            val storedName: String? = CommonUtil.getPreferenceString(mContext, result.name)

            storedName?.let {
                if (!it.isEmpty()) {
                    mTVChangeDeviceName.text = it
                }
            }

            mETChangeDeviceName.setText(mTVChangeDeviceName.text)

            mBtnChangeDeviceName.setOnClickListener {
                if (it.isSelected) {
                    mCLChangeDeviceName.visibility = View.GONE
                    mTVChangeDeviceName.visibility = View.VISIBLE

                    it.isSelected = false

                    CommonUtil.hideKeyboard(mContext, mETChangeDeviceName)

                    if (!mETChangeDeviceName.text.toString().isEmpty()) {
                        CommonUtil.setPreferenceString(
                            mContext,
                            result.name,
                            mETChangeDeviceName.text.toString()
                        )

                        mTVChangeDeviceName.text = mETChangeDeviceName.text
                    }

                    mETChangeDeviceName.setText(mTVChangeDeviceName.text)


                } else {
                    mCLChangeDeviceName.visibility = View.VISIBLE
                    mTVChangeDeviceName.visibility = View.GONE

                    it.isSelected = true
                    mETChangeDeviceName.requestFocus()

                    CommonUtil.showKeyboard(mContext, mETChangeDeviceName)
                }
            }

            mBtnChangeDeviceNameCancel.setOnClickListener {
                mCLChangeDeviceName.visibility = View.GONE
                mTVChangeDeviceName.visibility = View.VISIBLE

                mBtnChangeDeviceName.isSelected = false

                CommonUtil.hideKeyboard(mContext, mETChangeDeviceName)

                mETChangeDeviceName.setText(mTVChangeDeviceName.text)
            }

            mCLItem.setOnClickListener {
                mResult?.let { device ->
                    mBtClient?.let {
                        it.connectToServer(device)
                    }
                }
            }

        }

    }
}