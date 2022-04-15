package com.exs.medivelskinmeasure.UI.Main.Connection

import android.annotation.SuppressLint
import android.bluetooth.BluetoothDevice
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.KeyEvent.KEYCODE_ENTER
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatEditText
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import com.exs.medivelskinmeasure.Device.BluetoothClient
import com.exs.medivelskinmeasure.Device.SocketListener
import com.exs.medivelskinmeasure.R
import com.exs.medivelskinmeasure.UI.Main.ConnectStateAdapter
import com.exs.medivelskinmeasure.common.CommonUtil
import com.exs.medivelskinmeasure.common.custom_ui.CommonTitleBar

class DeviceInfoActivity : AppCompatActivity() {

    private val TAG = "DeviceInfoActivity"

    private lateinit var mTitleBar: CommonTitleBar

    private var mDataBtDevice: BluetoothDevice? = null

    private lateinit var mTVOriDeviceName: TextView
    private lateinit var mTVChangeDeviceName: TextView
    private lateinit var mCLChangeDeviceNameEdit: ConstraintLayout
    private lateinit var mETChangeDeviceName: AppCompatEditText
    private lateinit var mBtnChangeDeviceNameConfirm: AppCompatButton

    private lateinit var mETAPInfoSSID: AppCompatEditText
    private lateinit var mETAPInfoPW: AppCompatEditText

    private lateinit var mBtnConnect: AppCompatButton

    private lateinit var mBtClient: BluetoothClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_device_info)

        mDataBtDevice =
            intent.getParcelableExtra(getString(R.string.intent_key_bluetooth_device_info))

        initUI()
        setCommonListener()

        initBt()
    }

    @SuppressLint("MissingPermission")
    private fun initUI() {
        mTitleBar = findViewById(R.id.ViewDeviceInfoTitleBar)
        mTitleBar.mStrTitle = getString(R.string.str_ko_device_info_title)
        mTitleBar.setCloseMode(true)

        mTVOriDeviceName = findViewById(R.id.TVDeviceInfoChangeNameOri)
        mTVChangeDeviceName = findViewById(R.id.TVDeviceInfoChangeName)
        mCLChangeDeviceNameEdit = findViewById(R.id.CLDeviceInfoChangeNameEditArea)
        mETChangeDeviceName = findViewById(R.id.ETDeviceInfoChangeName)
        mBtnChangeDeviceNameConfirm = findViewById(R.id.BtnDeviceInfoChangeNameConfirm)

        mETAPInfoSSID = findViewById(R.id.ETDeviceInfoAPSSID)
        mETAPInfoPW = findViewById(R.id.ETDeviceInfoAPPW)

        mCLChangeDeviceNameEdit.visibility = View.GONE

        mDataBtDevice?.let { data ->
            mTVOriDeviceName.text = data.name
            mTVChangeDeviceName.text = data.name

            val storedName: String? = CommonUtil.getPreferenceString(
                this,
                data.name
            )

            storedName?.let {
                if (!it.isEmpty()) {
                    mTVChangeDeviceName.text = it
                }
            }

            mETChangeDeviceName.setText(mTVChangeDeviceName.text)
        }

        mBtnConnect = findViewById(R.id.BtnDeviceInfoConnection)

//        // Test Code
//        val apName: String = "KT_GiGA_Mesh_922D"
//        val pass: String = "9aea4dh929"
//        mETAPInfoSSID.setText(apName)
//        mETAPInfoPW.setText(pass)
//
//        mBtnConnect.isEnabled = true
//        // -----------

    }

    @SuppressLint("MissingPermission")
    private fun setCommonListener() {
        mTitleBar.setOnClickBackListener {
            finish()
        }

        mTVChangeDeviceName.setOnClickListener {
            mCLChangeDeviceNameEdit.visibility = View.VISIBLE
            mTVChangeDeviceName.visibility = View.GONE

            mETChangeDeviceName.requestFocus()

            CommonUtil.showKeyboard(this, mETChangeDeviceName)
        }

        mBtnChangeDeviceNameConfirm.setOnClickListener {
            mDataBtDevice?.let { data ->
                mCLChangeDeviceNameEdit.visibility = View.GONE
                mTVChangeDeviceName.visibility = View.VISIBLE

                CommonUtil.hideKeyboard(this, mETChangeDeviceName)

                if (!mETChangeDeviceName.text.toString().isEmpty()) {
                    CommonUtil.setPreferenceString(
                        this,
                        data.name,
                        mETChangeDeviceName.text.toString()
                    )
                    mTVChangeDeviceName.text = mETChangeDeviceName.text
                }

                mETChangeDeviceName.setText(mTVChangeDeviceName.text)
            }
        }

        mETChangeDeviceName.setOnEditorActionListener { view, action, event ->

            var handled = false

            if (event.keyCode == KEYCODE_ENTER) {
                mDataBtDevice?.let { data ->
                    mCLChangeDeviceNameEdit.visibility = View.GONE
                    mTVChangeDeviceName.visibility = View.VISIBLE

                    CommonUtil.hideKeyboard(this, mETChangeDeviceName)

                    if (!mETChangeDeviceName.text.toString().isEmpty()) {
                        CommonUtil.setPreferenceString(
                            this,
                            data.name,
                            mETChangeDeviceName.text.toString()
                        )
                        mTVChangeDeviceName.text = mETChangeDeviceName.text
                    }

                    mETChangeDeviceName.setText(mTVChangeDeviceName.text)

                    handled = true
                }
            }

            handled
        }

        mETAPInfoSSID.setOnEditorActionListener { view, action, event ->

            var handled = false

            if (event.keyCode == KEYCODE_ENTER) {
                handled = true

                CommonUtil.hideKeyboard(this,mETAPInfoSSID)
            }

            if(!mETAPInfoSSID.text.toString().isEmpty() && !mETAPInfoPW.text.toString().isEmpty()) {
                mBtnConnect.isEnabled = true
            } else {
                mBtnConnect.isEnabled = false
            }

            handled
        }

        mETAPInfoPW.setOnEditorActionListener { view, action, event ->

            var handled = false

            if (event.keyCode == KEYCODE_ENTER) {
                handled = true

                CommonUtil.hideKeyboard(this,mETAPInfoPW)
            }

            if(!mETAPInfoSSID.text.toString().isEmpty() && !mETAPInfoPW.text.toString().isEmpty()) {
                mBtnConnect.isEnabled = true
            } else {
                mBtnConnect.isEnabled = false
            }

            handled
        }

        mBtnConnect.setOnClickListener {
            mDataBtDevice?.let { device ->
                mBtClient?.let {
                    it.connectToServer(device)
                }
            }
        }
    }

    fun showToast(msg: String) {
        runOnUiThread {
            Log.e(TAG, msg)
            Toast.makeText(this@DeviceInfoActivity, msg, Toast.LENGTH_SHORT)
                .show()
        }
    }

    /**
     * Bluetooth 연결
     */
    private val mOnSocketListener: SocketListener = object : SocketListener {
        @SuppressLint("MissingPermission")
        override fun onConnect() {
//            isConnected = true
            showToast("블루투스 기기 연결됨")

            val apName: String = mETAPInfoSSID.text.toString()
            val pass: String = mETAPInfoPW.text.toString()
            val nameMd5: String? = mBtClient.getConnectedDevice()?.let {
                CommonUtil.md5(it.name)
            }

            nameMd5?.let { md5 ->
                val sendStrContent =
                    "${md5}0${CommonUtil.getIPAddress(true)},SSID-${apName},PWD-${pass}"
                val length = sendStrContent.length
                var lenStr = ""

                if (length < 10) {
                    lenStr = "00" + length
                } else if (length < 100) {
                    lenStr = "0" + length
                } else {
                    lenStr = "${length}"
                }


                val sendString = "#C" + lenStr + sendStrContent

                mBtClient.sendData(sendString)
            }

//            val bleConfig:BluetoothConfigration = BluetoothConfigration()

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
                Log.e(TAG, "Send : $it\n")
            }
        }

        @SuppressLint("MissingPermission")
        override fun onSearch(device: BluetoothDevice) {

        }

        override fun onLogPrint(msg: String?) {
            msg?.let { Log.e(TAG, "$it\n") }
        }
    }

    fun initBt() {
        mBtClient = BluetoothClient(this)
        mBtClient.setOnSocketListener(mOnSocketListener)
        mBtClient.forceScanStop()
    }
}