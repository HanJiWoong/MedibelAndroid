package com.exs.medivelskinmeasure.Device

import android.bluetooth.BluetoothDevice

interface SocketListener {
    fun onConnect()
    fun onDisconnect()
    fun onError(e: Exception?)
    fun onReceive(msg: String?)
    fun onSend(msg: String?)
    fun onSearch(device: BluetoothDevice)

    fun onLogPrint(msg: String?)
}