package com.exs.medivelskinmeasure.Device.Bluetooth

import android.annotation.SuppressLint
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import java.io.IOException
import java.util.*
import java.util.concurrent.Executors

class SerialSocket(context:Context,device:BluetoothDevice):Runnable {

    private val BLUETOOTH_SPP = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB")

    private var disconnectBroadcastReceiver: BroadcastReceiver? = null

    private var context: Context? = null
    private var listener: SerialListener? = null
    private var device: BluetoothDevice? = null
    private var socket: BluetoothSocket? = null
    private var connected = false

    init {
        this.context = context
        this.device = device

        disconnectBroadcastReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {
                if (listener != null) listener!!.onSerialIoError(IOException("background disconnect"))
                disconnect() // disconnect now, else would be queued until UI re-attached
            }
        }
    }

    @SuppressLint("MissingPermission")
    fun getName(): String? {
        return if (device!!.name != null) device!!.name else device!!.address
    }

    /**
     * connect-success and most connect-errors are returned asynchronously to listener
     */
    @Throws(IOException::class)
    fun connect(listener: SerialListener?) {
        this.listener = listener
        context!!.registerReceiver(
            disconnectBroadcastReceiver,
            IntentFilter(Constants.INTENT_ACTION_DISCONNECT)
        )
        Executors.newSingleThreadExecutor().submit(this)
    }

    fun disconnect() {
        listener = null // ignore remaining data and errors
        // connected = false; // run loop will reset connected
        if (socket != null) {
            try {
                socket!!.close()
            } catch (ignored: Exception) {
            }
            socket = null
        }
        try {
            context!!.unregisterReceiver(disconnectBroadcastReceiver)
        } catch (ignored: Exception) {
        }
    }

    @Throws(IOException::class)
    fun write(data: ByteArray?) {
        if (!connected) throw IOException("not connected")
        socket!!.outputStream.write(data)
    }

    @SuppressLint("MissingPermission")
    override fun run() { // connect & read
        try {
            socket = device!!.createRfcommSocketToServiceRecord(BLUETOOTH_SPP)
            socket!!.connect()

            if (listener != null) listener!!.onSerialConnect()
        } catch (e: Exception) {
            if (listener != null) listener!!.onSerialConnectError(e)
            try {
                socket!!.close()
            } catch (ignored: Exception) {
            }
            socket = null
            return
        }
        connected = true
        try {
            val buffer = ByteArray(1024)
            var len: Int
            while (true) {
                len = socket!!.getInputStream().read(buffer)
                val data = Arrays.copyOf(buffer, len)
                if (listener != null) listener!!.onSerialRead(data)
            }
        } catch (e: Exception) {
            connected = false
            if (listener != null) listener!!.onSerialIoError(e)
            try {
                socket!!.close()
            } catch (ignored: Exception) {
            }
            socket = null
        }
    }

}