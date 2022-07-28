package com.exs.medivelskinmeasure.Device.Bluetooth

import android.annotation.SuppressLint
import android.bluetooth.*
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.util.Log
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream
import java.lang.reflect.Method
import java.util.*
import kotlin.collections.ArrayList

class BluetoothClient(context: Context) {
    private val TAG = "BluetoothClient"
    private lateinit var mContext: Context

    //    private var btAdapter:BluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
    private lateinit var btAdapter: BluetoothAdapter
    private var clientThread: ClientThread? = null
    private var commThread: CommThread? = null
    private var inputStream: InputStream? = null
    private var outputStream: OutputStream? = null
    private var socketListener: SocketListener? = null

    private var mConnectedDevice: BluetoothDevice? = null

    init {
        mContext = context

        val manager = context.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        btAdapter = manager.adapter


    }

    private val deviceScanReciver = object : BroadcastReceiver() {
        @SuppressLint("MissingPermission")
        override fun onReceive(context: Context?, intent: Intent?) {
            intent?.let {
                val action: String? = it.action
                when (action) {
                    BluetoothDevice.ACTION_FOUND -> {
                        val device: BluetoothDevice? =
                            it.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE)
                        device?.let { dev ->
                            dev.uuids?.let { uuids ->
                                Log.e(TAG, uuids.toString())
                            }

                            dev.name?.let { name ->
                                if(name.contains("WAVU")) {
                                    Log.e(TAG, "${name} and type is ${dev.type.toString()}")
                                    onSearch(dev)
                                }
                            }

                        }
                    }
                    else -> {
                        Log.e(TAG, "Other State -> ACTION_FOUND 가 아님")
                    }
                }
            }

        }

    }

    /**
     * Bluetooth Scan
     */
    @SuppressLint("MissingPermission")
    fun scanStart() {
        val filter = IntentFilter(BluetoothDevice.ACTION_FOUND)
        mContext.registerReceiver(deviceScanReciver, filter)
        btAdapter.startDiscovery()
    }

    fun scanStop() {
        mContext.unregisterReceiver(deviceScanReciver)
        forceScanStop()
    }

    @SuppressLint("MissingPermission")
    fun forceScanStop() {
        btAdapter.cancelDiscovery()
    }

    /**
     * Socket Linstener 동작 호출
     */
    fun setOnSocketListener(listener: SocketListener?) {
        socketListener = listener
    }

    fun onConnect() {
        socketListener?.onConnect()
    }

    fun onDisconnect() {
        socketListener?.onDisconnect()
    }

    fun onLogPrint(message: String?) {
        socketListener?.onLogPrint(message)
    }

    fun onError(e: Exception) {
        socketListener?.onError(e)
    }

    fun onReceive(msg: String) {
        socketListener?.onReceive(msg)
    }

    fun onSend(msg: String) {
        socketListener?.onSend(msg)
    }

    fun onSearch(device: BluetoothDevice) {
        socketListener?.onSearch(device)
    }

    /**
     * ************ End
     */

    @SuppressLint("MissingPermission")
    fun getPairedDevices(): ArrayList<BluetoothDevice> {
        val deviceList = ArrayList<BluetoothDevice>()
        val pairedDevices = btAdapter.bondedDevices
        if (pairedDevices.size > 0) {
            for (device in pairedDevices) {
                deviceList.add(device)
            }
        }
        return deviceList
    }

    fun connectToServer(device: BluetoothDevice) {
        disconnectFromServer()
        if (clientThread != null) {
            clientThread?.stopThread()
        }

        onLogPrint("Connect to server.")
        clientThread = ClientThread(device)
        clientThread?.start()
        mConnectedDevice = device
    }

    fun disconnectFromServer() {
        if (clientThread == null) return

        try {
            clientThread?.let {
                onDisconnect();

                it.stopThread()
                it.join(1000)
                it.interrupt()

                mConnectedDevice = null
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun getConnectedDevice(): BluetoothDevice? {
        return mConnectedDevice
    }

    fun connected(socket: BluetoothSocket?) {
        // Cancel the thread that completed the connection
        if (clientThread != null) {
//            clientThread?.stopThread()
            clientThread = null
        }

        // Cancel any thread currently running a connection
        if (commThread != null) {
            commThread?.stopThread()
            commThread = null
        }

        // Start the thread to manage the connection and perform transmissions
        commThread = CommThread(socket)
        commThread?.start()

//        // Send the name of the connected device back to the UI Activity
//        MainActivity.settingsFragment?.activity?.runOnUiThread {
//            (MainActivity.settingsFragment as SettingsFragment).btConnected()
//        }

    }



    @SuppressLint("MissingPermission")
    inner class ClientThread(device: BluetoothDevice) : Thread() {
        private var socket: BluetoothSocket? = null
        private var mDevice: BluetoothDevice? = null

        @SuppressLint("MissingPermission")
        override fun run() {
            btAdapter.cancelDiscovery()

            try {
                onLogPrint("Try to connect to server..")

                socket?.connect()
            } catch (e: Exception) {
                onError(e)

                e.printStackTrace()
                disconnectFromServer()
            }

            if (socket != null) {
                onConnect()
                mConnectedDevice = mDevice

                connected(socket)
            }
        }

        fun stopThread() {
            try {
                socket?.close()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        @SuppressLint("MissingPermission")
        @Throws(IOException::class)
        private fun createBluetoothSocket(device: BluetoothDevice): BluetoothSocket? {
            if (Build.VERSION.SDK_INT >= 10) {
                try {
                    val m: Method = device.javaClass.getMethod(
                        "createInsecureRfcommSocketToServiceRecord", *arrayOf(
                            UUID::class.java
                        )
                    )
                    return m.invoke(device, BTConstant.BLUETOOTH_UUID_SPP) as BluetoothSocket?
                } catch (e: java.lang.Exception) {
                    Log.e(TAG, "Could not create Insecure RFComm Connection", e)
                }
            }
            return device.createRfcommSocketToServiceRecord(BTConstant.BLUETOOTH_UUID_SPP)
        }

        init {
            try {
                socket = createBluetoothSocket(device)
                mDevice = device
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    inner class CommThread(private val socket: BluetoothSocket?) : Thread() {

        override fun run() {
            try {
                outputStream = socket?.outputStream
                inputStream = socket?.inputStream
            } catch (e: Exception) {
                e.printStackTrace()
            }

            var len: Int
            val buffer = ByteArray(1024)
            val byteArrayOutputStream = ByteArrayOutputStream()

            while (true) {
                try {
                    socket?.inputStream?.let {
                        len = it.read(buffer)
                        val data = buffer.copyOf(len)
                        byteArrayOutputStream.write(data)

                        it.available()?.let { available ->

                            if (available == 0) {
                                val dataByteArray = byteArrayOutputStream.toByteArray()
                                val dataString = String(dataByteArray)
                                onReceive(dataString)

                                byteArrayOutputStream.reset()
                            }
                        }
                    }

                } catch (e: Exception) {
                    e.printStackTrace()
                    stopThread()
                    disconnectFromServer()
                    break
                }
            }
        }

        fun stopThread() {
            try {
                inputStream?.close()
                outputStream?.close()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun sendData(msg: String) {
        if (outputStream == null) return

        try {
            outputStream?.let {
                it.write(msg.toByteArray())
                it.flush()

                onSend(msg)

            }
        } catch (e: Exception) {
            onError(e)
            e.printStackTrace()
            disconnectFromServer()
        }
    }
}
