package com.exs.medivelskinmeasure.Device

import android.annotation.SuppressLint
import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.Context
import android.content.Intent

object BluetoothObject {

    /**
     * 장비의 블루투스 기능을 가져오기 위한 Adapter
     */
    var btAdapter:BluetoothAdapter

    init {
        btAdapter = BluetoothAdapter.getDefaultAdapter()
    }


    /**
     * 블루투스 어댑터의 상태를 체크
     */
    fun checkBtState():Boolean {
        if (btAdapter != null) {
            return btAdapter.isEnabled
        }

        return false
    }

    /**
     * 블루투스 활성화 요청
     */
    @SuppressLint("MissingPermission")
    fun requestBtEnable(context:Context, requestCode:Int) {
        val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
        (context as Activity).startActivityForResult(enableBtIntent, requestCode)
    }

    /**
     * 페어링된 장비 검색
     */
    @SuppressLint("MissingPermission")
    fun requestPairedDevices():Set<BluetoothDevice>? {
        return btAdapter?.bondedDevices
    }


}