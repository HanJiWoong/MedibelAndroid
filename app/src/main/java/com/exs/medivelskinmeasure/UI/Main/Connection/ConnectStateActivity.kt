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
import com.exs.medivelskinmeasure.UI.Main.Connection.DeviceInfoActivity
import com.exs.medivelskinmeasure.UI.Member.Join.JoinActivity
import com.exs.medivelskinmeasure.UI.Member.Join.TermDetailActivity
import com.exs.medivelskinmeasure.common.CommonUtil
import com.exs.medivelskinmeasure.common.custom_ui.CommonCheckBox
import com.exs.medivelskinmeasure.common.custom_ui.CommonTextListView
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

    private var mDeviceList: ArrayList<BluetoothDevice> = arrayListOf()

    private val mOnSocketListener: SocketListener = object : SocketListener {
        @SuppressLint("MissingPermission")
        override fun onConnect() {

        }

        override fun onDisconnect() {
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
                Log.e(TAG, "Send : $it\n")
            }
        }

        @SuppressLint("MissingPermission")
        override fun onSearch(device: BluetoothDevice) {
            var isExist = false

            for (deviceInList in mDeviceList) {
                if (device.name.equals(deviceInList.name)) {
                    isExist = true
                    break
                }
            }

            if (!isExist) {
                mDeviceList.add(device)
                setListData()
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
                arrayOf(
                    Manifest.permission.BLUETOOTH_CONNECT,
                    Manifest.permission.BLUETOOTH_SCAN,
                    Manifest.permission.BLUETOOTH_ADMIN,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ),
                REQUEST_BT_CONNECTION
            ) {
                mBtClient = BluetoothClient(this)
                mBtClient.setOnSocketListener(mOnSocketListener)

                mBtClient.scanStart()
            }
        }

    }

    override fun onResume() {
        super.onResume()

        mListAdapter.notifyDataSetChanged()

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
                    arrayOf(
                        Manifest.permission.BLUETOOTH_CONNECT,
                        Manifest.permission.BLUETOOTH_SCAN,
                        Manifest.permission.ACCESS_FINE_LOCATION
                    ),
                    REQUEST_BT_CONNECTION
                ) {
                    mBtClient = BluetoothClient(this)
                    mBtClient.setOnSocketListener(mOnSocketListener)
                    mBtClient.scanStart()

                }
            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(this, "블루투스가 비활성화 되었습니다.", Toast.LENGTH_SHORT).show()

                finish()
            }
        }
    }

    @SuppressLint("MissingPermission")
    private fun setListData() {

        mListAdapter.setDeviceList(mDeviceList, mBtClient)
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

                CommonUtil.requestPermissions(
                    this,
                    arrayOf(
                        Manifest.permission.BLUETOOTH_CONNECT,
                        Manifest.permission.BLUETOOTH_SCAN,
                        Manifest.permission.ACCESS_FINE_LOCATION
                    ),
                    REQUEST_BT_CONNECTION
                ) {
                    mBtClient = BluetoothClient(this)
                    mBtClient.setOnSocketListener(mOnSocketListener)
                    mBtClient.scanStart()
                }
            }
            REQUEST_BT_CONNECTION -> {
                Log.e(TAG, "RQUEST_BT_CONNECTION")

                mBtClient = BluetoothClient(this)
                mBtClient.setOnSocketListener(mOnSocketListener)

                mBtClient.scanStart()
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
        val view: View =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.view_common_text_list, parent, false)
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
        var mTVTitle: TextView
        var mView: View

        init {
            mView = itemView
            mTVTitle = itemView.findViewById(R.id.TVCommonTextListTitle)
        }

        @SuppressLint("MissingPermission")
        fun bind(result: BluetoothDevice) {
            mResult = result


            var presentStr: String = result.name

            val storedName: String? = CommonUtil.getPreferenceString(mContext, result.name)

            storedName?.let {
                if (!it.isEmpty()) {
                    presentStr += "(${it})"
                }
            }

            mTVTitle.text = presentStr

            mView.setOnClickListener {
                Log.e("TEST", mResult!!.name)

                val intent = Intent(mContext,DeviceInfoActivity::class.java)
                intent.putExtra(mContext.getString(R.string.intent_key_bluetooth_device_info),mResult)
                mContext.startActivity(intent)

            }

        }

    }
}