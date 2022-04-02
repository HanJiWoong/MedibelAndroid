package com.exs.medivelskinmeasure.Device

import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.bluetooth.BluetoothSocket
import android.content.Context
import java.io.ByteArrayOutputStream
import java.io.InputStream
import java.io.OutputStream

class BluetoothClient(context:Context) {

//    private var btAdapter:BluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
    private lateinit var btAdapter:BluetoothAdapter
    private var clientThread: ClientThread? = null
    private var commThread: CommThread? = null
    private var inputStream: InputStream? = null
    private var outputStream: OutputStream? = null
    private var socketListener: SocketListener? = null

    init {
        val manager = context.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        btAdapter = manager.adapter
    }

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
    }

    fun disconnectFromServer() {
        if (clientThread == null) return

        try {
            clientThread?.let {
                onDisconnect();

                it.stopThread()
                it.join(1000)
                it.interrupt()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun connected(socket: BluetoothSocket?) {
        // Cancel the thread that completed the connection
        if (clientThread != null) {
            clientThread?.stopThread()
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
    inner class ClientThread(val device: BluetoothDevice) : Thread() {
        private var socket: BluetoothSocket? = null

        @SuppressLint("MissingPermission")
        override fun run() {
//            btAdapter.cancelDiscovery()

            for (uuid in device.uuids) {
                try {
                    onLogPrint("Try to connect to server..")
                    val mmSocket: BluetoothSocket? =
                        device.createRfcommSocketToServiceRecord(uuid.uuid)

                    mmSocket?.let { thisSocket ->
                        thisSocket.connect()

                        socket = thisSocket
                        connected(socket)
                    }

                    break
                } catch (e: Exception) {
                    stopThread()

                    onError(e)

                    e.printStackTrace()

                }
            }

//            if (socket != null) {
//                onConnect()
//
//                commThread = CommThread(socket)
//                commThread?.start()
//            }
        }

        fun stopThread() {
            try {
                socket?.close()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    internal inner class CommThread(private val socket: BluetoothSocket?): Thread() {

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
                    len = socket?.inputStream?.read(buffer)!!
                    val data = buffer.copyOf(len)
                    byteArrayOutputStream.write(data)

                    socket.inputStream?.available()?.let { available ->

                        if (available == 0) {
                            val dataByteArray = byteArrayOutputStream.toByteArray()
                            val dataString = String(dataByteArray)
                            onReceive(dataString)

                            byteArrayOutputStream.reset()
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
                onSend(msg)

                it.write(msg.toByteArray())
                it.flush()
            }
        } catch (e: Exception) {
            onError(e)
            e.printStackTrace()
            disconnectFromServer()
        }
    }
}
