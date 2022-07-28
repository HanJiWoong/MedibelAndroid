package com.exs.medivelskinmeasure.Device.Bluetooth

import android.annotation.SuppressLint
import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import android.content.Context
import android.content.Intent
import java.io.InputStream
import java.io.OutputStream

object BluetoothObject {

    private val TAG: String? = this.javaClass.name

    /**
     * 장비의 블루투스 기능을 가져오기 위한 Adapter
     */
    var btAdapter: BluetoothAdapter


    /**
     * 장비의 데이터 통신을 위한 소켓, 입출력 스트림
     */
    var mBtSocket: BluetoothSocket? = null
    var mOutputStream: OutputStream? = null
    var mInputStream: InputStream? = null

    var mWorkThread: Thread? = null
    var mArrReadBuffer: ByteArray? = null
    var mPosReadBuffer: Int = 0

    init {
        btAdapter = BluetoothAdapter.getDefaultAdapter()

    }


    /**
     * 블루투스 어댑터의 상태를 체크
     */
    fun checkBtState(): Boolean {
        if (btAdapter != null) {
            return btAdapter.isEnabled
        }

        return false
    }

    /**
     * 블루투스 활성화 요청
     */
    @SuppressLint("MissingPermission")
    fun requestBtEnable(context: Context, requestCode: Int) {
        val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
        (context as Activity).startActivityForResult(enableBtIntent, requestCode)
    }

    /**
     * 페어링된 장비 검색
     */
    @SuppressLint("MissingPermission")
    fun requestPairedDevices(): Set<BluetoothDevice>? {
        return btAdapter?.bondedDevices
    }
}

